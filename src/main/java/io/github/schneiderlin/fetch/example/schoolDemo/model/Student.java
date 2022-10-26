package io.github.schneiderlin.fetch.example.schoolDemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {
    private String id;
    private String name;
}
