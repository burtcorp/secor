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

import com.twitter.ostrich.stats.Stats;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricName;
import java.util.concurrent.TimeUnit;

/**
 * Utilities to interact with Ostrich stats exporter.
 *
 * @author Pawel Garbacki (pawel@pinterest.com)
 */
public class StatsUtil {
    public static void setLabel(String name, String value) {
        long threadId = Thread.currentThread().getId();
        name += "." + threadId;
        Stats.setLabel(name, value);
    }

    public static void clearLabel(String name) {
        long threadId = Thread.currentThread().getId();
        name += "." + threadId;
        Stats.clearLabel(name);
    }

    public static void incr(String name) {
      Stats.incr(name);
    }

    public static Meter newMeter(String group, String type, String name, String eventType) {
        return StatsUtil.newMeter(group, type, name, eventType, null);
    }

    public static Meter newMeter(String group, String type, String name, String eventType, String scope) {
        String mbeanName = createMBeanName(group, type, name, scope);
        MetricName metricName = new MetricName(group, type, name, scope, mbeanName);
        return Metrics.newMeter(metricName, eventType, TimeUnit.SECONDS);
    }

    public static String createMBeanName(String group, String type, String name, String scope) {
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(group);
        nameBuilder.append(":type=");
        nameBuilder.append(type);
        if (scope != null) {
            nameBuilder.append(",scope=");
            nameBuilder.append(scope);
        }
        if (name.length() > 0) {
            nameBuilder.append(",name=");
            nameBuilder.append(name);
        }
        return nameBuilder.toString();
    }
}
