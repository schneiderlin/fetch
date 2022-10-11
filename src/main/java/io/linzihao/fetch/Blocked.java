package io.linzihao.fetch;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Blocked<K, A> implements Result<A> {
    List<BlockedRequest<K, Object>> blockedRequests;

    Fetch<A> cont;
}
