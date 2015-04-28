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
import com.pinterest.secor.util.StatsUtil;
import com.yammer.metrics.core.Meter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeyBasedDeduplicator implements Deduplicator {
    private static final Logger LOG = LoggerFactory.getLogger(KeyBasedDeduplicator.class);
    private final int mLimit;
    private final Map<TopicPartition, byte[][]> mDedupMap;
    private final Meter mDuplicates;

    public KeyBasedDeduplicator(int limit) {
        this.mLimit = limit;
        this.mDedupMap = new HashMap<TopicPartition, byte[][]>();
        this.mDuplicates = StatsUtil.newMeter("secor", "Deduplication", "duplicates", "duplicates");
    }

    public boolean isPresent(TopicPartition topicPartition, byte[] key) {
        if (key == null || key.length == 0) {
            return false;
        }
        if (!mDedupMap.containsKey(topicPartition)) {
            mDedupMap.put(topicPartition, new byte[mLimit][]);
        }
        int position = (Arrays.hashCode(key) & 0x7FFFFFFF) % mLimit;
        byte[][] dedupArray = mDedupMap.get(topicPartition);
        byte[] candidate = dedupArray[position];
        if (candidate != null && Arrays.equals(candidate, key)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("duplicate key in " + topicPartition + ", " + new String(key) + " == " + new String(candidate));
            }
            mDuplicates.mark();
            return true;
        } else {
            dedupArray[position] = key;
            return false;
        }
    }

    public void reset(TopicPartition topicPartition) {
        mDedupMap.put(topicPartition, new byte[mLimit][]);
    }
}
