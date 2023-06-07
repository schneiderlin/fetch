package io.github.schneiderlin.fetch;

import io.github.schneiderlin.fetch.io.IO;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Resolver {
    public static Function1<List<BlockedRequest<Object, Object>>, IO<Void>> makeResolver(FetchContext fetchContext) {
        return blockedRequests -> {
            Map<String, List<BlockedRequest<Object, Object>>> requests = blockedRequests.groupBy(r -> r.request.getClass().getCanonicalName());
            Seq<IO<?>> ios = requests.map(kv -> {
                String key = kv._1;
                List<BlockedRequest<Object, Object>> value = kv._2;

                try {
                    Class clazz = Class.forName(key);
                    List<Object> ids = value.map(request -> request.request.getId()).distinct();


                    Method[] declaredMethods = clazz.getDeclaredMethods();
                    Method method = Arrays.stream(declaredMethods).filter(m -> m.getName().equals("batchQuery")).findAny().get();
                    Class<?> idsClassType = method.getParameterTypes()[1];

                    Map<Object, Object> map;
                    if (idsClassType == List.class) {
                        map = (Map<Object, Object>) method.invoke(null, fetchContext, ids);
                    } else {
                        map = (Map<Object, Object>) method.invoke(null, fetchContext, ids.asJava());
                    }

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
        };
    }

    public static void main(String[] args) {
        Method method = Resolver.class.getMethods()[2];
        Class<?> parameterType = method.getParameterTypes()[0];
        System.out.println(parameterType == List.class);
    }

    public void foo(java.util.List<Integer> l) {

    }
}
