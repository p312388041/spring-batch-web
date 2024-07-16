package com.chong.study.batch.chunk;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;

@StepScope
@Component
public class StudentItemWriter extends MyBatisBatchItemWriter<Student> {
    StudentItemWriter(@Autowired SqlSessionFactory sqlSessionFactory) {
        setStatementId(StudentMapper.class.getName() + ".insert");
        setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public void write(Chunk<? extends Student> items) {
        String idString = String.join(",",
                items.getItems().stream().map(student -> student.getId()).map(data -> data + "").toList());
        System.out
                .println("-----********id---list********" + idString + "****-------" + Thread.currentThread().threadId()
                        + "-------total: -------" + items.size());
        super.write(items);
    }
}
