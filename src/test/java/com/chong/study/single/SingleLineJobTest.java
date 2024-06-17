package com.chong.study.single;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import com.chong.study.Constans;
import com.chong.study.StudyApplication;
import com.chong.study.service.StudentService;
import com.opencsv.exceptions.CsvValidationException;

@SpringBootTest(classes = StudyApplication.class)
public class SingleLineJobTest {

    @Autowired
    private NotUseBatch singleLineJob;

    @Autowired
    private StudentService studentService;

    @Test
    public void testProcess() throws CsvValidationException, IOException, InterruptedException {
        // for (int i = 0; i < 10; i++) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        singleLineJob.process();
        stopWatch.stop();
        System.out.println("-------------" + stopWatch.getTotalTimeMillis());
        assertEquals(studentService.count(), Constans.DATA_COUNT);
        studentService.clear();
        // }
    }
}
