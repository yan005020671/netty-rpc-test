package com.tstd2.nettyrpc1.support.hessian;


import com.tstd2.nettyrpc1.support.MessageCodecUtil;
import com.tstd2.nettyrpc1.support.MessageEncoder;

/**
 * Hessian编码器
 */
public class HessianEncoder extends MessageEncoder {

    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}