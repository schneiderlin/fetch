package io.github.schneiderlin.fetch.example.schoolDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeClassStudent {
    // 一个年级里面所有班级, 班级里面所有学生
    // List<GradeClassStudent>
    //             grade
    //       class1   ....   class2
    //    student ...        ...  student

    // 如果用 3 层 for 循环写, 很多次 io
    private Grade grade;
    private Clazz clazz;
    private Student student;
}
