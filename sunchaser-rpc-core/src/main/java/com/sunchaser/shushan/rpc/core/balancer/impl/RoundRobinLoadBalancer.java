package com.sunchaser.shushan.rpc.core.balancer.impl;

import com.google.common.collect.Maps;
import com.sunchaser.shushan.rpc.core.balancer.Node;
import com.sunchaser.shushan.rpc.core.balancer.WeightNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询 & 加权轮询
 * 当每个节点权重都相同时（默认权重为1），退化成普通轮询。线程安全
 * Borrowed from org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/17
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    /**
     * 全局线程安全映射表
     */
    private final ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> wrrMapMap = Maps.newConcurrentMap();

    @Override
    protected <T> Node<T> doSelect(List<? extends Node<T>> nodes) {
        // 获取整个nodes对应的WeightedRoundRobin映射表
        ConcurrentMap<String, WeightedRoundRobin> wrrMap = wrrMapMap.computeIfAbsent(nodes.toString(), v -> Maps.newConcurrentMap());
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        Node<T> selectedNode = null;
        WeightedRoundRobin selectedWrr = null;
        for (Node<T> tNode : nodes) {
            WeightNode<T> node = (WeightNode<T>) tNode;
            int weight = node.getWeight();
            // 给node创建对应的WeightedRoundRobin对象
            WeightedRoundRobin wrr = wrrMap.computeIfAbsent(
                    node.toString(),
                    v -> WeightedRoundRobin.builder()
                            .weight(weight)
                            .build()
            );
            // 初始化wrr对象的当前权重为配置的权重weight
            long current = wrr.increaseCurrent();
            // 利用maxCurrent变量，迭代寻找具有最大权重的Node节点并选中
            if (current > maxCurrent) {
                // 暂存当前迭代最大权重值
                maxCurrent = current;
                // 选中当前迭代最大权重Node
                selectedNode = node;
                // 选中当前迭代最大权重WeightedRoundRobin
                selectedWrr = wrr;
            }
            // 累加总权重
            totalWeight += weight;
        }
        if (Objects.nonNull(selectedNode)) {
            // 被选中的节点权重减去总权重
            selectedWrr.sel(totalWeight);
            return selectedNode;
        }
        // should not happen here
        return nodes.get(0);
    }

    /**
     * 每个Node节点对应一个WeightedRoundRobin对象
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class WeightedRoundRobin {

        /**
         * Node配置的权重（不会变化）
         */
        private int weight;

        /**
         * 当前权重，每个Node在每一轮的负载均衡中都会进行调整，初始值为0。
         */
        private final AtomicLong current = new AtomicLong(0);

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }
    }
}
