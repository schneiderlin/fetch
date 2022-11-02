package io.github.schneiderlin.fetch.io;

import io.github.schneiderlin.fetch.Trampoline;

import java.util.function.Function;

public class IODone<A> implements IO<A> {
    private A a;

    public IODone(A a) {
        this.a = a;
    }

    @Override
    public A performIO() {
        return a;
    }

    @Override
    public <B> IO<B> flatMap(Function<A, IO<B>> f) {
        return new IOFlatMap(this, a -> f.apply((A) a));
    }
}
