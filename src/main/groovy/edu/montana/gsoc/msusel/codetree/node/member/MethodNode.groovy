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
package edu.montana.gsoc.msusel.codetree.node.member

import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.INode
import edu.montana.gsoc.msusel.codetree.cfg.ControlFlowGraph
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.Modifiers
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.ModuleNode
import edu.montana.gsoc.msusel.codetree.node.structural.ProjectNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.TypeVarTypeRef
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils
import groovy.transform.builder.Builder

/**
 * An abstraction of a type method. A method's unique qualified name is similar
 * to a fields but contains a parenthesized listing of the parameter types
 * associated with the method. Each method contains the following: a set of
 * parameters, and a set of statements. A method also has a return type
 * associated with it. Each parameter is simply a pair consisting of a type and
 * a name.
 *
 * @author Isaac Griffith
 * @version 1.2.0
 */
class MethodNode extends MemberNode {

    def params = []
    def typeParams = []
    ControlFlowGraph cfg
    def statements = []
    def exceptions = []

    /**
     *
     */
    @Builder(buildMethodName = 'create')
    MethodNode(String key, String parentKey, Map<String, Double> metrics = [:],
               Accessibility accessibility = Accessibility.DEFAULT, specifiers = [],
               int start, int end, AbstractTypeRef type, params = [], typeParams = [], statements = []) {
        super(key, parentKey, metrics, accessibility, specifiers, start, end, type)
        this.params = params
        this.typeParams = typeParams
        this.statements = statements
    }

    def addException(AbstractTypeRef ref) {
        if (ref != null && !exceptions.contains(ref)) {
            exceptions << ref
        }
    }

    def hasModifier(String mod) {
        modifiers.contains(Modifiers.valueOf(mod.toLowerCase()))
    }

    def isOverriding(TypeNode owner, CodeTree tree) {
        TypeNode current = owner
        def parents = []
        while (!tree.getGeneralizedFrom(current).isEmpty()) {
            current = (TypeNode) tree.getGeneralizedFrom(current).first()
            parents << current
        }

        parents.each { p ->
            if (p.hasMethodWithMatchingSignature(this))
                return true
        }

        return false
    }

    String signature() {
        String sig = getType().name() + " " + name() + "("
        params.each { ParameterNode param ->
            sig += param.getType().name() + ", "
        }
        if (sig.endsWith(", "))
            sig = sig.trim()[0..-2]
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

        for (String key : m.getMetricNames()) {
            this.metrics[key] = m.getMetric(key)
        }

        node.statements.each { StatementNode stmt ->
            if (findStatement(stmt.key) != null) {
                findStatement(stmt.key).update(stmt)
            } else {
                statements << stmt
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
    StatementNode findStatement(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new IllegalArgumentException("Statment identifier cannot be null or empty")

        (StatementNode) statements.find { it.key == identifier }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    MethodNode cloneNoChildren() {
        MethodNode mnode = builder()
                .key(this.key)
                .parentKey(parentKey)
                .params(params)
                .create()

        copyMetrics(mnode)

        mnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    MethodNode clone() throws CloneNotSupportedException {
        cloneNoChildren()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type() {
        "Method"
    }

    def extractTree(tree) {
        CodeTreeUtils utils = tree.utils
        TypeNode type = utils.findType(parentKey)

        FileNode file = utils.findFile(type.parentKey)

        def retVal = new CodeTree()
        Stack<ProjectNode> stack = new Stack<>()
        ProjectNode project
        ModuleNode module

        if (utils.findProject(file.parentKey) != null) {
            project = utils.findProject(file.parentKey)
        } else {
            module = utils.findModule(file.parentKey)
            project = utils.findProject(((ModuleNode) module).parentKey)
        }

        stack.push(project)
        while (project.hasParent()) {
            project = utils.findProject(project.parentKey)
            stack.push(project)
        }

        ProjectNode current = stack.pop().cloneNoChildren()
        ProjectNode root = current
        ProjectNode next
        while (!stack.isEmpty()) {
            next = stack.pop().cloneNoChildren()

            current.children << next
            current = next
        }

        ProjectNode currentProject = utils.findProject(current.key)

        file = ((FileNode) utils.findFile(type.parentKey)).cloneNoChildren()

        currentProject.children << file

        type = (TypeNode) type.cloneNoChildren()
        file.children << type

        try {
            type.children << clone()
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace()
        }

        retVal.setProject(root)
    }

    def getParameterByName(String param) { params.find { it.name() == param } }

    TypeVarTypeRef getTypeParamByName(String paramName) {
        typeParams.find { it.name() == paramName }
    }
}
