package com.chong.study.mapper;

import java.util.List;
import com.chong.study.pojo.Student;

public interface StudentMapper {
    List<Student> list();
    void insert(Student student);
    void delete(int id);
    void update(Student student);
    Student get(int id);
    int max();
    int count();
}
