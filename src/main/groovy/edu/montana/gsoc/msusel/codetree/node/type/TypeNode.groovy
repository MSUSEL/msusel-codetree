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
package edu.montana.gsoc.msusel.codetree.node.type

import com.google.gson.annotations.Expose
import edu.montana.gsoc.msusel.codetree.DefaultCodeTree
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.CodeNode
import edu.montana.gsoc.msusel.codetree.node.Modifiers
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.InitializerNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.PropertyNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.ModuleNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.ProjectNode
import edu.montana.gsoc.msusel.codetree.typeref.TypeVarTypeRef
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils

import javax.persistence.Entity

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Entity
abstract class TypeNode extends CodeNode {

    @Expose
    List<TypeVarTypeRef> templateParams = []
    @Expose
    NamespaceNode namespace

    TypeNode(String key, String parentKey, Accessibility accessibility = Accessibility.DEFAULT, specifiers = [],
             int start, int end, List<TypeVarTypeRef> templateParams = [], NamespaceNode namespace) {
        super(key, parentKey, accessibility, specifiers, start, end)
        this.templateParams = templateParams
        this.namespace = namespace
    }

    void addTypeParam(TypeVarTypeRef node) {
        if (templateParams == null)
            templateParams = []

        if (node != null || !templateParams.contains(node))
            templateParams << node
    }

    def methods() {
        children.findAll {
            it instanceof MethodNode
        }
    }

    def fields() {
        children.findAll {
            it instanceof FieldNode
        }
    }

    def getField(String name) {
        fields().find { it.name() == name }
    }

    def properties() {
        children.findAll {
            it instanceof PropertyNode
        }
    }

    boolean isStatic() {
        modifiers.contains(Modifiers.STATIC)
    }

    boolean isAbstract() {
        modifiers.contains(Modifiers.ABSTRACT)
    }

    abstract boolean isInterface()

    /**
     * {@inheritDoc}
     */
    @Override
    def name() {
        key.split("\\.").last()
    }

    MethodNode findMethod(final int line) {
        if (line < getStart() || line > getEnd())
            return null

        methods().find { MethodNode m -> m.containsLine(line) }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def extractTree(tree) {
        TypeNode type = (TypeNode) node

        FileNode file = findFile(type.getParentKey())

        retVal = new DefaultCodeTree()
        Stack<ProjectNode> stack = new Stack<>()
        ProjectNode project
        ModuleNode module

        if (findProject(file.getParentKey()) != null) {
            project = findProject(file.getParentKey())
        } else {
            module = findModule(file.getParentKey())
            project = findProject(module.getParentKey())
        }

        stack.push(project)
        while (project.hasParent()) {
            project = findProject(project.getParentKey())
            stack.push(project)
        }

        ProjectNode current = stack.pop().cloneNoChildren()
        ProjectNode root = current
        ProjectNode next
        while (!stack.isEmpty()) {
            next = stack.pop().cloneNoChildren()

            current.addSubProject(next)
            current = next
        }

        ProjectNode currentProject = findProject(current.getQIdentifier())

        file = currentProject.getFile(type.getParentKey()).cloneNoChildren()

        currentProject.addFile(file)

        file.addType(type.cloneNoChildren())

        retVal.setProject(root)
    }

    @Override
    def findParent(CodeTreeUtils utils) {
        utils.findFile(getParentKey())
    }

    abstract def generatePlantUML()

    def getMethod(String name) {
        methods().find { it.name() == name }
    }

    def getAllMethodsByName(String name) {
        methods().findAll { it.name() == name }
    }

    TypeVarTypeRef getTypeParamByName(String param) {
        templateParams.find { it.name() == param }
    }

    boolean hasTypeParam(String name) {
        templateParams.find { it.name() == name } != null
    }

    MethodNode findMethodBySignature(String sig) {
        methods().find { MethodNode m -> m.signature() == sig }
    }

    InitializerNode getStaticInitializer(int i) {
        methods().find { it.name() == '<static_init$' + i + '>' }
    }

    InitializerNode getInstanceInitializer(int i) {
        methods().find { it.name() =~ /^<init\$${i}>$/ }
    }

    boolean hasMethod(MethodNode node) {
        methods().find { it == node } != null
    }
}