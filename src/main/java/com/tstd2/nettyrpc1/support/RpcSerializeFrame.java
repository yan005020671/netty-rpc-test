package com.tstd2.nettyrpc1.support;

import io.netty.channel.ChannelPipeline;

/**
 * RPC消息序序列化协议选择器接口
 */
public interface RpcSerializeFrame {

    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}