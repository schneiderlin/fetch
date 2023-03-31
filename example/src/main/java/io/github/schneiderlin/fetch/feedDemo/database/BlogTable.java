package io.github.schneiderlin.fetch.feedDemo.database;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogTable {
    private String id;
    private String uid;
    private String title;
    private String preview;
    private LocalDateTime publishTime;
}
