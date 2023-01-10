package io.github.schneiderlin.fetch.example.gatewayDemo.model;


import io.vavr.collection.List;
import lombok.Data;

@Data
public class OrderVO {
    private long orderId;

    @Data
    public static class Package {
        private long packageId;
        private Address address;
    }

    @Data
    public static class Address {
        private long addressId;
    }

    private List<Package> packages;

    //          order
    //        /         \
    //   package1      packge2    ... package n
    //       |           |             |
    //   address1      address2       address n
}
