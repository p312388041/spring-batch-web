package com.chong.study.single;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chong.study.Constans;
import com.chong.study.mapper.StudentMapper;
import com.chong.study.pojo.Student;
import com.chong.study.util.StudentUtils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Component
public class NotUseBatch {

    @Autowired
    private StudentMapper studentMapper;

    public void process() throws CsvValidationException, IOException {
        String filePath = Constans.INPUT_FILE_PATH;
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath)));
        reader.forEach(item -> {
            // deal with data
            Student student = StudentUtils.parseLineToStudent(item);
            studentMapper.insert(student);
        });
        reader.close();
    }

    public void begin() {
        System.out.println("haha");
    }
}
