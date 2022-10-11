package io.linzihao.fetch.example.gatewayDemo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.linzihao.fetch.BlockedRequest;
import io.linzihao.fetch.Fetch;
import io.linzihao.fetch.IO;
import io.linzihao.fetch.IORef;
import io.linzihao.fetch.example.gatewayDemo.model.Address;
import io.linzihao.fetch.example.gatewayDemo.model.Package;
import io.linzihao.fetch.example.gatewayDemo.model.Order;
import io.linzihao.fetch.example.gatewayDemo.model.OrderVO;
import io.linzihao.fetch.example.gatewayDemo.request.AddressById;
import io.linzihao.fetch.example.gatewayDemo.request.OrderById;
import io.linzihao.fetch.example.gatewayDemo.request.PackageById;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.Objects;
import static io.linzihao.fetch.Fetch.*;


public class Program {
    private static final Gson gson = new Gson();

    private static Fetch<OrderVO> program() {
        long oid = 1;
        // 开始构建 ast
        return orderById(oid)
                .flatMap(order -> {
                    List<Long> packageIds = order.getPackageIds();
                    Fetch<List<Package>> packagesFetch = Fetch.mapM(packageIds, Program::packageById);
                    Fetch<List<OrderVO.Package>> packageVOsFetch = packagesFetch.flatMap(packages -> {
                        return Fetch.mapM(packages, pkg -> {
                            long packageId = pkg.id;
                            long addressId = pkg.addressId;
                            Fetch<OrderVO.Address> addressFetch = addressById(addressId)
                                    .map(address -> {
                                        OrderVO.Address addressVO = new OrderVO.Address();
                                        addressVO.setAddressId(address.id);
                                        return addressVO;
                                    });
                            return addressFetch.map(addressVO -> {
                                OrderVO.Package packageVO = new OrderVO.Package();
                                packageVO.setAddress(addressVO);
                                packageVO.setPackageId(packageId);
                                return packageVO;
                            });
                        });
                    });

                    return packageVOsFetch.map(packageVOs -> {
                        OrderVO vo = new OrderVO();
                        vo.setOrderId(order.getId());
                        vo.setPackages(packageVOs);
                        return vo;
                    });
                });
    }

    public static void main(String[] args) {
        //Fetch<OrderVO> program = program();
        //
        //// 优化 ast 并执行
        //OrderVO vo = runFetch(Program::resolver, program).performIO();
        //System.out.println(vo);

        // basic building block, [orderById, packageById, addressById, resolver]
        long oid = 1;
        Fetch<Order> orderFetch = orderById(oid);

        // 在没有 performIO 之前, 不会执行任何查询
        //System.out.println(orderFetch);

        // 实际执行
        //Order order = runFetch(Program::resolver, orderFetch).performIO();
        //System.out.println(order);

        // Fetch 是个 functor, [Optional, List, Future] 这些也是 functor.
        // functor 可以 map
        // Optional<A>, (A -> B)    ->     Optional<B>
        // List<A>,     (A -> B)    ->     List<B>
        // Future<A>,   (A -> B)    ->     Future<B>
        // Fetch<A>,    (A -> B)    ->     Fetch<B>

        //Fetch<String> orderJsonFetch = orderById(oid)
        //        .map(o -> JSON.toJSONString(o));
        //String json = runFetch(Program::resolver, orderJsonFetch).performIO();
        //System.out.println(json);

        // Fetch 是个 monad, [Optional, List, Future] 这些也是 monad.
        // monad 可以 flatMap
        // Optional<A>, (A -> Optional<B>)    ->     Optional<B>
        // List<A>,     (A -> List<B>)        ->     List<B>
        // Future<A>,   (A -> Future<B>)      ->     Future<B>
        // Fetch<A>,    (A -> Fetch<B>)       ->     Fetch<B>

        // 读取 order, 再读取 order 中的第一个 package
        //Fetch<Package> packageFetch = orderFetch.flatMap(o -> {
        //    Long packageId1 = o.packageIds.get(0);
        //    return packageById(packageId1);
        //});
        //System.out.println(runFetch(Program::resolver, packageFetch).performIO());

        // 如果同时需要 package 和 order 的信息
        //Fetch<JSONObject> program = orderFetch.flatMap(o -> {
        //    Long packageId1 = o.packageIds.get(0);
        //    return packageById(packageId1).map(p -> {
        //        JSONObject jsonObject = new JSONObject();
        //        jsonObject.put("order", o);
        //        jsonObject.put("package", p);
        //        return jsonObject;
        //    });
        //});
        //System.out.println(runFetch(Program::resolver, program).performIO());

        // flatMap 可以无限嵌套
        //Fetch<Address> program = orderFetch.flatMap(o -> {
        //    long packageId1 = o.packageIds.get(0);
        //    return packageById(packageId1).flatMap(p -> {
        //        long addressId = p.addressId;
        //        return addressById(addressId);
        //    });
        //});
        //System.out.println(runFetch(Program::resolver, program).performIO());

        // 层数多了可以拆分开, pure program is easy to compose
        //Fetch<JSONObject> program = orderFetch.flatMap(o -> {
        //    long packageId1 = o.packageIds.get(0);
        //    return packageWithAddress(packageId1).map(jsonObject -> {
        //        jsonObject.put("order", o);
        //        return jsonObject;
        //    });
        //});
        //System.out.println(runFetch(Program::resolver, program).performIO());

        // Fetch 还是 applicative
        // List<A>, (A -> Fetch<B>)    ->    Fetch<List<B>>
        //Fetch<List<Package>> program = orderFetch.flatMap(o ->
        //        mapM(o.getPackageIds(), pid -> packageById(pid))
        //);
        //System.out.println(runFetch(Program::resolver, program).performIO());

        // again, program can compose
        // 有缩进表示有依赖关系, 同一级的可并行
        // order
        //   package1
        //     address
        //   package2
        //     address
        //   package3
        //     address
        //Fetch<List<JSONObject>> program = orderFetch.flatMap(o ->
        //        mapM(o.getPackageIds(), pid -> packageWithAddress(pid))
        //);
        //System.out.println(runFetch(Program::resolver, program).performIO());


    }

    private static Fetch<JsonObject> packageWithAddress(long packageId) {
        return packageById(packageId).flatMap(p -> {
            long addressId = p.addressId;
            return addressById(addressId).map(a -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("package", gson.toJsonTree(p));
                jsonObject.add("address", gson.toJsonTree(a));
                return jsonObject;
            });
        });
    }

    private static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        // batch 所有的同类请求
        List<Long> orderIds = blockedRequests.filter(request -> Objects.equals(request.request.getTag(), "OrderById"))
                .map(request -> {
                    String json = gson.toJson(request.request);
                    OrderById r = gson.fromJson(json, OrderById.class);
                    return r.orderId;
                });
        List<Long> packageIds = blockedRequests.filter(request -> Objects.equals(request.request.getTag(), "PackageById"))
                .map(request -> {
                    String json = gson.toJson(request.request);
                    PackageById r = gson.fromJson(json, PackageById.class);
                    return r.packageId;
                });
        List<Long> addressIds = blockedRequests.filter(request -> Objects.equals(request.request.getTag(), "AddressById"))
                .map(request -> {
                    String json = gson.toJson(request.request);
                    AddressById r = gson.fromJson(json, AddressById.class);
                    return r.addressId;
                });

        // 实际数据库查询
        Map<Long, Order> orderMap = Database.orderByIds(orderIds)
                .toMap(x -> new Tuple2<>(x.getId(), x));
        Map<Long, Package> packageMap = Database.packageByIds(packageIds)
                .toMap(x -> new Tuple2<>(x.id, x));
        Map<Long, Address> addressMap = Database.addressByIds(addressIds)
                .toMap(x -> new Tuple2<>(x.id, x));

        return IO
                .sequence(blockedRequests.map(request -> {
                    if (Objects.equals(request.request.getTag(), "OrderById")) {
                        String json = gson.toJson(request.request);
                        OrderById r = gson.fromJson(json, OrderById.class);
                        return IORef.writeIORef(request.result, orderMap.get(r.orderId).get());
                    } else if (Objects.equals(request.request.getTag(), "PackageById")) {
                        String json = gson.toJson(request.request);
                        PackageById r = gson.fromJson(json, PackageById.class);
                        return IORef.writeIORef(request.result, packageMap.get(r.packageId).get());
                    } else if (Objects.equals(request.request.getTag(), "AddressById")) {
                        String json = gson.toJson(request.request);
                        AddressById r = gson.fromJson(json, AddressById.class);
                        return IORef.writeIORef(request.result, addressMap.get(r.addressId).get());
                    }

                    throw new RuntimeException("no resolver");
                }))
                .andThen(IO.noop());
    }

    public static Fetch<Order> orderById(long oid) {
        return new OrderById(oid).toFetch();
    }

    public static Fetch<Package> packageById(long packageId) {
        return new PackageById(packageId).toFetch();
    }

    public static Fetch<Address> addressById(long addressId) {
        return new AddressById(addressId).toFetch();
    }
}
