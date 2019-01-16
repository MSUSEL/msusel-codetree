/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum Accessibility {
    PUBLIC(1),
    PROTECTED(2),
    PRIVATE(3),
    DEFAULT(4),
    PACKAGE(5),
    INTERNAL(6),
    PROTECTED_INTERNAL(7);

    private final int value;
    private static Map<Integer, Accessibility> map = Maps.newHashMap();

    static {
        map.put(1, PUBLIC);
        map.put(2, PROTECTED);
        map.put(3, PRIVATE);
        map.put(4, DEFAULT);
        map.put(5, PACKAGE);
        map.put(6, INTERNAL);
        map.put(7, PROTECTED_INTERNAL);
    }

    Accessibility(int value) { this.value = value; }

    public int value() { return value; }

    public static Accessibility fromValue(int value) {
        return map.get(value);
    }
}