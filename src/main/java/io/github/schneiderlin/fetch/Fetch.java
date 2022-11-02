package io.github.schneiderlin.fetch;

import io.github.schneiderlin.fetch.io.IO;
import io.vavr.*;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public class Fetch<A> {
    private IO<Result<A>> unFetch;

    public static <A> Fetch<A> unit(A a) {
        return new Fetch<>(IO.value(new Done<>(a)));
    }

    public <B> Fetch<B> map(Function1<A, B> f) {
        return map(this, f);
    }

    // functorial combine
    public static <K, A, B> Fetch<B> map(Fetch<A> fa, Function1<A, B> f) {
        return new Fetch<>(fa.unFetch.flatMap(ra -> {
            if (ra instanceof Done) {
                Done<A> ra_ = (Done) ra;
                return IO.value(new Done<>(f.apply(ra_.a)));
            }
            if (ra instanceof Blocked) {
                Blocked<K, A> ra_ = (Blocked) ra;
                return IO.value(new Blocked<>(ra_.blockedRequests, map(ra_.cont, f)));
            }
            throw new RuntimeException("unreachable");
        }));
    }

    // applicative combine
    public static <K, A, B> Fetch<B> app(Fetch<Function1<A, B>> ff, Fetch<A> fa) {
        return new Fetch<>(ff.unFetch.flatMap(rf -> {
            return fa.unFetch.flatMap(ra -> {

                if (rf instanceof Done && ra instanceof Done) {
                    Done<Function1<A, B>> rf_ = (Done) rf;
                    Done<A> fa_ = (Done) ra;
                    return IO.value(new Done<>(rf_.a.apply(fa_.a)));
                }
                if (rf instanceof Done && ra instanceof Blocked) {
                    Done<Function1<A, B>> rf_ = (Done) rf;
                    Blocked<K, A> fa_ = (Blocked) ra;
                    return IO.value(new Blocked<>(fa_.blockedRequests, map(fa_.cont, rf_.a)));
                }
                if (rf instanceof Blocked && ra instanceof Done) {
                    Blocked<K, Function1<A, B>> ff_ = (Blocked) rf;
                    return IO.value(new Blocked<>(ff_.blockedRequests, app(ff_.cont, fa)));
                }
                if (rf instanceof Blocked && ra instanceof Blocked) {
                    Blocked<K, Function1<A, B>> ff_ = (Blocked) rf;
                    Blocked<K, A> fa_ = (Blocked) ra;
                    return IO.value(new Blocked<>(fa_.blockedRequests.appendAll(ff_.blockedRequests), app(ff_.cont, fa_.cont)));
                }
                throw new RuntimeException("unreachable");

            });
        }));
    }

    // applicative helper function
    public static <A, B> Fetch<List<B>> mapM(List<A> as, Function1<A, Fetch<B>> f) {
        List<Fetch<B>> fbs = as.map(f);
        Fetch<List<B>> zero = unit(List.empty());

        Function1<B, Function1<List<B>, List<B>>> map2F =
                b -> bs -> bs.append(b);

        Function2<Fetch<List<B>>, Fetch<B>, Fetch<List<B>>> foldF = (flb, fb) ->
                appCombine(map2F, fb, flb);

        return fbs.foldLeft(zero, foldF);
    }

    public static <A, B> Fetch<List<B>> concatM(List<A> as, Function1<A, Fetch<List<B>>> f) {
        Fetch<List<List<B>>> bssFetch = mapM(as, f);
        return bssFetch.map(bss -> bss.flatMap(bs -> bs));
    }

    public static <A, B> Fetch<java.util.List<B>> mapM(java.util.List<A> as, Function1<A, Fetch<B>> f) {
        return mapM(List.ofAll(as), f)
                .map(Value::toJavaList);
    }

    public static <A, B, C> Fetch<C> appCombine(Function1<A, Function1<B, C>> f, Fetch<A> fa, Fetch<B> fb) {
        return app(app(unit(f), fa), fb);
    }

    public static <A, B, C> Fetch<C> appCombine(Function2<A, B, C> f, Fetch<A> fa, Fetch<B> fb) {
        return app(app(unit(f.curried()), fa), fb);
    }

    public static <A, B, C, D> Fetch<D> appCombine(Function3<A, B, C, D> f, Fetch<A> fa, Fetch<B> fb, Fetch<C> fc) {
        return app(app(app(unit(f.curried()), fa), fb), fc);
    }

    public static <A, B, C, D, E> Fetch<E> appCombine(Function4<A, B, C, D, E> f, Fetch<A> fa, Fetch<B> fb, Fetch<C> fc, Fetch<D> fd) {
        return app(app(app(app(unit(f.curried()), fa), fb), fc), fd);
    }

    public static <A1, A2, A3, A4, A5, R> Fetch<R> appCombine(Function5<A1, A2, A3, A4, A5, R> f,
                                                              Fetch<A1> fa1,
                                                              Fetch<A2> fa2,
                                                              Fetch<A3> fa3,
                                                              Fetch<A4> fa4,
                                                              Fetch<A5> fa5) {
        return app(app(app(app(app(unit(f.curried()), fa1), fa2), fa3), fa4), fa5);
    }

    public <B> Fetch<B> flatMap(Function1<A, Fetch<B>> f) {
        return flatMap(this, f);
    }

    // monadic combine
    public static <K, A, B> Fetch<B> flatMap(Fetch<A> fa, Function1<A, Fetch<B>> f) {
        return new Fetch<B>(fa.unFetch.flatMap(ra -> {

            if (ra instanceof Done) {
                Done<A> ra_ = (Done) ra;
                return f.apply(ra_.a).unFetch;
            }
            if (ra instanceof Blocked) {
                Blocked<K, A> fa_ = (Blocked) ra;
                return IO.value(new Blocked<>(fa_.blockedRequests, flatMap(fa_.cont, f)));
            }
            throw new RuntimeException("unreachable");

        }));
    }

    private static IO<IORef<Object>> createBox() {
        return IORef.newIORef(null);
    }

    // wrap a request into a fetch
    public static <K, A> Fetch<A> dataFetch(Request<K, A> request) {
        IO<Result<A>> ioa = createBox().flatMap(box -> {
            BlockedRequest<K, Object> br = (BlockedRequest<K, Object>) new BlockedRequest<K, A>(box, request);

            IO<Result<A>> contR = IORef.readIORef(box)
                    .flatMap(status -> {
                        A a = (A) status;
                        return IO.value(new Done<>(a));
                    });
            Fetch<A> cont = new Fetch<>(contR);
            return IO.value(new Blocked<K, A>(List.of(br), cont));
        });
        return new Fetch<A>(ioa);
    }

    // given a function that can resolve blockedRequest, eval the program
    public static <A> IO<A> runFetch(Function1<List<BlockedRequest<Object, Object>>, IO<Void>> resolver, Fetch<A> fa) {
        return fa.unFetch.flatMap(ra -> {

            if (ra instanceof Done) {
                Done<A> ra_ = (Done) ra;
                return IO.value(ra_.a);
            }
            if (ra instanceof Blocked) {
                Blocked<Object, A> ra_ = (Blocked) ra;
                return resolver
                        .apply(ra_.blockedRequests)
                        .flatMap(unit -> runFetch(resolver, ra_.cont));
            }
            throw new RuntimeException("unreachable");
        });
    }

}
