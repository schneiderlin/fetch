package io.github.schneiderlin.fetch.example.gatewayDemo.model;


import io.vavr.collection.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Order {
    private long id;

    private List<Long> packageIds;
}
