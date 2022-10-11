package io.linzihao.fetch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Done<A> implements Result<A> {
    A a;
}
