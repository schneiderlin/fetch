package io.github.schneiderlin.fetch.feedDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.feedDemo.database.ReviewTable;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ReviewsByBlogId implements Request<String, List<ReviewTable>> {
    private String blogId;

    @Override
    public String getId() {
        return blogId;
    }
}
