/*
 * Copyright 2023 LongKui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2022 LongKui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.llnancy.longkui.core.test.proxy.jmh;

import io.github.llnancy.longkui.core.config.RpcClientConfig;
import io.github.llnancy.longkui.core.config.RpcServiceConfig;
import io.github.llnancy.longkui.core.test.HelloService;
import io.github.llnancy.longkui.core.test.proxy.jmh.impl.JMHByteBuddyDynamicProxy;
import io.github.llnancy.longkui.core.test.proxy.jmh.impl.JMHCglibDynamicProxy;
import io.github.llnancy.longkui.core.test.proxy.jmh.impl.JMHJavassistDynamicProxy;
import io.github.llnancy.longkui.core.test.proxy.jmh.impl.JMHJdkDynamicProxy;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 动态代理对象调用方法 JMH 基准测试
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/8/24
 */
@BenchmarkMode(Mode.AverageTime)// 统计模式
@OutputTimeUnit(TimeUnit.NANOSECONDS)// 统计单位
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 10)// 预热3轮，每轮10秒
@Measurement(iterations = 3, time = 10)// 度量3轮，每轮10秒
@Fork(1)
@Threads(8)
public class InvokeDynamicProxyBenchmarkTest {

    private static final String JDK = "jdk";

    private static final String CGLIB = "cglib";

    private static final String JAVASSIST = "javassist";

    private static final String BYTE_BUDDY = "byteBuddy";

    private static final RpcClientConfig RPC_CLIENT_CONFIG = RpcClientConfig.createDefaultConfig();

    private static final RpcServiceConfig RPC_SERVICE_CONFIG = RpcServiceConfig.createDefaultConfig(HelloService.class);

    private static final HelloService JDK_PROXY_INSTANCE = JMHJdkDynamicProxy.getInstance().createProxyInstance(RPC_CLIENT_CONFIG, RPC_SERVICE_CONFIG);

    private static final HelloService CGLIB_PROXY_INSTANCE = JMHCglibDynamicProxy.getInstance().createProxyInstance(RPC_CLIENT_CONFIG, RPC_SERVICE_CONFIG);

    private static final HelloService JAVASSIST_PROXY_INSTANCE = JMHJavassistDynamicProxy.getInstance().createProxyInstance(RPC_CLIENT_CONFIG, RPC_SERVICE_CONFIG);

    private static final HelloService BYTE_BUDDY_PROXY_INSTANCE = JMHByteBuddyDynamicProxy.getInstance().createProxyInstance(RPC_CLIENT_CONFIG, RPC_SERVICE_CONFIG);

    @Benchmark
    public String jdk() {
        return JDK_PROXY_INSTANCE.sayHello(JDK);
    }

    @Benchmark
    public String cglib() {
        return CGLIB_PROXY_INSTANCE.sayHello(CGLIB);
    }

    @Benchmark
    public String javassist() {
        return JAVASSIST_PROXY_INSTANCE.sayHello(JAVASSIST);
    }

    @Benchmark
    public String byteBuddy() {
        return BYTE_BUDDY_PROXY_INSTANCE.sayHello(BYTE_BUDDY);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(InvokeDynamicProxyBenchmarkTest.class.getSimpleName())
                .result("invoke-jmh-result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
