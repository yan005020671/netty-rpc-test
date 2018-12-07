package com.tstd2.nettyrpc1.core.client;

import com.google.common.reflect.AbstractInvocationHandler;
import com.tstd2.nettyrpc1.core.MessageCallBack;
import com.tstd2.nettyrpc1.model.MessageRequest;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Rpc客户端消息处理
 * @param <T>
 */
public class MessageSendProxy<T> extends AbstractInvocationHandler {

    @Override
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);

        MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = handler.sendRequest(request);
        return callBack.start();
    }
}