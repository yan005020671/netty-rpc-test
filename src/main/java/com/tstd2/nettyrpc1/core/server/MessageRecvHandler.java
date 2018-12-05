package com.tstd2.nettyrpc1.core.server;

import com.tstd2.nettyrpc1.model.MessageRequest;
import com.tstd2.nettyrpc1.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Map;

/**
 * Rpc服务器消息处理
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handlerMap;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, response, handlerMap);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask, ctx, request, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //网络有异常要关闭通道
        ctx.close();
    }
}