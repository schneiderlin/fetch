package io.github.schneiderlin.fetch.study.request;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public class VavrDemo {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> map = list.map(i -> i + 1);

        Map<Integer, String> m = HashMap.of(1, "one", 2, "two");
        Option<String> strings = m.get(3);
    }
}
