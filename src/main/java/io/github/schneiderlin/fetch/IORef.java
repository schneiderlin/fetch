package io.github.schneiderlin.fetch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IORef<A> {
    public A a;

    public static <A> IO<IORef<A>> newIORef(A a) {
        return IO.value(new IORef<>(null));
    }

    public static <A> IO<A> readIORef(IORef<A> ref) {
        return () -> ref.a;
    }

    public static <A> IO<Void> writeIORef(IORef<A> ref, A a) {
        ref.a = a;
        return IO.noop();
    }
}
