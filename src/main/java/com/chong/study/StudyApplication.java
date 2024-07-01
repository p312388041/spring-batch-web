package com.chong.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@MapperScan({ "com.chong.study.mapper" })
@SpringBootApplication
@RestController
public class StudyApplication {

	@Autowired
	private ConfigurableApplicationContext context;

	public static void main(String[] args) {
		
		SpringApplication.run(StudyApplication.class, args);
	}
}
