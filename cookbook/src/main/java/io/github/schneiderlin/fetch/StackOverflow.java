package io.github.schneiderlin.fetch;


import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.io.IO;
import io.github.schneiderlin.fetch.io.IODone;
import io.vavr.collection.List;

public class StackOverflow {
    public static void main(String[] args) {
        //myIO();
        io();
        // 问题的根源是, 有一个很长的 callstack
        // f1(f2(f3(f4(f5(f...(x))))))
        // performIO 是递归到最里面, 那 f...(x) 算完了, 然后再算 f5.
        // 就需要把上面的 1-5 的信息都存在 stack 上面
    }

    private static void io() {
        List<Integer> range = List.range(0, 100960);
        Fetch<List<Integer>> listFetch = Fetch.mapM(range, Fetch::unit);
        IO<List<Integer>> program = Fetch.runFetch(null, listFetch);
        List<Integer> range1 = program.performIO();
        System.out.println(range1);
    }

    private static void myIO() {
        List<Integer> range = List.range(0, 40000);
        IO<List<Integer>> empty = new IODone<>(List.empty());

        // 初始化一个 [], 每循环一次, 往 list 的末尾追加一个 index
        // 最终的结果应该是 [0, 1, 2]
        IO<List<Integer>> program = range.foldLeft(
                empty,
                ((listIO, integer) -> listIO.flatMap(list -> new IODone<>(list.append(integer)))));
        List<Integer> range1 = program.performIO();
        System.out.println(range1);
    }


}
