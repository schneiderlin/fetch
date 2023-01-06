package io.github.schneiderlin.fetch.example.study.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.model.Order;

// K 是数据库 id 类型
// A 是数据库返回类型
public class OrderById implements Request<String, Order> {
    public String oid;

    public OrderById(String oid) {
        this.oid = oid;
    }

    @Override
    public String getTag() {
        return OrderById.class.getCanonicalName();
    }

    @Override
    public String getId() {
        return oid;
    }
}
