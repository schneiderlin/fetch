package io.github.schneiderlin.fetch.example.study;

import io.github.schneiderlin.fetch.example.study.model.Order;
import io.github.schneiderlin.fetch.example.study.model.OrderDetail;
import io.vavr.collection.List;


public class Database {

    public List<Order> orderByIds(List<String> orderIds) {
        return orderIds.map(oid -> {
            Order order = new Order();
            order.id = oid;
            return order;
        });
    }

    public List<OrderDetail> detailByIds(List<String> detailIds) {
        return null;
    }
}
