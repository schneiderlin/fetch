package io.github.schneiderlin.fetch.example.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MostRecentBlogIdByUId implements Request<String, String> {
    private String uid;


    @Override
    public String getId() {
        return uid;
    }
}
