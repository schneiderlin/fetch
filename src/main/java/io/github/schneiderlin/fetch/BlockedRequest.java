package io.github.schneiderlin.fetch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockedRequest<K, A> {

    public IORef<Object> result;

    public Request<K, A> request;
}
