package io.github.schneiderlin.fetch.io;

import io.vavr.collection.List;

import java.util.function.Function;

public interface IO<A> {
    static <A> IO<A> value(A a) {
        return new IODone<>(a);
    }

    A performIO();

    <B> IO<B> flatMap(Function<A, IO<B>> f);

    static IO<Void> noop() {
        return new IODone<>(null);
    }

    default <R> IO<R> andThen(IO<R> io) {
        return flatMap(ignoredResult -> io);
    }

    static IO<Void> sequence(List<IO<?>> ios) {
        return ios.fold(noop(), IO::andThen)
                .andThen(noop());
    }

    static IO<Void> parallel(List<IO<?>> ios) {
        ios.forEach(IO::performIO);
        return noop();
    }
}
