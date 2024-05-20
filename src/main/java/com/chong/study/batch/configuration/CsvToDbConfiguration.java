package com.chong.study.batch.configuration;

import java.io.StringReader;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.Constans;
import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;
import com.opencsv.CSVReader;

@Configuration
public class CsvToDbConfiguration {

    private JobParameters jobParameters;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
        System.out.println(jobParameters);
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        String filePath = jobParameters.getString(Constans.FILE_PATH);

        FlatFileItemReader<Student> reader = new FlatFileItemReaderBuilder<Student>()
                .resource(new FileSystemResource(filePath))
                .lineMapper(new StudentLineMapper())
                .build();

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
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, transactionManager))
                .build();
    }

    private static class StudentLineMapper implements LineMapper<Student> {
        @Override
        public Student mapLine(String line, int lineNumber) throws Exception {
            CSVReader csvReader = new CSVReader(new StringReader(line));
            String[] fileds = csvReader.readAll().get(0);
            csvReader.close();
            return new Student(Integer.parseInt(fileds[0]), fileds[1], Long.parseLong(fileds[2]),
                    Long.parseLong(fileds[3]), Long.parseLong(fileds[4]), Long.parseLong(fileds[5]));
        }
    }
}
