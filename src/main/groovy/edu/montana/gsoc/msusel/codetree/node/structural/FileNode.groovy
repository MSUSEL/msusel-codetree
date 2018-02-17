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
package edu.montana.gsoc.msusel.codetree.node.structural

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.INode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class FileNode extends StructuralNode {

    /**
     *
     */
    @Builder(buildMethodName = 'create')
    FileNode(String key, String parentKey, Map<String, Double> metrics) {
        super(key, parentKey, metrics)
    }

    def name() {
        key
    }

    def type() {
        "File"
    }

    def imports() {
        children.findAll {
            it instanceof ImportNode
        }
    }

    def types() {
        children.findAll {
            it instanceof TypeNode
        }
    }

    def files() {
        null
    }

    /**
     * Retrieves the method containing the given line from a type contained in
     * this file.
     *
     * @param line
     *            Line number for which a method is requested.
     * @return Qualified identifier of the method at the given line, or the
     *         empty string if no method exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    MethodNode findMethod(final int line) {
        TypeNode parent = findType(line)

        parent?.findMethod(line)
    }

    /**
     * Retrieves the qualified identifier of the type in this file which
     * contains the given line.
     *
     * @param line
     *            Line number for which a type is requested
     * @return Qualified identifier of the type at the given line, or the
     *         empty string if no type exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    TypeNode findType(final int line) {
        types().find { TypeNode t -> t.containsLine(line) }
    }

    /**
     * Retrieves the type with the given qualified identifier.
     *
     * @param key
     *            Qualified identifier of the type to find in this file.
     * @return The type with the given identifier, if one exists, null
     *         otherwise or if the given identifier is null or empty.
     */
    TypeNode findType(final String key) {
        if (key == null || key.isEmpty())
            return null

        types().find { TypeNode t -> t.key == key }
    }

    /**
     * Retrieves the qualified identifier of the field at the given line, if one
     * exists.
     *
     * @param line
     *            Line to find a field at.
     * @return The qualified identifier if the given line contains a field
     *         definition, empty string otherwise.
     */
    String findField(final int line) {
        final String type = getType(line)

        if (type != null && types.containsKey(type)) {
            final TypeNode node = types.get(type)
            if (node.getField(line) != null) {
                return node.getField(line).getQIdentifier()
            }
        }

        return ""
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode node) {
        if (node == null)
            return
        if (!(node instanceof FileNode))
            return

        FileNode f = (FileNode) node

        f.types().each { TypeNode t ->
            if (hasType(t.key)) {
                findType(t.key).update(t)
            } else {
                children << t
            }
        }

        f.getMetricNames().each { this.addMetric(key, f.getMetric(key)) }
    }

    /**
     * Checks whether the provided type is contained within this file.
     *
     * @param key
     *            The unique identifier of the type in question
     * @return true if contained in this file, false if provided type is null or
     *         not contained in this file.
     */
    boolean hasType(String key) {
        findType(key) != null
    }

    /**
     * @return The set of methods of all types contained in this file.
     */
    List<MethodNode> methods() {
        def methods = []
        types().each { TypeNode t ->
            methods.addAll(t.methods())
        }

        return methods
    }

    /**
     * Checks if the given import is contained in this FileNode
     *
     * @param imp
     *            Import to check
     * @return true if the given import is contained in the FileNode, false if
     *         the provided value is null, empty, or not contained in the
     *         FileNode.
     */
    boolean hasImport(String imp) {
        if (imp == null || imp.isEmpty())
            return false

        return imports.contains(imp)
    }

    /**
     * @return The set of Imports contained in the FileNode
     */
    List<String> getImports() {
        []
    }

    /**
     * {@inheritDoc}
     */
    @Override
    FileNode cloneNoChildren() {
        FileNode fnode = FileNode.builder().key(this.key).parentKey(parentKey).create()

        copyMetrics(fnode)

        return fnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    FileNode clone() throws CloneNotSupportedException {
        FileNode fnode = cloneNoChildren()

        children.each { fnode.children << it.clone() }

        return fnode
    }

    def extractTree(tree) {
        def retVal = new CodeTree()
        Stack<ProjectNode> stack = new Stack<>()
        ProjectNode project
        ModuleNode module
        CodeTreeUtils utils = tree.utils

        if (utils.findProject(parentKey) != null) {
            project = utils.findProject(parentKey)
        } else {
            module = utils.findModule(parentKey)
            project = utils.findProject(module.parentKey)
        }

        stack.push(project)
        while (project.hasParent()) {
            project = utils.findProject(project.parentKey)
            stack.push(project)
        }

        ProjectNode current = stack.pop().cloneNoChildren()
        ProjectNode root = current
        ProjectNode next
        ProjectNode actual = current
        while (!stack.isEmpty()) {
            next = stack.pop().cloneNoChildren()

            current.children << next
            if (next.key == parentKey)
                actual = next

            current = next
        }

        try {
            actual.children << clone()
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace()
        }

        retVal.setProject(root)
    }
}
