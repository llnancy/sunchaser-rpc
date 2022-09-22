/*
 * Copyright 2022 SunChaser
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

package com.sunchaser.shushan.rpc.core.test.compress;

import com.sunchaser.shushan.rpc.core.compress.Compressor;
import com.sunchaser.shushan.rpc.core.protocol.RpcRequest;
import com.sunchaser.shushan.rpc.core.serialize.Serializer;
import com.sunchaser.shushan.rpc.core.serialize.impl.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * Abstract Compressor Test
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/9/8
 */
@Slf4j
public abstract class AbstractCompressorTest {

    protected static final RpcRequest REQUEST = RpcRequest.builder()
            .serviceName("com.sunchaser.shushan.rpc.core.test.HelloService")
            .methodName("sayHello")
            .version("1")
            .argTypes(new Class[]{String.class, null, Integer.class})
            .args(new Object[]{"hello, sunchaser", null, 666})
            .build();

    @Test
    public void compress() {
        Serializer serializer = new JsonSerializer();
        byte[] serialize = serializer.serialize(REQUEST);
        Compressor compressor = getCompressor();
        byte[] compress = compressor.compress(serialize);
        byte[] unCompress = compressor.unCompress(compress);
        RpcRequest deserialize = serializer.deserialize(unCompress, RpcRequest.class);
        LOGGER.info("deserialize: {}", deserialize);
    }

    protected abstract Compressor getCompressor();
}
