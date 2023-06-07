package io.github.schneiderlin.fetch.feedDemo.database;

import lombok.Data;

@Data
public class ReviewTable {
    private String userId;
    private String blogId;
    private String review;
}
