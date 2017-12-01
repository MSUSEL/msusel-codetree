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
package edu.montana.gsoc.msusel.codetree.relations;

/**
 * An enumeration of the different types of relationships allowed in the
 * CodeTree.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public enum RelationshipType {

    /**
     * A simple relationship indicating that the source depends on the dest in
     * some way. For example in a Use-Dependency, the source type has a method
     * whose return type or parameter is of the dest type.
     */
    Dependency,
    /**
     * A relationship indicating a HAS-A relationship. A
     * source type is associated to a dest type if the source type has a field
     * of type dest type.
     */
    Association,
    /**
     * A relationship indicating a IS-A relationship. Here the source type is an
     * extension of the dest type, indicating that the source specializes the
     * dest and the dest generalizes the source. Here the dest type and source
     * type must both be classes or both be interfaces.
     */
    Generalization,
    /**
     * A relationship indicating a IS-A relationship. Here the dest type must be
     * an interface and the source type a class. We then say that the source
     * type realizes the interface of the dest type.
     * an interface
     */
    Realization,
}
