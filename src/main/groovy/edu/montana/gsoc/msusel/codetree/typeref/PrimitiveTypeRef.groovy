/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
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
package edu.montana.gsoc.msusel.codetree.typeref

import com.google.gson.annotations.Expose
import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Builder(buildMethodName = "create")
class PrimitiveTypeRef extends AbstractTypeRef {

    @Expose
    String key

    static PrimitiveTypeRef getInstance(String key) {
        MultitonHolder.map[key]
    }

    static def getTypes() {
        MultitonHolder.map.values()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String name() {
        key
    }

    @Override
    String type() {
        return key
    }

    private static class MultitonHolder {
        private static def map = [:]

        static {
            // Shared Java and C# Primitive Types
            map["int"] = builder().key("int").create()
            map["long"] = builder().key("long").create()
            map["char"] = builder().key("char").create()
            map["byte"] = builder().key("byte").create()
            map["short"] = builder().key("short").create()
            map["double"] = builder().key("double").create()
            map["float"]  = builder().key("float").create()
            map["void"] = builder().key("void").create()

            // Java Primitive Types
            map["boolean"] = builder().key("boolean").create()

            // C# Primitive Types
            map["sbyte"] = builder().key("sbyte").create()
            map["ushort"] = builder().key("ushort").create()
            map["uint"] = builder().key("uint").create()
            map["ulong"] = builder().key("ulong").create()
            map["object"] = builder().key("object").create()
            map["string"] = builder().key("string").create()
            map["decimal"] = builder().key("decimal").create()
            map["bool"] = builder().key("bool").create()
            map["DataTime"] = builder().key("DateTime").create()
            map["DateSpan"] = builder().key("DateSpan").create()
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String toString() {
        key
    }
}
