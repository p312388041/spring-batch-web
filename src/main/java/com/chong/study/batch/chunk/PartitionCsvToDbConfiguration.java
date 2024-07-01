package com.chong.study.batch.chunk;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.chong.study.Constans;
import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;
import com.chong.study.util.StudentUtils;
import com.opencsv.CSVReader;

@Configuration
public class PartitionCsvToDbConfiguration {

    private static String FORLDER_PATH = "C:\\Users\\31238\\OneDrive\\デスクトップ\\test_" + Constans.DATA_COUNT;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job partitionJob() throws IOException {
        return new JobBuilder("partitionJob", jobRepository)
                .start(step1Manager())
                .build();
    }

    private Step step() throws IOException {
        return new StepBuilder("step", jobRepository)
                .<Student, Student>chunk(10, transactionManager)
                .reader(itemReader(null))
                .processor(processor())
                .writer(writer())
                // .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step1Manager() throws IOException { 
        return new StepBuilder("step1.manager", jobRepository)
                .partitioner(step().getName(), new FileParttioner())
                .gridSize(Constans.PARTITION_SIZE)
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public PartitionHandler partitionHandler() throws IOException {
        TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
        retVal.setTaskExecutor(taskExecutor());
        retVal.setStep(step());
        retVal.setGridSize(Constans.PARTITION_SIZE);
        return retVal;
    }

    private ItemProcessor<Student, Student> processor() {
        return (inStudent -> {
            System.out.println("-------process-------" + Thread.currentThread().threadId() + "--------------");
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

    @Bean
    @StepScope
    public FlatFileItemReader<Student> itemReader(
            @Value("#{stepExecutionContext['fileName']}") Resource resource) {
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<Student>();
        itemReader.setLineMapper(studentLineMapper());
        itemReader.setResource(resource);
        return itemReader;
    }

    private LineMapper<Student> studentLineMapper() {
        return (line, lineNumber) -> {
            CSVReader csvReader = new CSVReader(new StringReader(line));
            String[] fileds = csvReader.readAll().get(0);
            csvReader.close();
            System.out.println("-------reader-------" + Thread.currentThread().threadId() + "--------------");
            return StudentUtils.parseLineToStudent(fileds);
        };
    }

    private static class FileParttioner implements Partitioner {

        @Override
        public Map<String, ExecutionContext> partition(int gridSize) {
            Map<String, ExecutionContext> result = new HashMap<>(gridSize);
            for (int i = 0; i < gridSize; i++) {
                ExecutionContext value = new ExecutionContext();
                String filePath = FORLDER_PATH + "/" + i + ".csv";
                String formString = "";
                try {
                    formString = new FileSystemResource(filePath).getURL().toExternalForm();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
                value.putString("fileName", formString);
                result.put("step:partition" + i, value);
            }
            return result;
        }
    }

    private TaskExecutor taskExecutor() {
        // return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(Constans.PARTITION_SIZE));
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        // taskExecutor.setVirtualThreads(true);
        return taskExecutor;
    }
}