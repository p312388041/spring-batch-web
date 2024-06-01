package com.chong.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chong.study.mapper.StudentMapper;

@Component
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public void clear() {
        studentMapper.clear();
    }

    public int count() {
        return studentMapper.count();
    }
}
