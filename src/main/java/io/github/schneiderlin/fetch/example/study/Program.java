package io.github.schneiderlin.fetch.example.study;

import io.github.schneiderlin.fetch.FetchContext;
import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.model.Order;
import io.github.schneiderlin.fetch.example.study.model.OrderDetail;
import io.github.schneiderlin.fetch.example.study.request.OrderById;
import io.vavr.collection.List;

public class Program {
    public static void main(String[] args) {
        List<String> oids = List.of("1", "2", "3", "4", "5");
        Fetch<List<Order>> orders = Fetch.mapM(oids, Program::orderFetch);

        FetchContext fetchContext = FetchContext.empty();
        fetchContext.addBean("database", new Database());

        List<Order> os = Fetch.resolve(fetchContext, orders);
        System.out.println(os);
    }

    // 用一个 order id, 查一个 order
    private static Order orderGet(String orderId) {
        System.out.println("查询 order 表");
        return new Order();
    }

    // 一个 order id 查一个 order
    private static Fetch<Order> orderFetch(String orderId) {
        Request<String, Order> request = new OrderById(orderId);
        return request.toFetch();
    }

    // 一个 order id 查多个 detail
    private Fetch<List<OrderDetail>> orderDetailsByOid(String oid) {
        return null;
    }
}
