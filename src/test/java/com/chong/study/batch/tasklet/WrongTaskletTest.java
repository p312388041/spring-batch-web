package com.chong.study.batch.tasklet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
public class WrongTaskletTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private StudentService studentService;

    @Autowired
    Job wrongUseBatchJob;

    @Test
    void testWrongUseBatch() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        jobLauncherTestUtils.getJobLauncher().run(wrongUseBatchJob, jobParameters);
        stopWatch.stop();
        System.out.println("-------------" + stopWatch.getTotalTimeMillis());

        assertEquals(studentService.count(), Constans.DATA_COUNT);
        // studentService.clear();
    }
}
