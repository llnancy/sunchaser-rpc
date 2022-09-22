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

package com.sunchaser.shushan.rpc.demo.server.service.impl;

import com.sunchaser.shushan.rpc.boot.server.annotation.RpcService;
import com.sunchaser.shushan.rpc.demo.facade.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloWorldService implementation v2
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/9/21
 */
@RpcService(version = "0.0.2", group = "hello")
@Slf4j
public class HelloWorldV2ServiceImpl implements HelloWorldService {

    @Override
    public String hello(String hello) {
        LOGGER.info("hello world v2 (hello) :{}", hello);
        return "hello world v2 (hello) :" + hello;
    }

    @Override
    public String world(String wor, Integer l, Long d) {
        LOGGER.info("hello world v2 (world) :{}, {}, {}", wor, l, d);
        return "hello world v2 (world) :" + wor + ", " + l + ", " + d;
    }
}
