package io.github.schneiderlin.fetch.example.study;

import io.github.schneiderlin.fetch.example.study.model.Order;
import io.github.schneiderlin.fetch.example.study.model.OrderDetail;
import io.vavr.collection.List;


public class Database {

    // XML,
    public static List<Order> orderByIds(List<String> orderIds) {
        return orderIds.map(oid -> {
            Order order = new Order();
            order.id = oid;
            return order;
        });
    }

    public static List<OrderDetail> detailByIds(List<String> detailIds) {
        return null;
    }
}
