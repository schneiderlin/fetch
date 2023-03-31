package io.github.schneiderlin.fetch.gatewayDemo.request;

import io.github.schneiderlin.fetch.gatewayDemo.model.PackageDO;
import io.github.schneiderlin.fetch.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageById implements Request<Long, PackageDO> {
    public long packageId;

    @Override
    public Long getId() {
        return packageId;
    }
}
