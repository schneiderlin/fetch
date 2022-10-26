package io.github.schneiderlin.fetch.cookbook;

import com.google.gson.Gson;
import io.github.schneiderlin.fetch.*;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AllArgsConstructor;

import java.util.Objects;

public class SameBase {
    private static Gson gson = new Gson();

    // p1
    // inter1   p2
    //     base
    // base 在同一个 resolve 批次里面, 两个的 id 是一样的, 所以只会查一次
    public static void main(String[] args) {
        Fetch<String> p1 = interFetch().flatMap(inter -> Fetch.unit(inter + "1"));
        Fetch<String> p2 = baseFetch().flatMap(base -> Fetch.unit(base + "2"));
        Fetch<String> program = Fetch.appCombine((b1, b2) -> b1 + b2, p1, p2);
        String result = Fetch.runFetch(SameBase::resolver, program).performIO();
        System.out.println(result);
    }

    private static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        // batch 所有的同类请求
        List<Long> getInfoPIds = blockedRequests
                .filter(request -> Objects.equals(request.request.getTag(), "BaseRequest"))
                .map(request -> {
                    String json = gson.toJson(request.request);
                    BaseRequest baseRequest = gson.fromJson(json, BaseRequest.class);
                    return baseRequest.id;
                });


        // 实际数据库查询
        Map<Long, String> baseMap = baseByIds(getInfoPIds)
                .toMap(info -> new Tuple2<>(1L, info));

        return IO
                .sequence(blockedRequests.map(request -> {
                    if (request.request.getClass() == BaseRequest.class) {
                        String json = gson.toJson(request.request);
                        BaseRequest baseRequest = gson.fromJson(json, BaseRequest.class);
                        return IORef.writeIORef(request.result, baseMap.get(baseRequest.id).get());
                    }

                    throw new RuntimeException("no resolver");
                }))
                .andThen(IO.noop());
    }

    private static List<String> baseByIds(List<Long> ids) {
        System.out.println("base by ids");
        return ids.map(String::valueOf);
    }

    private static Fetch<String> interFetch() {
        return baseFetch().flatMap(base -> Fetch.unit(base + "1"));
    }

    private static Fetch<String> baseFetch() {
        return new BaseRequest(1).toFetch();
    }

    @AllArgsConstructor
    private static class BaseRequest implements Request<Long, String> {

        private long id;

        @Override
        public String getTag() {
            return "BaseRequest";
        }

        @Override
        public Long getId() {
            return id;
        }
    }
}
