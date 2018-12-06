package com.tstd2.nettyrpc2.proxy;

import com.tstd2.nettyrpc2.CallBack;
import com.tstd2.nettyrpc2.CallBackHolder;
import com.tstd2.nettyrpc2.NettyChannelPool;
import com.tstd2.nettyrpc2.RequestMsg;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {

    //netty channel池
    private static final NettyChannelPool nettyChannelPool = new NettyChannelPool();

    /**
     * 创建一个实际代理处理对象的方法
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<?> clazz) {
        MethodProxy methodProxy = new MethodProxy(clazz);
        // 运行时期jvm当中的代理实例
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
        return result;
    }

    static class MethodProxy implements InvocationHandler {

        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // 如果传过来的是一个以及实现的具体类，
            // 返回表示声明由此method对象表示的方法的类或接口的class对象

            if (false) {

            } else {
                // 增加监控代码
                return rpcInvoke(proxy, method, args);
            }

            return null;
        }

        /**
         * netty异步通信
         *
         * @param proxy
         * @param method
         * @param args
         * @return
         */
        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            // 远程协议对象构建
            RequestMsg request = new RequestMsg();

            // 把数据设置协议数据包对象当中
            request.setServiceId(UUID.randomUUID().toString());
            request.setClassName(this.clazz.getName());
            request.setMethodName(method.getName());
            request.setParametersType(method.getParameterTypes());
            request.setParametersValue(args);

            try {
                // 通过netty传输管道直接拿到响应结果
                CallBack callBack = new CallBack();
                CallBackHolder.put(request.getServiceId(), callBack);
                Channel channel = nettyChannelPool.syncGetChannel();
                channel.writeAndFlush(request);

                return callBack.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
