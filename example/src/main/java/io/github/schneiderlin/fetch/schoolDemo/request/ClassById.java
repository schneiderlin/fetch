package io.github.schneiderlin.fetch.schoolDemo.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.schoolDemo.model.Clazz;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClassById implements Request<String, Clazz> {

    private String id;

    @Override
    public String getId() {
        return id;
    }
}
