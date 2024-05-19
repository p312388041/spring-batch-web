package com.chong.study.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.chong.study.batch.tasklet.DeleteStudentTasklet;

@Configuration
public class DeleteStudentConfiguration {
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new DeleteStudentTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job DeleteStudentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("deleteStudentJob", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .build();
    }
}
