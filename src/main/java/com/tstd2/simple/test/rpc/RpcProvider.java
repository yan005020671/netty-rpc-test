package com.tstd2.simple.test.rpc;

import com.tstd2.simple.framwork.RpcFramework;
import com.tstd2.simple.test.service.HelloService;
import com.tstd2.simple.test.service.HelloServiceImpl;

/**
 * RpcProvider
 *
 * @author william.liangf
 */
public class RpcProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }

}