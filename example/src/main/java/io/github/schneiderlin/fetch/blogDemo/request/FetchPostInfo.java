package io.github.schneiderlin.fetch.blogDemo.request;

import io.github.schneiderlin.fetch.blogDemo.model.PostInfo;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchPostInfo implements Request<Long, PostInfo> {
    public long postId;

    @Override
    public Long getId() {
        return postId;
    }
}
