package io.linzihao.fetch;

public interface Request<K, A> {
    default Fetch<A> toFetch() {
        return Fetch.dataFetch(this);
    }

    String getTag();

    K getId();
}
