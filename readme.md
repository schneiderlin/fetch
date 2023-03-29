# Fetch

致力于提供一个简单易用的数据查询框架，让使用者可以按照数据库实体对象之间的关系进行查询，让你从繁琐的数据查询中解放出来，写出优雅的代码。

## Project structure

- [example](./example) 一些简单的 Demo，展示 Fetch 的基本用法
- [cookbook](./cookbook) 项目常见用法的示例，帮助你快速上手 Fetch
- [core](./core) 项目的核心代码，如果你好奇 Fetch 的实现原理，可以看看这里

## What can it do?


假定有一个订单表 Order，一个订单明细表 OrderDetail，一个商品表 Product，需要查询 id 为 `[1,100]` 的订单时，如果使用最自然的方式，应该是 for 循环遍历 `idList` 的每一个 id，然后查询并组装 VO：

~~~java
public List<OrderVO> queryOrderByIds(List<Long> idList) {
    return idList.stream().map(id -> {
            var orderDO = queryOrderById(id);
            var orderDetailListDO = queryOrderDetailByOrderId(id);
            var orderDetailVOList = orderDetailListDO.stream().map(orderDetailDO -> {
                var productDO = queryProductById(orderDetailDO.getProductId());
                return new OrderDetailVO(orderDetailDO, productDO);
            });
        })
    return new OrderVO(orderDO, orderDetailVOList);
}
~~~

这样的查询是最自然的，因为它本质上就是按照数据库的实体关系来查询，但是随之而来的问题的性能羸弱，就以上述代码的查询为例：

- 查询 order 表 N 次
- 查询 orderDetail 表 N 次
- 查询 Product N*M 次

这仅仅是三层关系的查询，如果是多层关系，那么查询的次数就会更多，也就是所谓的 N+1 问题

在一般的业务代码中，我们通常需要这样查询:

~~~java
public List<OrderVO> queryOrderByIds(List<Long> idList) {
    var orderDOList = queryOrderByIdList(idList);
    
    var orderDetailIdList = idList.stream().map(OrderDO::getId).collect(Collectors.toList());
    var orderDetailDOList = queryOrderDetailByIdList(orderDetailIdList);
    var orderId2OrderDetailDOList = orderDetailDOList.stream().collect(
            Collectors.toMap(OrderDetailDO::getOrderId, Function.identity(), (l, r) -> l));

    var productIdList = orderDetailDOList.stream().map(OrderDetailDO::getProductId).collect(Collectors.toList());
    var productDOList = queryProductByIdList(productIdList);
    var productId2DO = productDOList.stream().collect(
        Collectors.toMap(ProductDO::getId, Function.identity(), (l, r) -> l));

    return orderDOList.stream().map(orderDO -> {
        return new OrderVO(orderDO, orderId2OrderDetailDOList.get(orderDO.getId()), productId2DO.get(orderDO.getId()));
    });
}
~~~

我们使用上述方式编写代码的原因是，我们希望尽可能少地查询数据库，因此需要将数据拍扁后进行查询，再组装成我们需要的数据结构，这样的代码编写起来不直观且十分繁琐，需要频繁的提取 id、组装 Map，不易维护。

使用 Fetch，你可以用最自然的方式查询数据库，而无需担心数据库的性能损耗，在 [example](./example) 模块中有很多 Demo，你可以尽情翻阅

## About Us

一些默默无闻的软件工程师，无足挂齿

如果你对项目有任何的意见和建议，欢迎提 issue 或者 PR，我们会尽快处理