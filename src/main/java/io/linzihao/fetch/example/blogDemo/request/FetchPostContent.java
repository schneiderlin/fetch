package io.linzihao.fetch.example.blogDemo.request;

import io.linzihao.fetch.Request;
import io.linzihao.fetch.example.blogDemo.model.PostContent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchPostContent implements Request<Long, PostContent> {
    public long postId;

    @Override
    public String getTag() {
        return "FetchPostContent";
    }

    @Override
    public Long getId() {
        return postId;
    }
}
