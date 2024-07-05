package com.chong.study.batch.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.chong.study.pojo.Student;

@StepScope
@Component
public class StudentItemProcesser implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student item) throws Exception {
        Student outStudent = new Student(item.getId(), item.getName(), item.getChinese(),
                item.getEnglish(), item.getMath());
        outStudent.setTotal(item.getChinese() + item.getEnglish() + item.getMath());
        // System.out.println("--dataId: " + item.getId() + "-----process-------" + Thread.currentThread().threadId()
        // + "--------------");
        return outStudent;
    }
}
