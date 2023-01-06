package io.github.schneiderlin.fetch.example.study.request;

import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.Database;
import io.github.schneiderlin.fetch.example.study.model.Order;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

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

    public static Map<String, Order> batchQuery(List<String> oids) {
        return Database.orderByIds(oids)
                .toMap(order -> new Tuple2<>(order.id, order));
    }
}
