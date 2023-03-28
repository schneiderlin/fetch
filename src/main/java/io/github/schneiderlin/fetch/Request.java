package io.github.schneiderlin.fetch;

public interface Request<K, A> {
    default Fetch<A> toFetch() {
        return Fetch.dataFetch(this);
    }

    default String getTag() {
        return this.getClass().getCanonicalName();
    }

    K getId();
}
