package com.chong.study.batch.chunk;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
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
import com.opencsv.CSVWriter;

@Configuration
public class CsvToDbConfiguration {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, transactionManager))
                .build();
    }

    private Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
        return new StepBuilder("step", jobRepository)
                .<Student, Student>chunk(10000, transactionManager)
                .reader(new StudentItemReader())
                .processor(processor())
                .writer(writer())
                // .faultTolerant()
                // .skip(Exception.class)
                // .skipLimit(10)
                // .listener(skipListener())
                .taskExecutor(taskExecutor())
                .build();
    }

    // public Step step1Manager(JobRepository jobRepository,
    // PlatformTransactionManager transactionManager) {
    // return new StepBuilder("step1.manager", jobRepository)
    // .partitioner("step", new SimplePartitioner())
    // .step(step(jobRepository, transactionManager))
    // .gridSize(11)
    // // .taskExecutor(taskExecutor())
    // .build();
    // }

    private ItemProcessor<Student, Student> processor() {
        return (inStudent -> {
            Student outStudent = new Student(inStudent.getId(), inStudent.getName(), inStudent.getChinese(),
                    inStudent.getEnglish(), inStudent.getMath());
            outStudent.setTotal(inStudent.getChinese() + inStudent.getEnglish() + inStudent.getMath());
            return outStudent;
        });
    }

    private MyBatisBatchItemWriter<Student> writer() {
        return new MyBatisBatchItemWriterBuilder<Student>()
                .statementId(StudentMapper.class.getName() + ".insert")
                .sqlSessionFactory(sqlSessionFactory).build();
    }

    private static class StudentItemReader extends FlatFileItemReader<Student> {

        @BeforeStep
        public void beforeStep(StepExecution stepExecution) {
            setResource(new FileSystemResource(Constans.INPUT_FILE_PATH));
            setLineMapper(studentLineMapper());
        }

        private LineMapper<Student> studentLineMapper() {
            return (line, lineNumber) -> {

                String[] fileds = line.split(",");
                return StudentUtils.parseLineToStudent(fileds);
            };
        }
    }

    private SkipListener skipListener() throws IOException {
        return new SkipListener<Student, Student>() {
            private AtomicInteger index = new AtomicInteger(0);
            private CSVWriter writer = new CSVWriter(new FileWriter(Constans.INPUT_FILE_PATH + "_error"));

            @Override
            public void onSkipInProcess(Student item, Throwable t) {
                System.out.println("--------------process");
                SkipListener.super.onSkipInProcess(item, t);
                writer.writeNext(new String[] { item.toString() });
                try {
                    writer.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSkipInRead(Throwable t) {
                System.out.println("--------------read" + index.getAndIncrement());
                FlatFileParseException except = (FlatFileParseException) t;
                writer.writeNext(new String[] { except.getInput() });
                try {
                    writer.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                SkipListener.super.onSkipInRead(t);

            }

            @Override
            public void onSkipInWrite(Student item, Throwable t) {
                writer.writeNext(new String[] { item.toString() });
                System.out.println("--------------write");
                try {
                    writer.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // TODO Auto-generated method stub
                SkipListener.super.onSkipInWrite(item, t);
            }

        };
    }

    private TaskExecutor taskExecutor() {
        // return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(48));
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

        // taskExecutor.setVirtualThreads(true);
        return taskExecutor;
    }
}