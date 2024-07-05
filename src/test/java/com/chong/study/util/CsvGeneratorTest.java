package com.chong.study.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.chong.study.Constans;
import com.chong.study.StudyApplication;
import com.chong.study.pojo.Student;

@SpringBootTest(classes = StudyApplication.class)
public class CsvGeneratorTest {

    @Test
    void TestGenerateCsv() throws IOException {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < Constans.DATA_COUNT; i++) {
            list.add(StudentFactory.createStudent(i));
        }

        String filePath = "C:\\Users\\31238\\OneDrive\\デスクトップ\\test_" + Constans.DATA_COUNT + ".csv";
        CsvGenerator.genatate(filePath, list);
        
        // String forlderPath = "C:\\Users\\31238\\OneDrive\\デスクトップ\\test_" + Constans.DATA_COUNT;
        // CsvGenerator.generatePartitionFiles(forlderPath, Constans.PARTITION_SIZE, list);
    }
}
