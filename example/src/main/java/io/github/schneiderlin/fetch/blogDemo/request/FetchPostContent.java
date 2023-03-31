package io.github.schneiderlin.fetch.blogDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.blogDemo.model.PostContent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchPostContent implements Request<Long, PostContent> {
    public long postId;

    @Override
    public Long getId() {
        return postId;
    }
}
