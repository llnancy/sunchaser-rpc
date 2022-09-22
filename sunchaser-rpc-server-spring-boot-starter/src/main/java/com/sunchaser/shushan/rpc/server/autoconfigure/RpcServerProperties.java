package com.sunchaser.shushan.rpc.server.autoconfigure;

import com.sunchaser.shushan.rpc.core.config.ThreadPoolConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * rpc server properties
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/9/21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "sunchaser.rpc.server")
public class RpcServerProperties {

    /**
     * server host
     */
    private String host;

    /**
     * server port
     */
    private Integer port;

    /**
     * io threads
     */
    private Integer ioThreads;

    /**
     * biz thread pool config (request executor)
     */
    @NestedConfigurationProperty
    private ThreadPoolConfig requestExecutorConfig;

    /**
     * rpc server (service provider)
     */
    private String rpcServer;

    /**
     * registry
     */
    private String registry;
}
