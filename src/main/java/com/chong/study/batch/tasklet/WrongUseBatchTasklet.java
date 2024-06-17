package com.chong.study.batch.tasklet;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.Constans;
import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;
import com.chong.study.util.StudentUtils;
import com.opencsv.CSVReader;

@Configuration
public class WrongUseBatchTasklet {

    @Autowired
    private StudentMapper studentMapper;

    @Bean
    public Job wrongUseBatchJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("wrongUseBatchJob", jobRepository)
                .start(wrongUseBatchTasklet(jobRepository, transactionManager))
                .build();
    }

    private Tasklet wrongUseBatchTasklet() {
        return (contribution, chunkContext) -> {
            String filePath = Constans.INPUT_FILE_PATH;
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath)));
            reader.forEach(item -> {
                Student student = StudentUtils.parseLineToStudent(item);
                studentMapper.insert(student);
            });
            reader.close();
            return RepeatStatus.FINISHED;
        };
    }

    private Step wrongUseBatchTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("wrongUseBatchTasklet", jobRepository)
                .tasklet(wrongUseBatchTasklet(), transactionManager).build();
    }
}