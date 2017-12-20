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
package codetree.node

import codetree.Accessibility
import codetree.CodeNode
import codetree.Modifiers
import codetree.CodeTree
import codetree.ProjectNode

/**
 * @author Isaac Griffith
 *
 */
abstract class TypeNode extends CodeNode {

    def templateParams = []
    NamespaceNode namespace
    
    /**
     * 
     */
    public TypeNode()
    {
        accessibility = Accessibility.PUBLIC
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
    
    boolean isStatic() {
        specifiers.contains(Modifiers.STATIC)
    }
    
    boolean isAbstract() {
        specifiers.contains(Modifiers.ABSTRACT)
    }
    
    def properties() {
        children.findAll {
            it instanceof PropertyNode
        }
    }
    
    def name() {
        key.split("\\.").last()
    }
    
    abstract boolean isInterface()
    
    def extractTree(tree) {
        TypeNode type = (TypeNode) node

        FileNode file = findFile(type.getParentKey())

        retVal = new CodeTree()
        Stack<ProjectNode> stack = new Stack<>()
        ProjectNode project
        ModuleNode module

        if (findProject(file.getParentKey()) != null)
        {
            project = findProject(file.getParentKey())
        }
        else
        {
            module = findModule(file.getParentKey())
            project = findProject(module.getParentKey())
        }

        stack.push(project)
        while (project.hasParent())
        {
            project = findProject(project.getParentKey())
            stack.push(project)
        }

        ProjectNode current = stack.pop().cloneNoChildren()
        ProjectNode root = current
        ProjectNode next
        while (!stack.isEmpty())
        {
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
}
