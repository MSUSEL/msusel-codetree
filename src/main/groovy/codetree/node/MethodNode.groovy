/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
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
/**
 * 
 */
package codetree.node

import codetree.Accessibility
import codetree.CodeTree
import codetree.INode
import codetree.Modifiers
import com.google.common.collect.Lists
import com.google.common.graph.Graph

/**
 * An abstraction of a type method. A method's unique qualified name is similar
 * to a fields but contains a parenthesized listing of the parameter types
 * associated with the method. Each method contains the following: a set of
 * parameters, and a set of statements. A method also has a return type
 * associated with it. Each parameter is simply a pair consisting of a type and
 * a name.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
class MethodNode extends MemberNode {

    def params = []
    def templateParams = []
    Graph<StatementNode> cfg

    /**
     * 
     */
    public MethodNode() {
        type = PrimitiveTypeNode.getInstance("void")
        accessibility = Accessibility.PUBLIC
    }

    def isAbstract() {
        specifiers.contains(Modifiers.ABSTRACT)
    }

    def isStatic() {
        specifiers.contains(Modifiers.STATIC)
    }

    def isFinal() {
        specifiers.contains(Modifiers.FINAL)
    }

    def isSynchronized() {
        specifiers.contains(Modifiers.SYNCHRONIZED)
    }

    def isAccessor() {
    }

    def isMutator() {
    }

    def isOverriding(TypeNode owner, CodeTree tree) {
        TypeNode current = owner
        def parents = []
        while (!tree.getGeneralizedFrom(current).isEmpty()) {
            current = tree.getGeneralizedFrom(current).first()
            parents << current
        }

        parents.each { p ->
            if (p.hasMethodWithMatchingSignature(this))
                return true
        }

        return false
    }

    def signature() {
        String sig = key + "("
        params.each {
            sig += it.getKey() + ", "
        }
        sig = sig.trim().chomp(",")
        sig += ")"
        sig
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode m) {
        if (m == null)
            return

        if (!(m instanceof MethodNode))
            return

        MethodNode node = (MethodNode) m
        this.setRange(node.getStart(), node.getEnd())

        setConstructor(node.isConstructor())

        for (String key : m.getMetricNames()) {
            this.metrics.put(key, m.getMetric(key))
        }

        for (StatementNode stmt : node.getStatements()) {
            if (getStatement(stmt.getQIdentifier()) != null) {
                getStatement(stmt.getQIdentifier()).update(stmt)
            }
            else {
                addStatement(stmt)
            }
        }
    }

    /**
     * Returns a statement with the given identifier contained in this method
     *
     * @param identifier
     *            Identifier of the statement requested
     * @return StatementNode contained in this method with the given identifier,
     *         or null otherwise
     * @throws IllegalArgumentException
     *             if provided identifier is null or the empty string
     */
    public StatementNode getStatement(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new IllegalArgumentException("Statment identifier cannot be null or empty")

        return statements.get(identifier)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode cloneNoChildren() {
        MethodNode mnode = new MethodNode(this.qIdentifier, this.name)
        mnode.setConstructor(this.isConstructor())
        mnode.setAbstract(this.isAbstract())
        mnode.setAccessorMethod(this.isAccessorMethod())
        mnode.setRange(this.getStart(), this.getEnd())

        copyMetrics(mnode)

        return mnode
    }

    /**
     * @return The set of statements contained within this method.
     */
    def getStatements() {
        return Lists.newArrayList(cfg.nodes())
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode clone() throws CloneNotSupportedException
    {
        return cloneNoChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type()
    {
        "Method"
    }
}
