package io.github.schneiderlin.fetch.example.schoolDemo;


import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.example.schoolDemo.model.*;
import io.github.schneiderlin.fetch.example.schoolDemo.request.ClassById;
import io.github.schneiderlin.fetch.example.schoolDemo.request.GradeById;
import io.github.schneiderlin.fetch.example.schoolDemo.request.StudentById;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.function.Function;

import static io.github.schneiderlin.fetch.Fetch.concatM;
import static io.github.schneiderlin.fetch.Fetch.mapM;

public class Program {
    public static void main(String[] args) {
        //getGradeClassStudent_slow("grade1");
        getGradeClassStudent_great("grade1");
    }

    /**
     * 根据年级 id, 查出里面所有班级, 所有学生
     */
    private static List<GradeClassStudent> getGradeClassStudent(String gradeId) {
        // 第一次数据库查询
        Grade grade = Database.gradeById(gradeId);
        List<String> classIds = grade.getClasses();

        // 第二次数据库查询
        List<Clazz> clazzes = Database.classByIds(classIds);
        Map<String, Clazz> clazzMap = clazzes.toMap(Clazz::getId, Function.identity());
        List<String> studentIds = clazzes.flatMap(Clazz::getStudents);

        // 第三次数据库查询
        List<Student> students = Database.studentByIds(studentIds);
        Map<String, Student> studentMap = students.toMap(Student::getId, Function.identity());

        return classIds.flatMap(classId -> {
            Clazz clazz = clazzMap.get(classId).get();
            return clazz.getStudents().map(studentId -> {
                Student student = studentMap.get(studentId).get();
                return new GradeClassStudent(grade, clazz, student);
            });
        });
    }

    /**
     * 根据年级 id, 查出里面所有班级, 所有学生
     */
    private static List<GradeClassStudent> getGradeClassStudent_slow(String gradeId) {
        // 第一次数据库查询
        Grade grade = Database.gradeById(gradeId);
        List<String> classIds = grade.getClasses();

        // getClassStudent 里面包含 2 次数据库查询
        // 每一个 classId 执行一次 getClassStudent, 一共是 length(classIds) * 2 次查询
        return classIds.flatMap(classId -> {
            List<ClassStudent> classStudents = getClassStudent(classId);
            return classStudents.map(classStudent -> new GradeClassStudent(grade, classStudent.getClazz(), classStudent.getStudent()));
        });
    }

    private static List<GradeClassStudent> getGradeClassStudent_great(String gradeId) {
        Fetch<List<GradeClassStudent>> program = new GradeById(gradeId).toFetch()
                .flatMap(grade -> concatM(grade.getClasses(), classId -> new ClassById(classId).toFetch()
                        .flatMap(clazz -> mapM(clazz.getStudents(), studentId -> new StudentById(studentId).toFetch()
                                .map(student -> new GradeClassStudent(grade, clazz, student))))));

        return Fetch.runFetch(Database::resolver, program).performIO();
    }

    /**
     * 根据班级 id, 查出里面的所有学生
     */
    private static List<ClassStudent> getClassStudent(String classId) {
        // 第一次数据库查询
        Clazz clazz = Database.classById(classId);

        // 第二次数据库查询
        List<Student> students = Database.studentByIds(clazz.getStudents());
        return students.map(student -> new ClassStudent(clazz, student));
    }

}
