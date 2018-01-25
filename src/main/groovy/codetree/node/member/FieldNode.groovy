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
package codetree.node.member

import codetree.AbstractTypeRef
import codetree.node.Accessibility
import codetree.INode
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
    FieldNode(String key, String parentKey, Map<String, Double> metrics = [:],
              Accessibility accessibility = Accessibility.PUBLIC, specifiers = [],
              int start, int end, AbstractTypeRef type) {
        super(key, parentKey, metrics, accessibility, specifiers, start, end, type)
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

    def extractTree(tree) {

    }

    def plantUML() {
        "${this.accessibility.getPlantUML()} ${name()} : ${type.toString()}"
    }
}
