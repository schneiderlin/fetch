package io.linzihao.fetch.example.gatewayDemo.request;

import io.linzihao.fetch.Request;
import io.linzihao.fetch.example.gatewayDemo.model.Address;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddressById implements Request<Long, Address> {
    public long addressId;

    @Override
    public String getTag() {
        return "AddressById";
    }

    @Override
    public Long getId() {
        return addressId;
    }
}
