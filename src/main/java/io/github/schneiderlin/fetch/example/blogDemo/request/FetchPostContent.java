package io.github.schneiderlin.fetch.example.blogDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.blogDemo.model.PostContent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchPostContent implements Request<Long, PostContent> {
    public long postId;

    @Override
    public Long getId() {
        return postId;
    }
}
