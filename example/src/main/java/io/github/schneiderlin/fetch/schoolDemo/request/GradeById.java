package io.github.schneiderlin.fetch.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.schoolDemo.model.Grade;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GradeById implements Request<String, Grade> {

    private String id;

    @Override
    public String getId() {
        return id;
    }
}
