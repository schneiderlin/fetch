package io.github.schneiderlin.fetch.schoolDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolGradeClassStudent {
    private School school;
    private Grade grade;
    private Clazz clazz;
    private Student student;
}
