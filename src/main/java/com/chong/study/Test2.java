package com.chong.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MapperScan(basePackages = "com.chong.study.mapper")
@SpringBootApplication
@RestController
public class Test2 {
 
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Test2.class, args);
    }

    @RequestMapping(value = "/hello2")
    public String hello(String jobName) throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
 
        return "hello world";
    }
}
