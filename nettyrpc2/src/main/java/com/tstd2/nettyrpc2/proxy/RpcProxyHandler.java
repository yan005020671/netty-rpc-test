package com.tstd2.nettyrpc2.proxy;

import com.tstd2.nettyrpc2.CallBack;
import com.tstd2.nettyrpc2.CallBackHolder;
import com.tstd2.nettyrpc2.ResponseMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取远程服务端信息包
        ResponseMsg response = (ResponseMsg) msg;
        String serviceId = response.getServiceId();

        CallBack callBack = CallBackHolder.get(serviceId);
        if (callBack != null) {
            CallBackHolder.remove(serviceId);
            callBack.over(response);
        }
    }

}
