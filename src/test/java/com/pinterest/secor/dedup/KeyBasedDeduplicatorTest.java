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
package com.pinterest.secor.dedup;

import com.pinterest.secor.common.TopicPartition;
import com.google.common.collect.MinMaxPriorityQueue;
import junit.framework.TestCase;

public class KeyBasedDeduplicatorTest extends TestCase {
    private Deduplicator mDeduplicator;
    private TopicPartition mTopicPartition;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDeduplicator = new KeyBasedDeduplicator(3);
        mTopicPartition = new TopicPartition("test-topic", 0);
    }

    public void testDifferentEntries() {
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "1".getBytes()));
    }

    public void testEqualEntries() {
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
        assertTrue(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
    }

    public void testEntriesWithEmptyKeys() {
        assertFalse(mDeduplicator.isPresent(mTopicPartition, new byte[0]));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, new byte[0]));
    }

    public void testEntriesWithNullKeys() {
        assertFalse(mDeduplicator.isPresent(mTopicPartition, null));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, null));
    }

    public void testExpire() {
        mDeduplicator.isPresent(mTopicPartition, "0".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "1".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "2".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "3".getBytes());
    }

    public void testReset() {
        mDeduplicator.isPresent(mTopicPartition, "0".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "1".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "2".getBytes());
        assertTrue(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
        assertTrue(mDeduplicator.isPresent(mTopicPartition, "1".getBytes()));
        assertTrue(mDeduplicator.isPresent(mTopicPartition, "2".getBytes()));
        mDeduplicator.reset(mTopicPartition);
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "1".getBytes()));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "2".getBytes()));
    }

    public void testResetWithEmptyEntries() {
        mDeduplicator.isPresent(mTopicPartition, "0".getBytes());
        mDeduplicator.isPresent(mTopicPartition, "1".getBytes());
        mDeduplicator.reset(mTopicPartition);
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "0".getBytes()));
        assertFalse(mDeduplicator.isPresent(mTopicPartition, "1".getBytes()));
    }
}
