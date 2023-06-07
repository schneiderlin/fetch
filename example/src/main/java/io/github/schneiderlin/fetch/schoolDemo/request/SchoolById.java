package io.github.schneiderlin.fetch.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.schoolDemo.model.School;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SchoolById implements Request<String, School> {

    private String id;

    @Override
    public String getId() {
        return id;
    }
}
