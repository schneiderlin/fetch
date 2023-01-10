package io.github.schneiderlin.fetch.example.study.request;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilDemo {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> integerStream = list.stream()
                .map(i -> i + 1)
                .collect(Collectors.toList());
        System.out.println(integerStream);

        Map<Integer, String> m = Map.of(1, "one", 2, "two");
        String s = m.get(3);
        System.out.println(s);
    }
}
