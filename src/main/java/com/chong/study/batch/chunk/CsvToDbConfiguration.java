package com.chong.study.batch.chunk;

import java.io.IOException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.pojo.Student;

@Configuration
public class CsvToDbConfiguration { 
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private StudentItemReader reader = new StudentItemReader();

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, transactionManager))
                .build();
    }

    private Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
        return new StepBuilder("step", jobRepository)
                .<Student, Student>chunk(90, transactionManager)
                .reader(reader)
                .processor(new StudentItemProcesser())
                .writer(new StudentItemWriter(sqlSessionFactory))
                .taskExecutor(taskExecutor())
                .build();
    }

    private TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setVirtualThreads(false);
        return executor;
        // return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
    }
}