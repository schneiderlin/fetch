package io.github.schneiderlin.fetch.schoolDemo.model;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Grade {
    private String id;
    private String name;
    private List<String> classes;
}
