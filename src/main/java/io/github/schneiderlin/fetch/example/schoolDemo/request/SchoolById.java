package io.github.schneiderlin.fetch.example.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Grade;
import io.github.schneiderlin.fetch.example.schoolDemo.model.School;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SchoolById implements Request<String, School> {

    private String id;

    @Override
    public String getTag() {
        return "SchoolById";
    }

    @Override
    public String getId() {
        return id;
    }
}
