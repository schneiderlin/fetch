package io.github.schneiderlin.fetch.example.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Student;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StudentById implements Request<String, Student> {

    private String id;

    @Override
    public String getId() {
        return id;
    }
}
