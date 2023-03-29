package io.github.schneiderlin.fetch.example.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class BlogIdsByUId implements Request<String, List<String>> {
    private String uid;

    @Override
    public String getId() {
        return uid;
    }
}
