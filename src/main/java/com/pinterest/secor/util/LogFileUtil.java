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
package com.pinterest.secor.util;

import com.pinterest.secor.common.Components;
import com.pinterest.secor.common.LogFilePath;
import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.io.FileReaderWriterFactory;

import java.util.Arrays;

public class LogFileUtil {
    public static LogFilePath createFromPath(String prefix, String path, SecorConfig config) throws Exception {
        FileReaderWriterFactory factory = ReflectionUtil.createFileReaderWriterFactory(config.getFileReaderWriterFactory());
        LogFilePath logFilePath = createFromPath(prefix, path);
        return factory.BuildLogFilePath(logFilePath.getPrefix(),
            logFilePath.getTopic(),
            logFilePath.getKafkaPartition(),
            logFilePath.getComponents(),
            logFilePath.getGeneration(),
            logFilePath.getOffset(),
            logFilePath.getExtension());
    }

    public static LogFilePath createFromPath(String prefix, String path) {
        assert path.startsWith(prefix): path + ".startsWith(" + prefix + ")";

        int prefixLength = prefix.length();
        if (!prefix.endsWith("/")) {
            prefixLength++;
        }
        String suffix = path.substring(prefixLength);
        String[] pathElements = suffix.split("/");
        // Suffix should contain a topic, at least one partition, and the basename.
        assert pathElements.length >= 3: Arrays.toString(pathElements) + ".length >= 3";

        String topic = pathElements[0];
        String[] pathComponents = Arrays.copyOfRange(pathElements, 1, pathElements.length - 1);
        String extension;

        // Parse basename.
        String basename = pathElements[pathElements.length - 1];
        // Remove extension.
        int lastIndexOf = basename.lastIndexOf('.');
        if (lastIndexOf >= 0) {
            extension = basename.substring(lastIndexOf, basename.length());
            basename = basename.substring(0, lastIndexOf);
        } else {
            extension = "";
        }
        String[] basenameElements = basename.split("_");
        assert basenameElements.length == 3: Integer.toString(basenameElements.length) + " == 3";
        int generation = Integer.parseInt(basenameElements[0]);
        int kafkaPartition = Integer.parseInt(basenameElements[1]);
        long offset = Long.parseLong(basenameElements[2]);
        Components components = new Components(pathComponents, topic, generation);
        return new LogFilePath(prefix, topic, kafkaPartition, components, generation, offset, extension);
    }
}
