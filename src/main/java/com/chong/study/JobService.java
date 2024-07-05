package com.chong.study;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.batch.chunk.StudentItemProcesser;
import com.chong.study.batch.chunk.StudentItemReader;
import com.chong.study.batch.chunk.StudentItemWriter;
import com.chong.study.pojo.Student;

@Configuration
public class JobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    public List<Job> jobs() {
        StudentItemReader reader = (StudentItemReader) context.getBean("studentItemReader");
        StudentItemWriter writer = (StudentItemWriter) context.getBean("studentItemWriter");
        StudentItemProcesser processor = (StudentItemProcesser) context.getBean("studentItemProcesser");

        Step step = new StepBuilder("001step", jobRepository)
                .<Student, Student>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
        Job job = new JobBuilder("001" + "job", jobRepository)
                .start(step)
                .build();
        List<Job> list = new ArrayList<>(); 
        list.add(job);
        return list;
    }

    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }
}
