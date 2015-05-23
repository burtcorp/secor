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
package com.pinterest.secor.common;

import com.pinterest.secor.message.ParsedMessage;
import com.pinterest.secor.util.LogFileUtil;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * LogFileTest tests the logic operating on lof file paths.
 *
 * @author Pawel Garbacki (pawel@pinterest.com)
 */
public class LogFilePathTest extends TestCase {
    private static final String PREFIX = "/some_parent_dir";
    private static final String TOPIC = "some_topic";
    private static final int GENERATION = 10;
    private static final int KAFKA_PARTITION = 0;
    private static final long LAST_COMMITTED_OFFSET = 100;
    private static final String[] PATH_PARTITIONS = {"some_partition", "some_other_partition"};
    private static final String PATH =
        "/some_parent_dir/some_topic/some_partition/some_other_partition/" +
        "10_0_00000000000000000100";
    private static final String CRC_PATH =
            "/some_parent_dir/some_topic/some_partition/some_other_partition/" +
            ".10_0_00000000000000000100.crc";

    private LogFilePath mLogFilePath;
    private Components mComponents;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mComponents = new Components(PATH_PARTITIONS, TOPIC, GENERATION);
        mLogFilePath = new LogFilePath(PREFIX, TOPIC, KAFKA_PARTITION, mComponents, GENERATION,
                                       LAST_COMMITTED_OFFSET, "");
    }

    public void testConstructFromMessage() throws Exception {
        ParsedMessage message = new ParsedMessage(TOPIC, KAFKA_PARTITION, 1000,
                                                  "some_payload".getBytes(), mComponents);
        LogFilePath logFilePath = new LogFilePath(PREFIX, message.getTopic(), message.getKafkaPartition(), message.getComponents(), GENERATION,
            LAST_COMMITTED_OFFSET, "");
        assertEquals(PATH, logFilePath.getLogFilePath());
    }

    public void testConstructFromPath() throws Exception {
        LogFilePath logFilePath = LogFileUtil.createFromPath("/some_parent_dir", PATH);

        assertEquals(PATH, logFilePath.getLogFilePath());
        assertEquals(TOPIC, logFilePath.getTopic());
        assertEquals(mComponents, logFilePath.getComponents());
        assertEquals(GENERATION, logFilePath.getGeneration());
        assertEquals(KAFKA_PARTITION, logFilePath.getKafkaPartition());
        assertEquals(LAST_COMMITTED_OFFSET, logFilePath.getOffset());
    }

    public void testGetters() throws Exception {
        assertEquals(TOPIC, mLogFilePath.getTopic());
        assertEquals(mComponents, mLogFilePath.getComponents());
        assertEquals(GENERATION, mLogFilePath.getGeneration());
        assertEquals(KAFKA_PARTITION, mLogFilePath.getKafkaPartition());
        assertEquals(LAST_COMMITTED_OFFSET, mLogFilePath.getOffset());
    }

    public void testGetLogFilePath() throws Exception {
        assertEquals(PATH, mLogFilePath.getLogFilePath());
    }

    public void testGetLogFileCrcPath() throws Exception {
        assertEquals(CRC_PATH, mLogFilePath.getLogFileCrcPath());
    }
}
