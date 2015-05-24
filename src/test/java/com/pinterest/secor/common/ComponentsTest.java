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

import org.junit.Test;
import java.lang.IllegalArgumentException;
import static org.junit.Assert.*;

/**
 * ComponentsTest tests the logic of Components.
 *
 * @author Mathias Söderberg (mathias@burtcorp.com)
 */
public class ComponentsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureNotNullPath() throws Exception {
        String[] filenameParts = new String[] { "file", "name" };
        Components components = new Components(null, filenameParts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureNotEmptyPath() throws Exception {
        String[] filenameParts = new String[] { "file", "name" };
        Components components = new Components(new String[] {}, filenameParts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureNotNullFilename() {
        String[] pathParts = new String[] { "path", "to", "logs" };
        Components components = new Components(pathParts, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureNotEmptyFilename() {
        String[] pathParts = new String[] { "path", "to", "logs" };
        Components components = new Components(pathParts, new String[] {});
    }

    @Test
    public void testHashCode() {
        String[] filenameParts = new String[] { "file", "name" };
        String[] pathParts = new String[] { "path", "to", "logs" };
        Components c1 = new Components(pathParts, filenameParts);
        Components c2 = new Components(pathParts, filenameParts);
        assertEquals(c1.hashCode(), c2.hashCode());
        c2 = new Components(pathParts, new String[] { "other", "file", "name" });
        assertTrue(c1.hashCode() != c2.hashCode());
        c2 = new Components(new String[] { "other", "path", "to", "logs" }, filenameParts);
        assertTrue(c1.hashCode() != c2.hashCode());
    }

    @Test
    public void testEquals() {
        String[] filenameParts = new String[] { "file", "name" };
        String[] pathParts = new String[] { "path", "to", "logs" };
        Components c1 = new Components(pathParts, filenameParts);
        Components c2 = new Components(pathParts, filenameParts);
        assertEquals(c1, c2);
        c2 = new Components(pathParts, new String[] { "other", "file", "name" });
        assertTrue(!c1.equals(c2));
        c2 = new Components(new String[] { "other", "path", "to", "logs" }, filenameParts);
        assertTrue(!c1.equals(c2));
    }
}
