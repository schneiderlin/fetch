package io.github.schneiderlin.fetch.gatewayDemo.request;

import io.github.schneiderlin.fetch.gatewayDemo.model.OrderDO;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderById implements Request<Long, OrderDO> {
    public long orderId;

    @Override
    public Long getId() {
        return orderId;
    }
}
