package io.linzihao.fetch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockedRequest<K, A> {
    public IORef<Object> result;

    public Request<K, A> request;
}
