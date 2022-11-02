package io.github.schneiderlin.fetch.io;

import java.util.function.Function;
import java.util.function.Supplier;

public class IODefer<A> implements IO<A> {
    private Supplier<A> a;

    public IODefer(Supplier<A> a) {
        this.a = a;
    }

    @Override
    public A performIO() {
        return a.get();
    }

    @Override
    public <B> IO<B> flatMap(Function<A, IO<B>> f) {
        return new IOFlatMap(this, a -> f.apply((A) a));
    }
}
