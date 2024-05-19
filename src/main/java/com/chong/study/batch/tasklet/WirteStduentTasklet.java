package com.chong.study.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.chong.study.factory.StudentFactory; 
import com.chong.study.pojo.Student;

public class WirteStduentTasklet implements Tasklet{
 
    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        Student student = StudentFactory.createStudent(); 
        return RepeatStatus.CONTINUABLE;
    }
}
