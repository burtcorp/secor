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

import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import java.util.Arrays;

/**
 * Components are used to construct the actual paths used by LogFilePath implementations.
 *
 * @author Mathias Söderberg (mathias@burtcorp.com)
 */
public class Components {
    private String[] mPath;
    private String[] mFilename;

    // convenience constructor
    public Components(String[] path, String topic, int generation) {
        this(ObjectArrays.concat(topic, path), new String[] { Integer.toString(generation) });
    }

    public Components(String[] path, String[] filename) {
        Preconditions.checkArgument(path != null && path.length > 0, "Path components cannot be null or empty");
        Preconditions.checkArgument(filename != null && filename.length > 0, "Filename components cannot be null or empty");
        this.mPath = path;
        this.mFilename = filename;
    }

    public String[] getPath() {
        return mPath;
    }

    public String[] getFilename() {
        return mFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Components that = (Components) o;
        return Arrays.equals(mPath, that.getPath()) && Arrays.equals(mFilename, that.getFilename());
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + Arrays.hashCode(mPath);
        result = 31 * result + Arrays.hashCode(mFilename);
        return result;
    }

    @Override
    public String toString() {
        return "Components{" + Arrays.toString(mPath) + ", " +
          Arrays.toString(mFilename) + "}";
    }
}
