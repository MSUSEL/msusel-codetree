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
package codetree

import codetree.node.ClassNode
import codetree.node.ConstructorNode
import codetree.node.DestructorNode
import codetree.node.EnumLiteralNode
import codetree.node.EnumNode
import codetree.node.FieldNode
import codetree.node.FileNode
import codetree.node.ImportNode
import codetree.node.InitializerNode
import codetree.node.InterfaceNode
import codetree.node.MethodNode
import codetree.node.ModuleNode
import codetree.node.NamespaceNode
import codetree.node.ParameterNode

/**
 * @author Isaac Griffith
 *
 */
class NodeFactory {

    static def nodes = [:]
    
    /**
     * 
     */
    public NodeFactory()
    {
        // TODO Auto-generated constructor stub
    }

    static def getNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            // create new node
            // add it to pool of nodes
            // return the new node
        }
    }
    
    static def getClassNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def type = new ClassNode(key: key)
            nodes[key] = type
            return type
        }
    }
    
    static def getInterfaceNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def type = new InterfaceNode(key: key)
            nodes[key] = type
            return type
        }
    }
    
    static def getEnumNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def type = new EnumNode(key: key)
            nodes[key] = type
            return type
        }
    }
    
    static def getConstructorNode(key, type) {
        if (nodes[key])
            return nodes[key]
        else {
            def cons = new ConstructorNode(key: key)
            nodes[key] = cons
            return cons
        }
    }
    
    static def getDestructorNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def des = new DestructorNode(key: key)
            nodes[key] = des
            return des
        }
    }
    
    static def getMethodNode(key) {
        def meth = new MethodNode(key: key)
        MemberMMap.store(meth)
    }
    
    static def getModuleNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def mod = new ModuleNode(key: key)
            nodes[key] = mod
            return mod
        }
    }
    
    static def getFileNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def file = new FileNode(key: key)
            nodes[key] = file
            return file
        }
    }
    
    static def getImportNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def imp = new ImportNode(key: key)
            nodes[key] = imp
            return imp
        }
    }
    
    static def getProjectNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def proj = new ProjectNode(key: key)
            nodes[key] = proj
            return proj
        }
    }
    
    static def getNamespaceNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def ns = new NamespaceNode(key: key)
            nodes[key] = ns
            return ns
        }
    }
    
    static def getFieldNode(key) {
        def field = new FieldNode(key: key)
        MemberMMap.store(field)
    }
    
    static def getEnumLiteralNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def lit = new EnumLiteralNode(key: key)
            nodes[key] = lit
            return lit
        }
    }
    
    static def getInitializerNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def init = new InitializerNode(key: key)
            nodes[key] = init
            return init
        }
    }
    
    static def getParameterNode(key) {
        if (nodes[key])
            return nodes[key]
        else {
            def param = new ParameterNode(key: key)
            nodes[key] = param
            return param
        }
    }
}
