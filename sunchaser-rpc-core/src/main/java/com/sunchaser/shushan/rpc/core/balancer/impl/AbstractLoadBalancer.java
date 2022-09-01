package com.sunchaser.shushan.rpc.core.balancer.impl;

import com.sunchaser.shushan.rpc.core.balancer.LoadBalancer;
import com.sunchaser.shushan.rpc.core.balancer.Node;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Function;

/**
 * RPC调用 负载均衡策略接口抽象实现
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/15
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    @Override
    public <T> Node<T> select(List<? extends Node<T>> nodes) {
        return select(nodes, this::doSelect);
    }

    @Override
    public <T> Node<T> select(List<? extends Node<T>> nodes, String hashKey) {
        return select(nodes, nodeList -> doSelect(nodeList, hashKey));
    }

    private <T> Node<T> select(List<? extends Node<T>> nodes, Function<List<? extends Node<T>>, Node<T>> function) {
        if (CollectionUtils.isEmpty(nodes)) {
            return null;
        }
        if (nodes.size() == 1) {
            return nodes.get(0);
        }
        return function.apply(nodes);
    }

    protected <T> Node<T> doSelect(List<? extends Node<T>> nodes) {
        throw new UnsupportedOperationException();
    }

    protected <T> Node<T> doSelect(List<? extends Node<T>> nodes, String hashKey) {
        throw new UnsupportedOperationException();
    }
}
