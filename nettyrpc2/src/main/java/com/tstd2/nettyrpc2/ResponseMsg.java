package com.tstd2.nettyrpc2;

import java.io.Serializable;

/**
 * rpc服务应答结构
 */
public class ResponseMsg implements Serializable {

    private String serviceId;
    private Object result;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}