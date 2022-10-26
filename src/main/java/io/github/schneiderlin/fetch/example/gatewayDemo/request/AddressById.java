package io.github.schneiderlin.fetch.example.gatewayDemo.request;

import io.github.schneiderlin.fetch.example.gatewayDemo.model.Address;
import io.github.schneiderlin.fetch.Request;
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
