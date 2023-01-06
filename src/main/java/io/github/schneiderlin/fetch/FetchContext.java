package io.github.schneiderlin.fetch;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class FetchContext {
    private Map<String, Object> beans;

    public static FetchContext empty() {
        FetchContext fetchContext = new FetchContext();
        fetchContext.beans = HashMap.empty();
        return fetchContext;
    }

    public FetchContext addBean(String name, Object bean) {
        beans = beans.put(name, bean);
        return this;
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return (T) beans.get(name)
                .filter(clazz::isInstance)
                .get();
    }
}
