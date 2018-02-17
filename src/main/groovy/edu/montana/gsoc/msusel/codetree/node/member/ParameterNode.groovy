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
package edu.montana.gsoc.msusel.codetree.node.member

import com.google.gson.annotations.Expose
import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import edu.montana.gsoc.msusel.codetree.INode
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.CodeNode
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ParameterNode extends CodeNode {

    @Expose
    AbstractTypeRef type
    @Expose
    boolean array
    
    /**
     * 
     */
    @Builder(buildMethodName = "create")
    ParameterNode(String key, String parentKey, Map<String, Double> metrics = [:],
                  Accessibility accessibility = Accessibility.PUBLIC, specifiers = [],
                  int start, int end, AbstractTypeRef type, boolean array = false) {
        super(key, parentKey, metrics, accessibility, specifiers, start, end)
        this.type = type
        this.array = array
    }

    def name() {
        key
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type()
    {
        type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode other)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    INode cloneNoChildren()
    {
        // TODO Auto-generated method stub
        null
    }

    def extractTree(tree) {
        null
    }
}
