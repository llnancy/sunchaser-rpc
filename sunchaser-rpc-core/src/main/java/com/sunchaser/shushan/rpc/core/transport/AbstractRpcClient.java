package com.sunchaser.shushan.rpc.core.transport;

/**
 * an abstract rpc client
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/15
 */
public abstract class AbstractRpcClient<T> implements RpcClient<T> {

    protected Integer connectionTimeout;

    public AbstractRpcClient(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
