package com.tstd2.nettyrpc2.test.service;

import com.tstd2.nettyrpc2.annotation.RpcService;

@RpcService("calculate")
public class CalcServiceImpl implements CalcService {
    @Override
    public int add(int x, int y) {
        return x + y;
    }
}
