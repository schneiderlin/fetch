package io.linzihao.fetch.example.blogDemo.request;

import io.linzihao.fetch.Request;
import io.linzihao.fetch.example.blogDemo.model.PostInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchPostInfo implements Request<Long, PostInfo> {
    public long postId;

    @Override
    public String getTag() {
        return "FetchPostInfo";
    }

    @Override
    public Long getId() {
        return postId;
    }
}
