package com.tstd2.nettyrpc1.test;

import com.tstd2.nettyrpc1.core.client.MessageSendExecutor;
import com.tstd2.nettyrpc1.support.RpcSerializeProtocol;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;

public class RpcParallelTest {

    public static void main(String[] args) throws Exception {
        RpcSerializeProtocol protocol = RpcSerializeProtocol.HESSIANSERIALIZE;
        final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:18888", protocol);
        //并行度10000
        int parallel = 10000;

        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);

        for (int index = 0; index < parallel; index++) {
            CalcParallelRequestThread client = new CalcParallelRequestThread(executor, signal, finish, index);
            new Thread(client).start();
        }
        
        //10000个并发线程瞬间发起请求操作
        signal.countDown();
        finish.await();
        
        sw.stop();

        String tip = String.format("RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);

        executor.stop();
    }
}