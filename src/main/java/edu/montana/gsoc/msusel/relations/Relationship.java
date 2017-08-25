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
package edu.montana.gsoc.msusel.relations;

import edu.montana.gsoc.msusel.INode;

/**
 * A basic relationship class used to connect entities within a code tree
 * outside the confines of the containment relationships inherent to the tree
 * structure.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class Relationship {

    /**
     * The type of this relationship
     */
    private RelationshipType type;

    /**
     * The node acting as the start point of this relationship
     */
    private INode source;
    /**
     * The node acting as the end point of this relationship
     */
    private INode dest;

    /**
     * Constructs a new relationship between the source and dest with the given
     * type.
     * 
     * @param source
     *            Source Node
     * @param dest
     *            Dest Node
     * @param type
     *            Type
     */
    public Relationship(INode source, INode dest, RelationshipType type)
    {
        this.source = source;
        this.dest = dest;
        this.type = type;
    }

    /**
     * @return The type of this relationship
     */
    public RelationshipType getType()
    {
        return type;
    }

    /**
     * @return The source of this relationship
     */
    public INode getSource()
    {
        return source;
    }

    /**
     * @return The destination of this relationship
     */
    public INode getDest()
    {
        return dest;
    }
}
