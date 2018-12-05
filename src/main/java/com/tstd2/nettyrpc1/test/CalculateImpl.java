package com.tstd2.nettyrpc1.test;

public class CalculateImpl implements Calculate {
    //两数相加
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}