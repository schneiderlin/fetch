package io.github.schneiderlin.fetch.example.schoolDemo.model;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class School {
    private String id;
    private String name;
    private List<String> gradeIds;
}
