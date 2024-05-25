package com.chong.study.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.chong.study.mapper.StudentMapper;

public class WrongExample implements Tasklet {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for (int i = 0; i < 100; i++) {
            studentMapper.insert(null);
        }
        return null;
    }

}
