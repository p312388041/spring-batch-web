package com.chong.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@MapperScan({ "com.chong.study.mapper" })
@SpringBootApplication
// @RestController
public class StudyApplication  {
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
		SpringApplication.run(StudyApplication.class, args);
	} 
}
