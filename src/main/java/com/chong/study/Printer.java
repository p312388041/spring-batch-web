package com.chong.study;

public class Printer {
    public static void print() {
        System.out.println("-------writer-------" + Thread.currentThread().threadId() + "--------------");
    }
}
