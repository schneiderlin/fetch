package io.github.schneiderlin.fetch.example.gatewayDemo.request;

import io.github.schneiderlin.fetch.example.gatewayDemo.model.Address;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddressById implements Request<Long, Address> {
    public long addressId;


    @Override
    public Long getId() {
        return addressId;
    }
}
