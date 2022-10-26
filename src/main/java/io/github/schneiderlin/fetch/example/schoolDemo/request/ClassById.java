package io.github.schneiderlin.fetch.example.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Clazz;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ClassById implements Request<String, Clazz> {

    private String id;

    @Override
    public String getTag() {
        return "ClassById";
    }

    @Override
    public String getId() {
        return id;
    }
}
