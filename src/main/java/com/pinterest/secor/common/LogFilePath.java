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

import org.apache.commons.lang.StringUtils;

import com.pinterest.secor.common.Components;
import com.pinterest.secor.util.FileUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

/**
 * LogFilePath represents path of a log file.  It contains convenience method for building and
 * decomposing paths.
 *
 * Log file path has the following form:
 *     prefix/topic/partition1/.../partitionN/generation_kafkaParition_firstMessageOffset
 * where:
 *     prefix is top-level directory for log files.  It can be a local path or an s3 dir,
 *     topic is a kafka topic,
 *     partition1, ..., partitionN is the list of partition names extracted from message content.
 *         E.g., the partition may describe the message date such as dt=2014-01-01,
 *     generation is the consumer version.  It allows up to perform rolling upgrades of
 *         non-compatible Secor releases,
 *     kafkaPartition is the kafka partition of the topic,
 *     firstMessageOffset is the offset of the first message in a batch of files committed
 *         atomically.
 *
 * @author Pawel Garbacki (pawel@pinterest.com)
 */
public class LogFilePath {
    protected static final String PATH_DELIMITER = "/";
    protected static final String FILENAME_DELIMITER = "_";
    private String mPrefix;
    private String mTopic;
    private Components mComponents;
    private int mGeneration;
    private int mKafkaPartition;
    private long mOffset;
    private String mExtension;

    public LogFilePath(String prefix, String topic, int partition, Components components, int generation, long offset, String extension) {
        this.mPrefix = prefix;
        this.mTopic = topic;
        this.mKafkaPartition = partition;
        this.mComponents = components;
        this.mGeneration = generation;
        this.mOffset = offset;
        this.mExtension = extension;
    }

    public String getLogFileDir() {
        List<String> elements = new ArrayList<String>();
        elements.add(getLogFileParentDir());
        for (String component : mComponents.getPath()) {
          elements.add(component);
        }
        return StringUtils.join(elements, "/");
    }

    public String getLogFilePath() {
        List<String> pathElements = new ArrayList<String>();
        pathElements.add(getLogFileDir());
        pathElements.add(getLogFileBasename());
        return StringUtils.join(pathElements, "/") + mExtension;
    }

    public String getLogFileCrcPath() {
        String basename = "." + getLogFileBasename() + ".crc";
        List<String> pathElements = new ArrayList<String>();
        pathElements.add(getLogFileDir());
        pathElements.add(basename);
        return StringUtils.join(pathElements, "/");
    }

    public void delete() throws IOException {
        FileUtil.delete(getLogFileCrcPath());
        FileUtil.delete(getLogFilePath());
    }

    public String getPrefix() {
        return mPrefix;
    }

    public String getTopic() {
        return mTopic;
    }

    public Components getComponents() {
        return mComponents;
    }

    public int getGeneration() {
        return mGeneration;
    }

    public int getKafkaPartition() {
        return mKafkaPartition;
    }

    public long getOffset() {
        return mOffset;
    }

    public String getExtension() {
        return mExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogFilePath that = (LogFilePath) o;

        if (mGeneration != that.mGeneration) return false;
        if (mKafkaPartition != that.mKafkaPartition) return false;
        if (mOffset != that.mOffset) return false;
        if (!mComponents.equals(that.mComponents)) return false;
        if (mPrefix != null ? !mPrefix.equals(that.mPrefix) : that.mPrefix != null) return false;
        if (mTopic != null ? !mTopic.equals(that.mTopic) : that.mTopic != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mPrefix != null ? mPrefix.hashCode() : 0;
        result = 31 * result + (mTopic != null ? mTopic.hashCode() : 0);
        result = 31 * result + (mComponents != null ? mComponents.hashCode() : 0);
        result = 31 * result + mGeneration;
        result = 31 * result + mKafkaPartition;
        result = 31 * result + (int) (mOffset ^ (mOffset >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return getLogFilePath();
    }

    protected String getPathDelimiter() {
        return PATH_DELIMITER;
    }

    protected String getFilenameDelimiter() {
        return FILENAME_DELIMITER;
    }

    protected String getLogFileBasename() {
        List<String> basenameElements = new ArrayList<String>();
        for (String component : mComponents.getFilename()) {
          basenameElements.add(component);
        }
        basenameElements.add(Integer.toString(mKafkaPartition));
        basenameElements.add(String.format("%020d", mOffset));
        return StringUtils.join(basenameElements, getFilenameDelimiter());
    }

    protected String getLogFileParentDir() {
        List<String> elements = new ArrayList<String>();
        elements.add(mPrefix);
        return StringUtils.join(elements, "/");
    }
}
