/*
 * Copyright 2022 LongKui
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

package io.github.llnancy.longkui.core.common;

/**
 * RPC 上下文
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/12
 */
public class RpcContext {

    public static final int HEADER_SIZE = 15;

    /**
     * 1101110
     */
    public static final byte MAGIC = 0x6E;

    public static final String PING = "ping";

    public static final String PONG = "pong";
}
