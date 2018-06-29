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
package edu.montana.gsoc.msusel.datamodel

import com.google.gson.annotations.Expose
import groovy.transform.builder.Builder

import javax.persistence.Embeddable
import javax.persistence.Embedded
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Embeddable
@Builder(buildMethodName = "create", excludes = ["bounds, typeArgs"])
class TypeReference {

    @Expose
    String dimensions
    @Embedded
    List<TypeReference> bounds = []
    Reference ref
    String typeName
    @Embedded
    List<TypeReference> typeArgs = []
    TypeRefType type

    static TypeReference createPrimitiveTypeRef(String key) {
        KnownTypeMultiton.map[key]
    }

    static TypeReference createWildCardTypeRef() {
        KnownTypeMultiton.map["?"]
    }

    static TypeReference createTypeVarTypeRef(String typeVar) {
        builder().typeName(typeVar).create()
    }

    static def knownTypes() {
        KnownTypeMultiton.map.values()
    }

    boolean isKnownType() {
        KnownTypeMultiton.map.containsKey(typeName)
    }

    private static class KnownTypeMultiton {
        public static def map = [:]

        static {
            // Shared Java and C# Primitive Types
            map["int"] = builder().typeName("int").type(TypeRefType.PRIMITIVE).create()
            map["long"] = builder().typeName("long").type(TypeRefType.PRIMITIVE).create()
            map["char"] = builder().typeName("char").type(TypeRefType.PRIMITIVE).create()
            map["byte"] = builder().typeName("byte").type(TypeRefType.PRIMITIVE).create()
            map["short"] = builder().typeName("short").type(TypeRefType.PRIMITIVE).create()
            map["double"] = builder().typeName("double").type(TypeRefType.PRIMITIVE).create()
            map["float"]  = builder().typeName("float").type(TypeRefType.PRIMITIVE).create()
            map["void"] = builder().typeName("void").type(TypeRefType.PRIMITIVE).create()

            // Java Primitive Types
            map["boolean"] = builder().typeName("boolean").type(TypeRefType.PRIMITIVE).create()

            // C# Primitive Types
            map["sbyte"] = builder().typeName("sbyte").type(TypeRefType.PRIMITIVE).create()
            map["ushort"] = builder().typeName("ushort").type(TypeRefType.PRIMITIVE).create()
            map["uint"] = builder().typeName("uint").type(TypeRefType.PRIMITIVE).create()
            map["ulong"] = builder().typeName("ulong").type(TypeRefType.PRIMITIVE).create()
            map["object"] = builder().typeName("object").type(TypeRefType.PRIMITIVE).create()
            map["string"] = builder().typeName("string").type(TypeRefType.PRIMITIVE).create()
            map["decimal"] = builder().typeName("decimal").type(TypeRefType.PRIMITIVE).create()
            map["bool"] = builder().typeName("bool").type(TypeRefType.PRIMITIVE).create()
            map["DataTime"] = builder().typeName("DateTime").type(TypeRefType.PRIMITIVE).create()
            map["DateSpan"] = builder().typeName("DateSpan").type(TypeRefType.PRIMITIVE).create()

            map["?"] = builder().typeName("?").type(TypeRefType.WILDCARD).create()
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String toString() {
        StringBuilder builder = new StringBuilder()
        builder << "${name()} "

        if (bounds) {
            builder << "extends "
            bounds.each {
                builder << it.toString()
                if (it != bounds.last())
                    builder << " & "
            }
        } else {
            if (typeArgs != null && !typeArgs.empty) {
                builder << "<"
                typeArgs.each { arg ->
                    builder << "${arg.toString()}"
                    if (arg != typeArgs.last())
                        builder << ", "
                }
                builder << ">"
            }
            builder << " ${dimensions}"
        }
        builder.toString()
    }

    void addBound(TypeReference ref) {
        if (bounds == null)
            bounds = []
        if (ref != null)
            bounds << ref
    }

    void addTypeArg(TypeReference ref) {
        if (typeArgs == null)
            typeArgs = []

        if (ref != null)
            typeArgs << ref
    }
}
