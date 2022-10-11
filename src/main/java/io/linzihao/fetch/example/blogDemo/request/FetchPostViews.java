package io.linzihao.fetch.example.blogDemo.request;


import io.linzihao.fetch.Request;

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
