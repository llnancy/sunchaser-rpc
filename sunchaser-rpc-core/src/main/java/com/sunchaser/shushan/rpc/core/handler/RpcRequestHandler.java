package com.sunchaser.shushan.rpc.core.handler;

import com.sunchaser.shushan.rpc.core.common.RpcContext;
import com.sunchaser.shushan.rpc.core.exceptions.RpcException;
import com.sunchaser.shushan.rpc.core.protocol.RpcHeader;
import com.sunchaser.shushan.rpc.core.protocol.RpcProtocol;
import com.sunchaser.shushan.rpc.core.protocol.RpcRequest;
import com.sunchaser.shushan.rpc.core.protocol.RpcResponse;
import com.sunchaser.shushan.rpc.core.util.BeanFactory;
import com.sunchaser.shushan.rpc.core.util.ThrowableUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Netty Rpc Request Handler
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/13
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final ThreadPoolExecutor requestHandlerPool;

    public RpcRequestHandler(ThreadPoolExecutor requestHandlerPool) {
        this.requestHandlerPool = requestHandlerPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        RpcHeader rpcHeader = msg.getRpcHeader();
        byte versionAndType = rpcHeader.getVersionAndType();
        if (RpcContext.isHeartbeat(versionAndType)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("*********** sunchaser-rpc netty RpcRequestHandler read heartbeat.");
            }
            return;
        }

        // 将消息类型从请求转为响应
        rpcHeader.setVersionAndType(RpcContext.transformToResponse(versionAndType));

        Runnable rpc = () -> {
            try {
                RpcProtocol<RpcResponse> rpcProtocol = invokeService(msg);
                ctx.writeAndFlush(rpcProtocol);
            } catch (Exception e) {
                handleException(ctx, rpcHeader, e);
            }
        };

        if (Objects.nonNull(requestHandlerPool)) {
            // 业务线程池中执行
            requestHandlerPool.execute(rpc);
        } else {
            // 业务线程池未被定义，直接在IO线程执行
            rpc.run();
        }
    }

    private void handleException(ChannelHandlerContext ctx, RpcHeader rpcHeader, Exception e) {
        LOGGER.error("process request {} error", rpcHeader.getSequenceId(), e);
        RpcProtocol<RpcResponse> rpcProtocol = RpcProtocol.<RpcResponse>builder()
                .rpcHeader(rpcHeader)
                .content(RpcResponse.builder()
                        .errorMsg(ThrowableUtils.toString(e))
                        .build()
                )
                .build();
        ctx.writeAndFlush(rpcProtocol);
    }

    private RpcProtocol<RpcResponse> invokeService(RpcProtocol<RpcRequest> msg) throws Exception {
        RpcHeader rpcHeader = msg.getRpcHeader();
        RpcResponse rpcResponse = new RpcResponse();
        RpcProtocol<RpcResponse> rpcProtocol = RpcProtocol.<RpcResponse>builder()
                .rpcHeader(rpcHeader)
                .content(rpcResponse)
                .build();
        Object result = doInvoke(msg.getContent());
        rpcResponse.setResult(result);
        return rpcProtocol;
    }

    private Object doInvoke(RpcRequest rpcRequest) throws Exception {
        String serviceName = rpcRequest.getServiceName();
        Object bean = BeanFactory.getBean(serviceName);
        if (Objects.isNull(bean)) {
            throw new RpcException(serviceName + " service bean does not exist.");
        }
        String methodName = rpcRequest.getMethodName();
        Class<?>[] argTypes = rpcRequest.getArgTypes();
        Object[] args = rpcRequest.getArgs();

        // todo FastClass
        Method method = bean.getClass().getMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(bean, args);
    }

}
