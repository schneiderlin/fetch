package io.github.schneiderlin.fetch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Done<A> implements Result<A> {
    A a;
}
