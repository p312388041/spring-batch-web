package com.chong.study;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.xml.StepParserStepFactoryBean;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MapperScan(basePackages = "com.chong.study.mapper")
@SpringBootApplication
@RestController
@Configuration
public class StudyApplication { 
    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StudyApplication.class, args);
    }

    @RequestMapping(value = "/hello")
    public String hello(String jobName) throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        String names[] = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println("----------" + name);
        }
        Job job = null;
        List<Job> jobs = ((List<Job>) context.getBean("jobs")).stream().filter(j -> j.getName().equals(jobName))
                .toList();
        if (jobs.size() > 0) {
            job = jobs.get(0);
        } else {
            job = (Job) context.getBean(jobName);
        }
        StepParserStepFactoryBean bean = new StepParserStepFactoryBean<>();

        System.out.println("-------------" + job);
        // jobService.getJobLauncher().run(job,
        // new JobParametersBuilder().addLong("unique",
        // System.currentTimeMillis()).toJobParameters());
        return "hello world";
    } 
}
