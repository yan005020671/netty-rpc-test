package com.tstd2.nettyrpc2.register;

import com.tstd2.nettyrpc2.RequestMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    // 提供方的地址
    private static Map<String, Object> registryMap = new ConcurrentHashMap<>();

    public RegistryHandler() {
        // 服务的发现
        scannerClass("com.tstd2.nettyrpc2.test.service");
    }

    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scannerClass(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "").trim();
                try {
                    Class<?> clazz = Class.forName(className);
                    if (!Modifier.isInterface(clazz.getModifiers())) {
                        Class<?> interfaces = clazz.getInterfaces()[0];
                        // 服务注册到zk
                        registryMap.put(interfaces.getName(), clazz.newInstance());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // consumer的代理通过netty发过来的request信息包
        Object response = new Object();
        // 反序列化
        RequestMsg request = (RequestMsg) msg;
        // zk当中进行匹配
        if (registryMap.containsKey(request.getClassName())) {
            // 服务实例对象
            Object clazz = registryMap.get(request.getClassName());
            // 代理对象里面方法名称和方法参数
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParametersType());
            response = method.invoke(clazz, request.getParametersValue());
        }
        // netty异步的方式写回给客户端
        ctx.write(response);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
