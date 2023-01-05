package io.github.schneiderlin.fetch.example.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlogLikeById implements Request<String, Integer> {
    private String blogId;

    @Override
    public String getTag() {
        return "BlogLikeById";
    }

    @Override
    public String getId() {
        return blogId;
    }
}
