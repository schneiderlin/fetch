package io.linzihao.fetch.example.gatewayDemo.request;

import io.linzihao.fetch.Request;
import io.linzihao.fetch.example.gatewayDemo.model.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderById implements Request<Long, Order> {
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
