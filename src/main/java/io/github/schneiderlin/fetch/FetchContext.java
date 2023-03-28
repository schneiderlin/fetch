package io.github.schneiderlin.fetch;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public final class FetchContext {
    private Map<String, Object> beans;

    private FetchContext(Map<String, Object> beans) {
        this.beans = beans;
    }

    public static FetchContext create() {
        return new FetchContext(HashMap.empty());
    }
    public static FetchContext empty() {
        return new FetchContext(HashMap.empty());
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
