package io.github.schneiderlin.fetch.schoolDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassStudent {
    private Clazz clazz;
    private Student student;
}
