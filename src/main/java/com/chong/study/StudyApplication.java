package com.chong.study;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@MapperScan({ "com.chong.study.mapper" })
@SpringBootApplication
@RestController
public class StudyApplication implements ApplicationRunner {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job deleteStudentJob;
 
	// @Autowired
	// private DataSource dataSource;
 
	public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

	@RequestMapping("/")
	public String hello() {
		return "hello world";
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//         Map<String, Object> parameters = new HashMap<>();
        // parameters.put(Constans.STUDENT_ID, 11);
        // JobParameters jobParameters = new JobParametersBuilder().addLong(Constans.STUDENT_ID, 11L).toJobParameters();
        // jobLauncher.run(deleteStudentJob, jobParameters);
		// Map<String,Object> parameters = new HashMap<>();
		// parameters.put(Constans.STUDENT_ID, 11); 
		// JobParameters jobParameters = new JobParametersBuilder().addLong(Constans.STUDENT_ID, 11L).toJobParameters();
		// jobLauncher.run(deleteStudentJob, jobParameters);
		// System.out.println("start--------"+dataSource+"------end");
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//  List<Map<String, Object>> resuList = jdbcTemplate.queryForList("select * from student");
		//  System.out.println(resuList);
		// resuList.forEach(item->{
		// 	System.out.println(item);
		// });
	}
}
