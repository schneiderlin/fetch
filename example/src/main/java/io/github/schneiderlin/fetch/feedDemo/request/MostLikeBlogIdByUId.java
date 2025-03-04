package io.github.schneiderlin.fetch.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MostLikeBlogIdByUId implements Request<String, String> {
    private String uid;


    @Override
    public String getId() {
        return uid;
    }
}
