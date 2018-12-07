package com.tstd2.nettyrpc1.core.client;

import com.tstd2.nettyrpc1.support.MessageCodecUtil;
import com.tstd2.nettyrpc1.support.RpcSerializeFrame;
import com.tstd2.nettyrpc1.support.RpcSerializeProtocol;
import com.tstd2.nettyrpc1.support.hessian.HessianCodecUtil;
import com.tstd2.nettyrpc1.support.hessian.HessianDecoder;
import com.tstd2.nettyrpc1.support.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RpcSendSerializeFrame implements RpcSerializeFrame {

    //后续可以优化成通过spring ioc方式注入
    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecUtil.MESSAGE_LENGTH, 0, MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new LengthFieldPrepender(MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                pipeline.addLast(new MessageSendHandler());
                break;
            }
            case HESSIANSERIALIZE: {
                HessianCodecUtil util = new HessianCodecUtil();
                pipeline.addLast(new HessianEncoder(util));
                pipeline.addLast(new HessianDecoder(util));
                pipeline.addLast(new MessageSendHandler());
                break;
            }
            default:
                throw new RuntimeException("must select serialize");
        }
    }
}