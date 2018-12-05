package com.tstd2.nettyrpc1.support.hessian;


import com.tstd2.nettyrpc1.support.MessageCodecUtil;
import com.tstd2.nettyrpc1.support.MessageDecoder;

/**
 * Hessian解码器
 */
public class HessianDecoder extends MessageDecoder {

    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}