/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
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
package codetree.typeref

import codetree.AbstractTypeRef
import codetree.node.type.TypeNode
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Builder(buildMethodName = "create")
class TypeRef extends AbstractTypeRef {

    TypeNode type
    def typeArgs = []

    String toString() {
        String args = ""
        if (typeArgs != null && !typeArgs.empty) {
            args = "<"
            typeArgs.each {arg -> args + "${arg.toString()}, "}
            args = args.substring(0, args.lastIndexOf(","))
            args += ">"
        }
        "${type.name()} ${args}"
    }

    void addTypeArg(AbstractTypeRef ref) {
        if (typeArgs == null)
            typeArgs = []

        if (ref != null)
            typeArgs << ref
    }

    @Override
    String name() {
        type.name()
    }
}
