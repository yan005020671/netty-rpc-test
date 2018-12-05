package com.tstd2.nettyrpc2.proxy;

import com.tstd2.nettyrpc2.RequestMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {

    /**
     * 创建一个实际代理处理对象的方法
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<?> clazz) {
        return create(clazz, null);
    }

    public static <T> T create(Class<?> clazz, String serviceName) {
        MethodProxy methodProxy = new MethodProxy(clazz, serviceName);
        // 运行时期jvm当中的代理实例
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
        return result;
    }

    static class MethodProxy implements InvocationHandler {

        private Class<?> clazz;
        private String serviceName;

        public MethodProxy(Class<?> clazz, String serviceName) {
            this.clazz = clazz;
            this.serviceName = serviceName;
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
            RequestMsg msg = new RequestMsg();

            // 把数据设置协议数据包对象当中
            msg.setClassName(this.clazz.getName());
            msg.setMethodName(method.getName());
            msg.setParametersType(method.getParameterTypes());
            msg.setParametersValue(args);

            // 异步调用
            // 基于NIO的非阻塞实现并行调用，客户端不需要启动多线程即可完成并行调用多个远程服务，相对多线程开销较小
            // 构建RpcProxyHandler异步处理响应的Handler
            final RpcProxyHandler consumerHandler = new RpcProxyHandler();

            // netty
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                pipeline.addLast(new LengthFieldPrepender(4));
                                pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                                pipeline.addLast(consumerHandler);

                            }
                        });

                ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
                future.channel().writeAndFlush(msg);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }

            // 通过netty传输管道直接拿到响应结果
            return consumerHandler.getResponse();
        }
    }
}
