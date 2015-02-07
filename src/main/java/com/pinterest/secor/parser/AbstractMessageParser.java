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
package com.pinterest.secor.parser;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.common.Components;
import com.pinterest.secor.message.Message;

// TODO(pawel): should we offer a multi-message parser capable of parsing multiple types of
// messages?  E.g., it could be implemented as a composite trying out different parsers and using
// the one that works.  What is the performance cost of such approach?

/**
 * Message parser extracts components from messages.
 *
 * @author Pawel Garbacki (pawel@pinterest.com)
 */
public abstract class AbstractMessageParser implements MessageParser {
    protected SecorConfig mConfig;

    public AbstractMessageParser(SecorConfig config) {
        mConfig = config;
    }

    @Override
    public Components parse(Message message) throws Exception {
        String[] partitions = extractPartitions(message);
        return new Components(partitions, message.getTopic(), mConfig.getGeneration());
    }

    public abstract String[] extractPartitions(Message payload) throws Exception;
}
