package com.tstd2.nettyrpc1.test;

import com.tstd2.nettyrpc1.core.client.MessageSendExecutor;
import com.tstd2.nettyrpc1.support.RpcSerializeProtocol;

public class RpcClientTest {

    public static void main(String[] args) {
        RpcSerializeProtocol protocol = RpcSerializeProtocol.HESSIANSERIALIZE;
        final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:18888", protocol);

        Calculate calc = executor.execute(Calculate.class);
        int add = calc.add(1, 1);
        System.out.println("calc add result:[" + add + "]");

        executor.stop();
    }
}
