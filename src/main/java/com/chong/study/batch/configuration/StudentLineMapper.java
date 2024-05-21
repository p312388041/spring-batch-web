package com.chong.study.batch.configuration;

import java.io.StringReader;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.transaction.annotation.Transactional;

import com.chong.study.pojo.Student;
import com.opencsv.CSVReader;

@Transactional
public class StudentLineMapper implements LineMapper<Student> {
        @Override
        public Student mapLine(String line, int lineNumber) throws Exception {
                CSVReader csvReader = new CSVReader(new StringReader(line));
                String[] fileds = csvReader.readAll().get(0);
                csvReader.close();
                float chinese = Float.parseFloat(fileds[2]);
                float english = Float.parseFloat(fileds[3]);
                float math = Float.parseFloat(fileds[4]);
                return new Student(Integer.parseInt(fileds[0]), fileds[1], chinese, english, math);
        }
}
