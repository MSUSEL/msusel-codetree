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

package codetree.node.type

import codetree.INode
import codetree.node.Accessibility
import codetree.node.member.FieldNode
import codetree.node.member.MethodNode
import codetree.node.structural.NamespaceNode
import codetree.typeref.TypeVarTypeRef
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ClassNode extends TypeNode implements Cloneable {

    /**
     *
     */
    @Builder(buildMethodName='create')
    ClassNode(String key, String parentKey, Map<String, Double> metrics = [:],
              Accessibility accessibility = Accessibility.DEFAULT, specifiers = [],
              int start, int end, List<TypeVarTypeRef> templateParams, NamespaceNode namespace) {
        super(key, parentKey, metrics, accessibility, specifiers, start, end, templateParams, namespace)
    }

    MethodNode findMethod(String key) {
        methods().find { MethodNode m -> m.key == key}
    }

    boolean hasMethod(String key) {
        findMethod(key) != null
    }

    FieldNode findField(String key) {
        fields().find { FieldNode f -> f.key == key}
    }

    boolean hasField(String key) {
        findField(key) != null
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isInterface()
    {
        false
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type() { "Class" }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode other)
    {
        if (other == null)
            return

        if (!(other instanceof ClassNode))
            return

        ClassNode type = (ClassNode) other

        start = type.start
        end = type.end

        type.methods().each { MethodNode m ->
            hasMethod(m.key) ? findMethod(m.key).update(m) : children << m
        }

        type.fields().each { FieldNode f ->
            hasField(f.key) ? findField(f.key).update(f) : children << f
        }

        type.getMetricNames().each { name ->
            this.metrics[name] = type.metrics[name]
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected ClassNode clone() throws CloneNotSupportedException
    {
        ClassNode tnode = (ClassNode) cloneNoChildren()

        children.each { tnode.children << it.clone() }

        return tnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    INode cloneNoChildren()
    {
        ClassNode cnode = builder().key(this.key).parentKey(parentKey).start(start).end(end).create()

        copyMetrics(cnode)

        return cnode
    }

    @Override
    def generatePlantUML() {
        StringBuilder builder = new StringBuilder()
        builder.append("class ${this.name()} {\n")
        fields().each { FieldNode f -> builder.append("${f.plantUML()}\n")}
        methods().each { MethodNode m -> builder.append("${m.accessibility.plantUML}${m.name()}() : ${m.getType()}\n") }
        builder.append("}\n")
        builder.append("\n")

        builder.toString()
    }
}
