package com.chong.study.batch.mapper;

// class StudentLineMapper implements LineMapper<Student> {
//     @Override
//     public Student mapLine(String line, int lineNumber) throws Exception {
//         CSVReader csvReader = new CSVReader(new StringReader(line));
//         String[] fileds = csvReader.readAll().get(0);
//         csvReader.close();
//         return new Student(Integer.parseInt(fileds[0]), fileds[1], Long.parseLong(fileds[2]),
//                 Long.parseLong(fileds[3]), Long.parseLong(fileds[4]), Long.parseLong(fileds[5]));
//     }
// }