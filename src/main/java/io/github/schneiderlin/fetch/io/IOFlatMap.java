package io.github.schneiderlin.fetch.io;

import io.github.schneiderlin.fetch.Trampoline;
import io.vavr.Function1;
import io.vavr.collection.List;

import java.util.function.Function;

public class IOFlatMap<A> implements IO<A> {
    private IO<Object> inner;
    private Function1<Object, IO<A>> mapping;

    public IOFlatMap(IO<Object> inner, Function1<Object, IO<A>> mapping) {
        this.inner = inner;
        this.mapping = mapping;
    }

    @Override
    public A performIO() {
        Object o = inner.performIO();
        return mapping.apply(o).performIO();

        //Function1<Object, IO<Object>> casted = (Function1<Object, IO<Object>>) (Object) mapping;
        //List<Function1<Object, IO<Object>>> initFs = List.of(casted);
        //return tailCall(initFs, inner).result();
    }

    //private Trampoline<A> tailCall(List<Function1<Object, IO<Object>>> fs, IO<Object> current) {
    //    if (current instanceof IODone) {
    //        IODone done = (IODone) current;
    //        Object result = done.performIO();
    //        return Trampoline.more(() -> backward(fs, result));
    //    } else if (current instanceof IODefer) {
    //        IODefer defer = (IODefer) current;
    //        Object result = defer.performIO();
    //        return Trampoline.more(() -> backward(fs, result));
    //    } else {
    //        IOFlatMap flatMap = (IOFlatMap) current;
    //        List<Function1<Object, IO<Object>>> newFs = fs.prepend(flatMap.mapping);
    //        return Trampoline.more(() -> tailCall(newFs, flatMap.inner));
    //    }
    //}

    //private Trampoline<A> backward(List<Function1<Object, IO<Object>>> fs, Object current) {
    //    if (fs.size() == 0) {
    //        return Trampoline.done((A) current);
    //    } else {
    //        Function1<Object, IO<Object>> f = fs.head();
    //        List<Function1<Object, IO<Object>>> tail = fs.tail();
    //        return Trampoline.more(() -> tailCall(tail, f.apply(current)));
    //    }
    //}

    @Override
    public <B> IO<B> flatMap(Function<A, IO<B>> f) {
        return new IOFlatMap(this, a -> f.apply((A) a));
    }
}
