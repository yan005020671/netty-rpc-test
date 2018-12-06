package com.tstd2.nettyrpc2.test;

import com.tstd2.nettyrpc2.proxy.RpcProxy;
import com.tstd2.nettyrpc2.test.service.CalcService;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    public static void main(String[] args) {
        // 通过代理机制获取远程处理执行代理对象
        CalcService calcService = RpcProxy.create(CalcService.class);
        System.out.println(calcService.add(1, 2));
        System.out.println(calcService.add(1, 3));
        System.out.println(calcService.add(1, 4));

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(threadPool);

        long b = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            final int x = i;
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return calcService.add(x, x);
                }
            });
        }

        for (int i = 0; i < 10000; i++) {
            try {
                completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - b);
    }


}
