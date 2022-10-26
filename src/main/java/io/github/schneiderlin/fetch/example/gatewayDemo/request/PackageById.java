package io.github.schneiderlin.fetch.example.gatewayDemo.request;

import io.github.schneiderlin.fetch.example.gatewayDemo.model.Package;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageById implements Request<Long, Package> {
    public long packageId;

    @Override
    public String getTag() {
        return "PackageById";
    }

    @Override
    public Long getId() {
        return packageId;
    }
}
