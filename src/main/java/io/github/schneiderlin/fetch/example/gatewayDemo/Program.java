package io.github.schneiderlin.fetch.example.gatewayDemo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.schneiderlin.fetch.BlockedRequest;
import io.github.schneiderlin.fetch.example.gatewayDemo.model.Order;
import io.github.schneiderlin.fetch.example.gatewayDemo.model.Package;
import io.github.schneiderlin.fetch.example.gatewayDemo.request.OrderById;
import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.IORef;
import io.github.schneiderlin.fetch.example.gatewayDemo.model.Address;
import io.github.schneiderlin.fetch.example.gatewayDemo.model.OrderVO;
import io.github.schneiderlin.fetch.example.gatewayDemo.request.AddressById;
import io.github.schneiderlin.fetch.example.gatewayDemo.request.PackageById;
import io.github.schneiderlin.fetch.io.IO;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.Objects;

import static io.github.schneiderlin.fetch.Fetch.mapM;
import static io.github.schneiderlin.fetch.Fetch.runFetch;


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
        example1();
        //example2();
        //example3();
        //example4();
        //example5();
        //example6();
        //example7();
        //example8();
        //example9();
    }

    /**
     * 首先假设已经有 [orderById, packageById, addressById, resolver] 这几个函数
     * 如何使用这些函数
     */
    private static void example1() {
        long oid = 1;
        // 调用 orderById, 传入一个 order id, 返回的是一个 Order, 但是包在 Fetch 里面
        // Fetch 可以类比 Optional 或者 Future, 对里面的 Order 做了一个封装
        Fetch<Order> orderFetch = orderById(oid);

        // 单纯的一个 Fetch<T> 不会执行任何查询, 程序执行到这里会直接结束
        // fetch 的核心是把 "代码描述" 和 "实际执行" 分离开
        // Fetch<T> 就是如何查询 T 的一个 "描述", 没有实际执行
    }

    /**
     * 接上 example1 的代码, 如果拿到 Fetch<T> 里面的 T
     */
    private static void example2() {
        long oid = 1;
        Fetch<Order> orderFetch = orderById(oid);

        // 实际执行
        Order order = runFetch(Program::resolver, orderFetch).performIO();
        System.out.println(order);

        // runFetch 和 performIO 是 "实际执行" 的部分
        // 会把一个 "描述" 进行优化, 优化的过程不改变语义, 只影响性能. 优化后再执行
        // 在这个例子中, 没有可优化的空间, 就是单纯的根据 id 查 order.
    }

    /**
     * Fetch<A> 可以用 map, 提供一个 A -> B 的函数, 可以转换成 Fetch<B>
     * "map" 和后面会提到的其他组合函数 "flatMap", "mapM", "appCombine" 等等, 都是在一个"描述"的基础上, 追加更多"描述"的方式, 以此来组合出复杂的"描述"
     */
    private static void example3() {
        long oid = 1;

        // 提供了一个 Order -> String 的函数, 可以把 Fetch<Order> 转成 Fetch<String>
        Fetch<String> orderJsonFetch = orderById(oid).map(o -> gson.toJson(o));
        // 同样, 在没有执行 performIO() 之前, 不会执行任何查询
        System.out.println(orderJsonFetch);

        // 实际执行
        String json = runFetch(Program::resolver, orderJsonFetch).performIO();
        System.out.println(json);
        // 这时候同样也没有优化空间, 执行器会先查出 order, 然后执行用户提供的 gson.toJson 函数
    }

    /**
     * Fetch<A> 可以用 flatMap, 提供一个 A -> Fetch<B> 的函数, 可以转换成 Fetch<B>
     */
    private static void example4() {
        long oid = 1;

        // 读取 order, 再读取 order 中的第一个 package
        Fetch<Package> packageFetch = orderById(oid).flatMap(o -> {
            Long packageId1 = o.getPackageIds().get(0);
            Fetch<Package> fetchPackage = packageById(packageId1);
            return fetchPackage;
        });
        // flatMap 可以表达有依赖关系的查询, 必须先把 order 查出来, 才知道 package id. 才能继续下一步的查询

        System.out.println(runFetch(Program::resolver, packageFetch).performIO());
        // 这时候还是没有优化空间, 执行器会先查询 order, 拿出第一个 package id, 然后用这个 id 查询 package, 最后返回 package
    }

    /**
     * 假设我同时需要用到 order 和 package 的信息
     */
    private static void example5() {
        // 首先查询 order, 然后用 flatMap 找到关联的 package.
        Fetch<JsonObject> program = orderById(1).flatMap(o -> {
            Long packageId1 = o.getPackageIds().get(0);
            return packageById(packageId1).map(p -> {
                // 在这里面, 可以同时拿到 Order o 和 Package p. 可以做任意的业务逻辑
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("order", gson.toJsonTree(o));
                jsonObject.add("package", gson.toJsonTree(p));
                return jsonObject;
            });
        });
        // 实际执行
        System.out.println(runFetch(Program::resolver, program).performIO());
    }

    /**
     * flatMap 可以无限嵌套
     */
    private static void example6() {
        // 以下是嵌套 3 层的例子, 先查 Order, 然后查对应的 Package, 再查对应的 Address
        Fetch<Address> program = orderById(1).flatMap(o -> {
            long packageId1 = o.getPackageIds().get(0);
            return packageById(packageId1).flatMap(p -> {
                long addressId = p.addressId;
                return addressById(addressId).map(a -> {
                    // 在这一级, 可以同时拿到 Order o, Package p, Address a
                    // 可以做任意业务逻辑
                    return a;
                });
            });
        });
        // 实际执行
        System.out.println(runFetch(Program::resolver, program).performIO());
    }

    /**
     * 当 flatMap 的嵌套层数多了之后, 代码的可读性会下降.
     * 处理方法和 for/if 嵌套一样, 可以根据代码逻辑, 把嵌套的部分拆成独立的函数
     */
    private static void example7() {
        // 和 example6 是基本上是一样的, 不过把 package 和 address 的部分抽到了一个独立的函数 packageWithAddress 里面
        Fetch<JsonObject> program = orderById(1).flatMap(o -> {
            long packageId1 = o.getPackageIds().get(0);
            // packageWithAddress 是抽出去的部分
            return packageWithAddress(packageId1).map(jsonObject -> {
                jsonObject.add("order", gson.toJsonTree(o));
                return jsonObject;
            });
        });

        // 实际执行
        System.out.println(runFetch(Program::resolver, program).performIO());
    }

    /**
     * flatMap 用来描述有依赖关系的查询, mapM 可以用来描述没有依赖关系的查询
     * <p>
     * mapM 的函数签名是
     * (有很多个 A, 提供一个函数描述怎么从 A 得到 Fetch<B>), 返回 Fetch<List<B>>
     * List<A>, (A -> Fetch<B>)    ->    Fetch<List<B>>
     */
    private static void example8() {
        Fetch<List<Package>> program = orderById(1).flatMap(o ->
                // 有很多个 package id, 描述怎么从一个 package id 变成 Fetch<Package>, 返回 Fetch<List<Package>>
                mapM(o.getPackageIds(), pid -> packageById(pid))
        );

        // 实际执行
        System.out.println(runFetch(Program::resolver, program).performIO());
    }

    /**
     * 如果查询之间没有依赖关系, 那么优化器就有优化的空间了
     */
    private static void example9() {
        // 和 example8 几乎一样, packageById 的部分换成了 packageWithAddress
        Fetch<List<JsonObject>> program = orderById(1).flatMap(o ->
                mapM(o.getPackageIds(), pid -> packageWithAddress(pid))
        );

        // 实际执行
        System.out.println(runFetch(Program::resolver, program).performIO());
        // 注意 packageWithAddress 的逻辑, 描述的是 `一个` package id, 怎么查询对应的 address, 然后组成 json
        // 优化器能够发现这些 package id 之间是没有依赖关系的, 会自动把多个 package id 集合在一起, 批量做查询
        // 同样, 多个 address 之间也是没有依赖关系的, 优化器会自动把多个 address id 集合在一起批量查询

        // 把"描述"和"执行"区分开之后, "描述"的部分就可以专注于业务逻辑, 在复用代码的时候根本不需要考虑性能, 这样就会有最大程度的复用
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
