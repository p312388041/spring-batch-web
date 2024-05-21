package com.chong.study.batch.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;

@Configuration
public class CsvToDbConfiguration {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        ItemProcessor<Student, Student> processor = new FunctionItemProcessor<Student, Student>(inStudent -> {
            Student outStudent = new Student(inStudent.getId(), inStudent.getName(), inStudent.getChinese(),
                    inStudent.getEnglish(), inStudent.getMath());
            outStudent.setTotal(inStudent.getChinese() + inStudent.getEnglish() + inStudent.getMath());
            return outStudent;
        });

        MyBatisBatchItemWriter<Student> writer = new MyBatisBatchItemWriterBuilder<Student>()
                .statementId(StudentMapper.class.getName() + ".insert")
                .sqlSessionFactory(sqlSessionFactory).build();

        return new StepBuilder("step", jobRepository)
                .<Student, Student>chunk(100, transactionManager)
                .reader(new StudentItemReader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(3)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, transactionManager))
                .build();
    }
}