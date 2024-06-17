package com.chong.study;

import java.io.IOException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chong.study.pojo.Student;

@MapperScan({ "com.chong.study.mapper" })
@SpringBootApplication
@RestController
public class StudyApplication {

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {

		context = SpringApplication.run(StudyApplication.class, args);
	} 
}
