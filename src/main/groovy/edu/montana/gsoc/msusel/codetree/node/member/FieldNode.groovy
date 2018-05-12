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

import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import edu.montana.gsoc.msusel.codetree.INode
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class FieldNode extends MemberNode implements Cloneable {

    /**
     *
     */
    @Builder(buildMethodName = "create")
    FieldNode(String key, String parentKey, Accessibility accessibility = Accessibility.DEFAULT, specifiers = [],
              int start, int end, AbstractTypeRef type) {
        super(key, parentKey, accessibility, specifiers, start, end, type)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type() {
        "Field"
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def name() {
        key.split("#").last()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode other) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FieldNode clone() throws CloneNotSupportedException {
        cloneNoChildren()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    FieldNode cloneNoChildren() {
        FieldNode fnode = builder()
                .key(this.key)
                .start(start)
                .end(end)
                .create()

        copyMetrics(fnode)

        fnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def extractTree(tree) {

    }

    @Override
    def findParent(CodeTreeUtils utils) {
        utils.findType(getParentKey())
    }

    def plantUML() {
        "${this.accessibility.getPlantUML()} ${name()} : ${type.toString()}"
    }
}
