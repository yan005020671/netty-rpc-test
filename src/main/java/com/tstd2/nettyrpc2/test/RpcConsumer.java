package com.tstd2.nettyrpc2.test;

import com.tstd2.nettyrpc2.proxy.RpcProxy;
import com.tstd2.nettyrpc2.test.service.CalcService;

public class RpcConsumer {

    public static void main(String[] args) {
        // 通过代理机制获取远程处理执行代理对象
        CalcService calcService = RpcProxy.create(CalcService.class, "calcService");
        System.out.println(calcService.add(1, 2));
        System.out.println(calcService.add(1, 3));
        System.out.println(calcService.add(1, 4));
    }


}
