package io.github.schneiderlin.fetch.example.gatewayDemo.request;

import io.github.schneiderlin.fetch.example.gatewayDemo.model.OrderDO;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderById implements Request<Long, OrderDO> {
    public long orderId;

    @Override
    public Long getId() {
        return orderId;
    }

    @Override
    public String getTag() {
        return "OrderById";
    }
}
