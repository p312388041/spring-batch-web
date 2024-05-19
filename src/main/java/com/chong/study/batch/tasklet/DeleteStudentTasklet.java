package com.chong.study.batch.tasklet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.chong.study.mapper.StudentMapper;

@MapperScan({ "com.chong.study.mapper" })
public class DeleteStudentTasklet implements Tasklet {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Long studentId = (Long) chunkContext.getStepContext().getStepExecution().getJobParameters().getLong(Constans.STUDENT_ID);
        // System.out.println("===============================" + studentId);
        studentMapper.delete(11);
        return RepeatStatus.FINISHED;
    }
}
