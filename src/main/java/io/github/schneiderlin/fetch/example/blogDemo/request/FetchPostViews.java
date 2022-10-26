package io.github.schneiderlin.fetch.example.blogDemo.request;


import io.github.schneiderlin.fetch.Request;

public class FetchPostViews implements Request<Long, Integer> {
    long postId;

    @Override
    public String getTag() {
        return "FetchPostViews";
    }

    @Override
    public Long getId() {
        return postId;
    }
}
