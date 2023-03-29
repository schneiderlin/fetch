package io.github.schneiderlin.fetch.blogDemo.model;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class PostInfo {
    public long id;
    public LocalDateTime date;
    public String topic;
}
