package com.chong.study.batch.chunk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;

import com.chong.study.Constans;
import com.chong.study.StudyApplication;
import com.chong.study.service.StudentService;

@SpringBatchTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StudyApplication.class)
public class RightChunkTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job job;
    // Job partitionJob;

    @Autowired
    private StudentService studentService;

    @Test
    void testStudentJob() throws Exception {

        StopWatch stopWatch = new StopWatch();
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        stopWatch.start();
        jobLauncherTestUtils.getJobLauncher().run(job, jobParameters);
        System.out.println("--------------" + Thread.activeCount() + "--------------");
        stopWatch.stop();

        System.out.println("-------------" + stopWatch.getTotalTimeMillis());

        assertEquals(Constans.DATA_COUNT, studentService.count());
        studentService.clear();
    }
}
