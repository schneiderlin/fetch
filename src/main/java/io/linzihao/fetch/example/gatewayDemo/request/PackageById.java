package io.linzihao.fetch.example.gatewayDemo.request;

import io.linzihao.fetch.Request;
import lombok.AllArgsConstructor;
import io.linzihao.fetch.example.gatewayDemo.model.Package;

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
