package io.github.schneiderlin.fetch.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlogLikeById implements Request<String, Integer> {
    private String blogId;

    @Override
    public String getId() {
        return blogId;
    }
}
