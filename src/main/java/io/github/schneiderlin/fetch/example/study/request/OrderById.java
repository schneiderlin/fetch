package io.github.schneiderlin.fetch.example.study.request;

import io.github.schneiderlin.fetch.FetchContext;
import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.Database;
import io.github.schneiderlin.fetch.example.study.model.Order;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// Request 这个 interface 怎么强制实现方都实现 static Map<String, Order> batchQuery(List<String> oids)
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

    public static Map<String, Order> batchQuery(FetchContext fetchContext, List<String> oids) {
        Database database = fetchContext.getBean("database", Database.class);
        return database.orderByIds(oids)
                .toMap(order -> new Tuple2<>(order.id, order));
    }
}
