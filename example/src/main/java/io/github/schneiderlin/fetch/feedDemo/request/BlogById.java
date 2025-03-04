package io.github.schneiderlin.fetch.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.feedDemo.database.BlogTable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlogById implements Request<String, BlogTable> {
    private String blogId;


    @Override
    public String getId() {
        return blogId;
    }
}
