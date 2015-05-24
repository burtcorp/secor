/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pinterest.secor.message;

import com.pinterest.secor.common.Components;
import java.util.Arrays;

/**
 * Parsed message is a Kafka message that has been processed by the parser
 * that extract path and filename components from the message.
 *
 * @author Pawel Garbacki (pawel@pinterest.com)
 */
public class ParsedMessage extends Message {
    private Components mComponents;

    @Override
    public String toString() {
        return "ParsedMessage{" + fieldsToString() +  ", mComponents=" +
               Arrays.toString(mComponents.getPath()) + "}";
    }

    public ParsedMessage(Message message, Components components) {
        super(message.getTopic(), message.getKafkaPartition(),
              message.getOffset(), message.getKey(), message.getPayload());
        this.mComponents = components;
    }

    public ParsedMessage(String topic, int kafkaPartition, long offset,
                         byte[] key, byte[] payload, Components components) {
        super(topic, kafkaPartition, offset, key, payload);
        this.mComponents = components;
    }

    public Components getComponents() {
        return mComponents;
    }
}
