package io.github.schneiderlin.fetch;

public interface Request<K, A> {
    default Fetch<A> toFetch() {
        return Fetch.dataFetch(this);
    }


    K getId();
}
