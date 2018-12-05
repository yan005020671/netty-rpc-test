package com.tstd2.nettyrpc1.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class RpcServiceTest {

    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("rpc-invoke-config.xml");
        System.in.read();
    }
}
