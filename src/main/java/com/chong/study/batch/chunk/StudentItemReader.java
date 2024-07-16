package com.chong.study.batch.chunk;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.chong.study.Constans;
import com.chong.study.pojo.Student;
import com.chong.study.util.StudentUtils;
import com.opencsv.CSVReader;

@StepScope
@Component
public class StudentItemReader extends FlatFileItemReader<Student> {

    // @BeforeStep
    // public void beforeStep(StepExecution stepExecution) {

    // setResource(new FileSystemResource(Constans.INPUT_FILE_PATH));
    // setLineMapper(studentLineMapper());
    // }

    private Map<Long, List<String>> dataMapList = new HashMap<>();

    public StudentItemReader() {
        setResource(new FileSystemResource(Constans.INPUT_FILE_PATH));
        setLineMapper(studentLineMapper());
    }

    private LineMapper<Student> studentLineMapper() {
        return (line, lineNumber) -> {
            CSVReader csvReader = new CSVReader(new StringReader(line));
            String[] fileds = csvReader.readAll().get(0);
            String dataId = fileds[0];
            csvReader.close();
            List<String> idList = dataMapList.getOrDefault(Thread.currentThread().threadId(), new ArrayList<>());
            idList.add(dataId);
            dataMapList.put(Thread.currentThread().threadId(), idList);
            return StudentUtils.parseLineToStudent(fileds);
        };
    }
}