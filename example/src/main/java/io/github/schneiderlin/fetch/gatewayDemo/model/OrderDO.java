package io.github.schneiderlin.fetch.gatewayDemo.model;


import io.vavr.collection.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDO {
    private long id;

    private List<Long> packageIds;
}
