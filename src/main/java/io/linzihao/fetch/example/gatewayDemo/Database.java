package io.linzihao.fetch.example.gatewayDemo;

import io.linzihao.fetch.example.gatewayDemo.model.Address;
import io.linzihao.fetch.example.gatewayDemo.model.Order;
import io.linzihao.fetch.example.gatewayDemo.model.Package;
import io.vavr.collection.List;


public class Database {
    private static List<Order> orderTable = List.of(
            Order.builder()
                    .id(1L)
                    .packageIds(List.of(2L, 3L, 4L))
                    .build()
    );

    private static List<Package> packageTable = List.of(
            Package.builder()
                    .id(2L)
                    .addressId(5L)
                    .build(),
            Package.builder()
                    .id(3L)
                    .addressId(6L)
                    .build(),
            Package.builder()
                    .id(4L)
                    .addressId(7L)
                    .build()
    );

    private static List<Address> addressTable = List.of(
            Address.builder()
                    .id(5L)
                    .build(),
            Address.builder()
                    .id(6L)
                    .build(),
            Address.builder()
                    .id(7L)
                    .build()
    );

    public static List<Order> orderByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("order by ids: " + ids);
        return orderTable.filter(x -> ids.contains(x.getId()));
    }

    public static List<Package> packageByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("package by ids: " + ids);
        return packageTable.filter(x -> ids.contains(x.id));
    }

    public static List<Address> addressByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("address by ids: " + ids);
        return addressTable.filter(x -> ids.contains(x.id));
    }
}
