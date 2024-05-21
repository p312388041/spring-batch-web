package com.chong.study.batch.configuration;

import javax.sql.DataSource;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.mapper.StudentMapper;

@Configuration
public class DeleteDbConfiguration {

    @Autowired
    private StudentMapper studentMapper;

    @Bean
    public Step deleteStduentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new StepBuilder("deleteStduentStep", jobRepository)
                .tasklet(deleteStudentTasklet(), transactionManager).build();
    }

    @Bean
    public Job deleteStduentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("deleteStduentJob", jobRepository)
                .start(deleteStduentStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public DataSourceTransactionManager batchTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Tasklet deleteStudentTasklet() {
        return (contribution, chunkContext) -> {
            if (studentMapper.count() > 0) {
                int maxId = studentMapper.max();
                studentMapper.delete(maxId);
                return RepeatStatus.CONTINUABLE;
            } else {
                return RepeatStatus.FINISHED;
            }
        };
    }
}