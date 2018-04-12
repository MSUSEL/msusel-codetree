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
package edu.montana.gsoc.msusel.codetree.node

import com.google.gson.annotations.Expose
import edu.montana.gsoc.msusel.codetree.INode
import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Slf4j

/**
 * The Abstract Class for all Nodes in a CodeTree
 * 
 * @author Isaac Griffith
 * @version 1.2.0
 */
@EqualsAndHashCode(includeFields=true, excludes=["children","metrics"])
@Slf4j
abstract class AbstractNode implements INode {

    /**
     * The collection of children of this node
     */
    @Expose
    def children = []
    /**
     * The unique identifying key of this node
     */
    @Expose
    String key
    /**
     * The unique identifier of the parent node or null if there is no parent
     */
    @Expose
    String parentKey
    /**
     * flag indicating that the node's metrics have been aggregated
     */
    @Expose
    boolean aggregated = false

    AbstractNode(String key, String parentKey) {
        this.key = key
        this.parentKey = parentKey
        aggregated = false
    }

    /**
     * {@inheritDoc}
     */
    @Override
    abstract INode cloneNoChildren()

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasParent() {
        parentKey != null
    }

    def leftShift(CodeNode node) {
        addChild(node)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def addChild(node) {
        children << node
        node.setParentKey(this.key)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def removeChild(node) {
        if (children.contains(node)) {
            children.remove(node)
        }
    }
}