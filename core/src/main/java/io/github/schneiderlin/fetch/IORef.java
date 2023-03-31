package io.github.schneiderlin.fetch;

import io.github.schneiderlin.fetch.io.IO;
import io.github.schneiderlin.fetch.io.IODefer;
import io.github.schneiderlin.fetch.io.IODone;
import io.github.schneiderlin.fetch.io.IOFlatMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IORef<A> {
    public A a;

    public static <A> IO<IORef<A>> newIORef(A a) {
        return new IODone<>(new IORef<>(null));
    }

    public static <A> IO<A> readIORef(IORef<A> ref) {
        return new IODefer<>(() -> ref.a);
    }

    public static <A> IO<Void> writeIORef(IORef<A> ref, A a) {
        ref.a = a;
        return IO.noop();
    }
}
