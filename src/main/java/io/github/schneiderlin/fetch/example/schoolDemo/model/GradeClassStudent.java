package io.github.schneiderlin.fetch.example.schoolDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeClassStudent {
    private Grade grade;
    private Clazz clazz;
    private Student student;
}
