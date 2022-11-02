package io.github.schneiderlin.fetch.example.schoolDemo;

import io.github.schneiderlin.fetch.BlockedRequest;
import io.github.schneiderlin.fetch.IORef;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Clazz;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Grade;
import io.github.schneiderlin.fetch.example.schoolDemo.model.School;
import io.github.schneiderlin.fetch.example.schoolDemo.model.Student;
import io.github.schneiderlin.fetch.io.IO;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

import java.util.Objects;

public class Database {
    private static List<School> schoolTable = List.of(
            School.builder()
                    .id("school1")
                    .name("school1")
                    .gradeIds(List.of("grade1", "grade2", "grade3"))
                    .build()
    );

    private static List<Grade> gradeTable = List.of(
            Grade.builder()
                    .id("grade1")
                    .name("grade1")
                    .classes(List.of("class1", "class2", "class3"))
                    .build(),
            Grade.builder()
                    .id("grade2")
                    .name("grade2")
                    .classes(List.of("class4", "class5", "class6"))
                    .build(),
            Grade.builder()
                    .id("grade3")
                    .name("grade3")
                    .classes(List.of("class7", "class8", "class9"))
                    .build()
    );

    private static List<Clazz> classTable = List.of(
            Clazz.builder()
                    .id("class1")
                    .name("class1")
                    .students(List.of("student1", "student2", "student3"))
                    .build(),
            Clazz.builder()
                    .id("class2")
                    .name("class2")
                    .students(List.of("student4", "student5", "student6"))
                    .build(),
            Clazz.builder()
                    .id("class3")
                    .name("class3")
                    .students(List.of("student7", "student8", "student9"))
                    .build(),
            Clazz.builder()
                    .id("class4")
                    .name("class4")
                    .students(List.of("student10", "student11", "student12"))
                    .build(),
            Clazz.builder()
                    .id("class5")
                    .name("class5")
                    .students(List.of("student13", "student14", "student15"))
                    .build(),
            Clazz.builder()
                    .id("class6")
                    .name("class6")
                    .students(List.of("student16", "student17", "student18"))
                    .build(),
            Clazz.builder()
                    .id("class7")
                    .name("class7")
                    .students(List.of("student19", "student20", "student21"))
                    .build(),
            Clazz.builder()
                    .id("class8")
                    .name("class8")
                    .students(List.of("student22", "student23", "student24"))
                    .build(),
            Clazz.builder()
                    .id("class9")
                    .name("class9")
                    .students(List.of("student25", "student26", "student27"))
                    .build()
    );

    private static List<Student> studentTable = List.of(
            Student.builder()
                    .id("student1")
                    .name("student1")
                    .build(),
            Student.builder()
                    .id("student2")
                    .name("student2")
                    .build(),
            Student.builder()
                    .id("student3")
                    .name("student3")
                    .build(),
            Student.builder()
                    .id("student4")
                    .name("student4")
                    .build(),
            Student.builder()
                    .id("student5")
                    .name("student5")
                    .build(),
            Student.builder()
                    .id("student6")
                    .name("student6")
                    .build(),
            Student.builder()
                    .id("student7")
                    .name("student7")
                    .build(),
            Student.builder()
                    .id("student8")
                    .name("student8")
                    .build(),
            Student.builder()
                    .id("student9")
                    .name("student9")
                    .build(),
            Student.builder()
                    .id("student10")
                    .name("student10")
                    .build(),
            Student.builder()
                    .id("student11")
                    .name("student11")
                    .build(),
            Student.builder()
                    .id("student12")
                    .name("student12")
                    .build(),
            Student.builder()
                    .id("student13")
                    .name("student13")
                    .build(),
            Student.builder()
                    .id("student14")
                    .name("student14")
                    .build(),
            Student.builder()
                    .id("student15")
                    .name("student15")
                    .build(),
            Student.builder()
                    .id("student16")
                    .name("student16")
                    .build(),
            Student.builder()
                    .id("student17")
                    .name("student17")
                    .build(),
            Student.builder()
                    .id("student18")
                    .name("student18")
                    .build(),
            Student.builder()
                    .id("student19")
                    .name("student19")
                    .build(),
            Student.builder()
                    .id("student20")
                    .name("student20")
                    .build(),
            Student.builder()
                    .id("student21")
                    .name("student21")
                    .build(),
            Student.builder()
                    .id("student22")
                    .name("student22")
                    .build(),
            Student.builder()
                    .id("student23")
                    .name("student23")
                    .build(),
            Student.builder()
                    .id("student24")
                    .name("student24")
                    .build(),
            Student.builder()
                    .id("student25")
                    .name("student25")
                    .build(),
            Student.builder()
                    .id("student26")
                    .name("student26")
                    .build(),
            Student.builder()
                    .id("student27")
                    .name("student27")
                    .build()
    );

    public static School schoolById(String id) {
        System.out.println("school by id: " + id);
        return schoolTable.filter(x -> Objects.equals(x.getId(), id)).get(0);
    }

    public static List<School> schoolByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("school by ids: " + ids);
        return schoolTable.filter(x -> ids.contains(x.getId()));
    }

    public static Grade gradeById(String id) {
        System.out.println("grade by id: " + id);
        return gradeTable.filter(x -> Objects.equals(x.getId(), id)).get(0);
    }

    public static List<Grade> gradeByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("grade by ids: " + ids);
        return gradeTable.filter(x -> ids.contains(x.getId()));
    }

    public static Clazz classById(String id) {
        System.out.println("class by id: " + id);
        return classTable.filter(x -> Objects.equals(x.getId(), id)).get(0);
    }

    public static List<Clazz> classByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("class by ids: " + ids);
        return classTable.filter(x -> ids.contains(x.getId()));
    }

    public static Student studentById(String id) {
        System.out.println("student by id: " + id);
        return studentTable.filter(x -> Objects.equals(x.getId(), id)).get(0);
    }

    public static List<Student> studentByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        System.out.println("student by ids: " + ids);
        return studentTable.filter(x -> ids.contains(x.getId()));
    }

    public static IO<Void> resolver1(List<BlockedRequest<String, Grade>> gradeRequests) {
        List<String> gradeIds = gradeRequests
                .map(request -> request.request.getId());

        Map<String, Grade> gradeMap = gradeByIds(gradeIds)
                .toMap(info -> new Tuple2<>(info.getId(), info));

        return IO
                .sequence(gradeRequests.map(request ->
                        IORef.writeIORef(request.result, gradeMap.get(request.request.getId()).get())))
                .andThen(IO.noop());
    }

    public static IO<Void> resolver2(List<BlockedRequest<String, Clazz>> requests) {
        List<String> classIds = requests
                .map(request -> request.request.getId());

        Map<String, Clazz> classMap = classByIds(classIds)
                .toMap(info -> new Tuple2<>(info.getId(), info));

        return IO
                .sequence(requests.map(request ->
                        IORef.writeIORef(request.result, classMap.get(request.request.getId()).get())))
                .andThen(IO.noop());
    }

    public static IO<Void> resolver3(List<BlockedRequest<String, Student>> requests) {
        List<String> studentIds = requests
                .map(request -> request.request.getId());

        Map<String, Student> studentMap = studentByIds(studentIds)
                .toMap(info -> new Tuple2<>(info.getId(), info));

        return IO
                .sequence(requests.map(request ->
                        IORef.writeIORef(request.result, studentMap.get(request.request.getId()).get())))
                .andThen(IO.noop());
    }

    public static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        Map<String, List<BlockedRequest<Object, Object>>> requests = blockedRequests.groupBy(r -> r.request.getTag());
        Seq<IO<?>> ios = requests.map(kv -> {
            String key = kv._1;
            List<BlockedRequest<Object, Object>> value = kv._2;
            if (Objects.equals(key, "GradeById")) {
                return resolver1((List<BlockedRequest<String, Grade>>) (Object) value);
            }
            if (Objects.equals(key, "ClassById")) {
                return resolver2((List<BlockedRequest<String, Clazz>>) (Object) value);
            }
            if (Objects.equals(key, "StudentById")) {
                return resolver3((List<BlockedRequest<String, Student>>) (Object) value);
            }
            throw new RuntimeException("no resolver");
        });
        return IO.parallel(ios.toList());
    }
}
