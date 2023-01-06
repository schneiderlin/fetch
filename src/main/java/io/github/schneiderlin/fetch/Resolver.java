package io.github.schneiderlin.fetch;

import io.github.schneiderlin.fetch.io.IO;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Resolver {
    public static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        Map<String, List<BlockedRequest<Object, Object>>> requests = blockedRequests.groupBy(r -> r.request.getTag());
        Seq<IO<?>> ios = requests.map(kv -> {
            String key = kv._1;
            List<BlockedRequest<Object, Object>> value = kv._2;

            try {
                Class clazz = Class.forName(key);
                System.out.println(clazz);
                List<Object> ids = value.map(request -> request.request.getId());

                Method[] declaredMethods = clazz.getDeclaredMethods();
                Method method = Arrays.stream(declaredMethods).filter(m -> m.getName().equals("batchQuery")).findAny().get();
                Map<Object, Object> map = (Map<Object, Object>) method.invoke(null, ids);
                return IO
                        .sequence(value.map(request ->
                                IORef.writeIORef(request.result, map.get(request.request.getId()).get())))
                        .andThen(IO.noop());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        return IO.parallel(ios.toList());
    }
}
