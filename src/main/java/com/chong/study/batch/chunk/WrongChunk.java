package com.chong.study.batch.chunk;

import java.io.StringReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.Constans;
import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;
import com.chong.study.util.StudentUtils;
import com.opencsv.CSVReader;

@Configuration
public class WrongChunk {

    @Autowired
    private StudentMapper studentMapper;

    @Bean
    public Job wrongChunkJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("wrongChunkJob", jobRepository)
                .start(wrongChunkJobStep(jobRepository, transactionManager))
                .build();
    }

    private Step wrongChunkJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("wrongChunkJobStep", jobRepository)
                .<Student, Student>chunk(1000, transactionManager)
                .reader(new StudentItemReader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    private ItemProcessor<Student, Student> processor() {
        return (inStudent -> {
            Student outStudent = new Student(inStudent.getId(), inStudent.getName(), inStudent.getChinese(),
                    inStudent.getEnglish(), inStudent.getMath());
            outStudent.setTotal(inStudent.getChinese() + inStudent.getEnglish() + inStudent.getMath());
            return outStudent;
        });
    }

    private ItemWriter<Student> writer() {
        return chunk -> {
            chunk.getItems().forEach(item -> {
                studentMapper.insert(item);
            });
        };
    }

    private static class StudentItemReader extends FlatFileItemReader<Student> {

        @BeforeStep
        public void beforeStep(StepExecution stepExecution) {
            setResource(new FileSystemResource(Constans.INPUT_FILE_PATH));
            setLineMapper(studentLineMapper());
        }

        private LineMapper<Student> studentLineMapper() {
            return (line, lineNumber) -> {
                CSVReader csvReader = new CSVReader(new StringReader(line));
                String[] fileds = csvReader.readAll().get(0);
                csvReader.close();
                return StudentUtils.parseLineToStudent(fileds);
            };
        }
    }

    private TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}