package com.chong.study.util;

import com.chong.study.pojo.Student;

public class StudentUtils {

    public static Student parseLineToStudent(String[] line) {
        int id = Integer.parseInt(line[0]);
        String name = line[1];
        float chinese = Float.parseFloat(line[2]);
        float english = Float.parseFloat(line[3]);
        float math = Float.parseFloat(line[4]);
        return new Student(id, name, chinese, english, math);
    }
}
