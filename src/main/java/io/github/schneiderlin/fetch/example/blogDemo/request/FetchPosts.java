package io.github.schneiderlin.fetch.example.blogDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.vavr.collection.List;

public class FetchPosts implements Request<Object, List<Long>> {
    public Object getId() {
        return null;
    }
}
