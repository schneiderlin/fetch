package io.github.schneiderlin.fetch.example.study;

import io.github.schneiderlin.fetch.BlockedRequest;
import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.IORef;
import io.github.schneiderlin.fetch.Request;
import io.github.schneiderlin.fetch.example.study.model.Order;
import io.github.schneiderlin.fetch.example.study.model.OrderDetail;
import io.github.schneiderlin.fetch.example.study.request.OrderById;
import io.github.schneiderlin.fetch.io.IO;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Program {
    public static void main(String[] args) {
        // 查 10 个 order 出来
        // 每个 order, 查对应所有明细
        List<String> oids = List.of("1", "2", "3", "4", "5");
        Fetch<List<Order>> orders = Fetch.mapM(oids, Program::orderFetch);
        // Fetch 是一个箱子 []
        List<Order> os = Fetch.runFetch(Program::resolver, orders).performIO();
        // run fetch 之后
        // Fetch 变成 [order...]
        System.out.println(os);
    }

    // 参数 blockedRequests 就是任务池中所有累计的任务
    public static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        Map<String, List<BlockedRequest<Object, Object>>> requests = blockedRequests.groupBy(r -> r.request.getTag());
        Seq<IO<?>> ios = requests.map(kv -> {
            String key = kv._1;
            List<BlockedRequest<Object, Object>> value = kv._2;

            // 怎么拿到一个 batchQuery
            // 怎么根据 class name 获取 class
            try {
                Class clazz = Class.forName(key);
                System.out.println(clazz);
                List<Object> ids = value.map(request -> request.request.getId());

                Method[] declaredMethods = OrderById.class.getDeclaredMethods();
                Method method = Arrays.stream(declaredMethods).filter(m -> m.getName().equals("batchQuery")).findAny().get();
                Map<Object, Object> map = (Map<Object, Object>) method.invoke(null, ids);
                return IO
                        .sequence(value.map(request ->
                                IORef.writeIORef(request.result, map.get(request.request.getId()).get())))
                        .andThen(IO.noop());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        return IO.parallel(ios.toList());
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
