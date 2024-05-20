package com.chong.study.batch.configuration;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.FileSystemResource;

import com.chong.study.Constans;
import com.chong.study.pojo.Student;

public class StudentItemReader extends FlatFileItemReader<Student> {
    private JobParameters jobParameters;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("----------------------------------start beforeStep");
        jobParameters = stepExecution.getJobParameters();
        System.out.println(jobParameters);
        setResource(new FileSystemResource(jobParameters.getString(Constans.FILE_PATH)));
        setLineMapper(new StudentLineMapper());
    }
}