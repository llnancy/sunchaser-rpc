package com.sunchaser.shushan.rpc.core.test.serialize.jmh;

import com.sunchaser.shushan.rpc.core.protocol.RpcRequest;
import com.sunchaser.shushan.rpc.core.serialize.Serializer;
import com.sunchaser.shushan.rpc.core.serialize.impl.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * serialize序列化 JMH 基准测试
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/9/8
 */
@BenchmarkMode(Mode.AverageTime)// 统计模式
@OutputTimeUnit(TimeUnit.NANOSECONDS)// 统计单位
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)// 预热3轮，每轮10秒
@Measurement(iterations = 3, time = 3)// 度量3轮，每轮10秒
@Fork(1)
@Threads(8)
public class SerializeBenchmarkTest {

    private RpcRequest request;

    private Serializer hessian2;

    private Serializer json;

    private Serializer kryo;

    private Serializer protostuff;

    private Serializer xml;

    @Setup
    public void prepare() {
        request = RpcRequest.builder()
                .serviceName("com.sunchaser.shushan.rpc.core.test.HelloService")
                .methodName("sayHello")
                .version("1")
                .argTypes(new Class[]{String.class, null, Integer.class})
                .args(new Object[]{"hello, sunchaser", null, 666})
                .build();
        hessian2 = new Hessian2Serializer();
        json = new JsonSerializer();
        kryo = new KryoSerializer();
        protostuff = new ProtostuffSerializer();
        xml = new XmlSerializer();
    }

    @Benchmark
    public byte[] hessian2() {
        return hessian2.serialize(request);
    }

    @Benchmark
    public byte[] json() {
        return json.serialize(request);
    }

    @Benchmark
    public byte[] kryo() {
        return kryo.serialize(request);
    }

    @Benchmark
    public byte[] protostuff() {
        return protostuff.serialize(request);
    }

    @Benchmark
    public byte[] xml() {
        return xml.serialize(request);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(SerializeBenchmarkTest.class.getSimpleName())
                .result("serialize-jmh-result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
