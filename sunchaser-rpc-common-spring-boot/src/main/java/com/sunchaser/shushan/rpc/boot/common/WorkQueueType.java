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

package com.sunchaser.shushan.rpc.boot.common;

import com.google.common.collect.Queues;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * work queue type enum
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/9/22
 */
public enum WorkQueueType {

    /**
     * {@link java.util.concurrent.SynchronousQueue}
     */
    SYNCHRONOUS_QUEUE() {
        @Override
        public BlockingQueue<Runnable> getWorkQueue(Integer capacity) {
            return Queues.newSynchronousQueue();
        }
    },

    /**
     * {@link java.util.concurrent.ArrayBlockingQueue}
     */
    ARRAY_BLOCKING_QUEUE() {
        @Override
        public BlockingQueue<Runnable> getWorkQueue(Integer capacity) {
            return Queues.newArrayBlockingQueue(capacity);
        }
    },

    /**
     * {@link java.util.concurrent.LinkedBlockingQueue}
     */
    LINKED_BLOCKING_QUEUE() {
        @Override
        public BlockingQueue<Runnable> getWorkQueue(Integer capacity) {
            return (Objects.isNull(capacity) || capacity == 0) ? Queues.newLinkedBlockingQueue() : Queues.newLinkedBlockingQueue(capacity);
        }
    },

    ;

    public abstract BlockingQueue<Runnable> getWorkQueue(Integer capacity);
}
