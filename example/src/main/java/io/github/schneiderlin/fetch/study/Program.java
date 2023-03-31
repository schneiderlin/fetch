package io.github.schneiderlin.fetch.study;

import io.github.schneiderlin.fetch.FetchContext;
import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.study.model.Order;
import io.github.schneiderlin.fetch.study.model.OrderDetail;
import io.github.schneiderlin.fetch.study.request.OrderById;
import io.github.schneiderlin.fetch.study.request.OrderDetailById;
import io.vavr.collection.List;

public class Program {
    public static void main(String[] args) {
        //List<String> oids = List.of("1", "2", "3", "4", "5");
        //Fetch<List<Order>> orders = Fetch.mapM(oids, Program::orderFetch);
        //
        //FetchContext fetchContext = FetchContext.empty();
        //fetchContext.addBean("database", new Database());
        //List<Order> os = Fetch.resolve(fetchContext, orders);
        //
        //System.out.println(os);

        multipleLevel();
    }

    private static void multipleLevel() {
        // 查 10 个 order, 每个 order 2 个 detail
        // 每个 order 查里面所有 detail

        // fetch 保留最清晰的语义, 但是不牺牲性能
        List<String> orderIds = List.of("1", "2", "3", "4", "5");
        Fetch<List<Order>> listFetch2 = Fetch.mapM(orderIds, Program::orderFetch)
                .map(os -> {
                    os.forEach(System.out::println);
                    return os;
                });

        FetchContext fetchContext = FetchContext.empty();
        fetchContext.addBean("database", new Database());
        List<Order> os = Fetch.resolve(fetchContext, listFetch2);

        //listFetch.flatMap(os -> {
        //    List<Order> os1 = os;
        //})

        //for (String oid : orderIds) { // x 10 次
        //    Order order = orderGet(oid); // 一次
        //    List<String> detailIds = detailIdsByOrder(order); // 一次
        //    List<OrderDetail> details = detailIds.map(Program::detailById); // 2 次
        //}
    }

    // 用一个 order id, 查一个 order
    private static Order orderGet(String orderId) {
        System.out.println("查询 order 表");
        return new Order();
    }

    private static List<String> detailIdsByOrder(Order order) {
        return null;
    }

    private static OrderDetail detailById(String detailId) {
        return new OrderDetail();
    }

    // 一个 order id 查一个 order
    private static Fetch<Order> orderFetch(String orderId) {
        Request<String, Order> request = new OrderById(orderId);
        return request.toFetch();
    }

    private static Fetch<OrderDetail> detailFetch(String detailId) {
        Request<String, OrderDetail> request = new OrderDetailById(detailId);
        return request.toFetch();
    }

    // 一个 order id 查多个 detail
    private Fetch<List<OrderDetail>> orderDetailsByOid(String oid) {
        return null;
    }
}
