package com.chong.study;

public class Printer {
    public static void print(String dataId) {
        System.out.println(
                "--dataId: " + dataId + "-----writer-------" + Thread.currentThread().threadId() + "--------------");
    }
}
