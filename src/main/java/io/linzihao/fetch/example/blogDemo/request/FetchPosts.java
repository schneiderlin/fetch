package io.linzihao.fetch.example.blogDemo.request;

import io.linzihao.fetch.Request;
import io.vavr.collection.List;

public class FetchPosts implements Request<Object, List<Long>> {
    public Object getId() {
        return null;
    }

    @Override
    public String getTag() {
        return "FetchPosts";
    }
}
