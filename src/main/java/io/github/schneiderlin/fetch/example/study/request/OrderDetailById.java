package io.github.schneiderlin.fetch.example.study.request;

import io.github.schneiderlin.fetch.FetchContext;
import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.Database;
import io.github.schneiderlin.fetch.example.study.model.Order;
import io.github.schneiderlin.fetch.example.study.model.OrderDetail;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class OrderDetailById implements Request<String, OrderDetail> {
    public String detailId;

    public OrderDetailById(String detailId) {
        this.detailId = detailId;
    }

    @Override
    public String getTag() {
        return OrderDetailById.class.getCanonicalName();
    }

    @Override
    public String getId() {
        return detailId;
    }

    public static Map<String, OrderDetail> batchQuery(FetchContext fetchContext, List<String> oids) {
        Database database = fetchContext.getBean("database", Database.class);
        return database.detailByIds(oids)
                .toMap(order -> new Tuple2<>(order.detailId, order));
    }
}