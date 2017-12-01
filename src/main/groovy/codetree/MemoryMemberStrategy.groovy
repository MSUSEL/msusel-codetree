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
package codetree

import codetree.node.ConstructorNode
import codetree.node.DestructorNode
import codetree.node.EnumLiteralNode
import codetree.node.FieldNode
import codetree.node.InitializerNode
import codetree.node.MethodNode
import codetree.node.PropertyNode

/**
 * @author Isaac Griffith
 *
 */
class MemoryMemberStrategy extends MemberStrategy {

    /**
     * 
     */
    public MemoryMemberStrategy()
    {
        // TODO Auto-generated constructor stub
    }

    def getFieldNode(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def fld = new FieldNode(key: key)
            NodeFactory.nodes[key] = fld
            return fld
        }
    }
    
    def getConstructorNode(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def cons = new ConstructorNode(key: key)
            NodeFactory.nodes[key] = cons
            return cons
        }
    }
    
    def getDestructorNode(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def des = new DestructorNode(key: key)
            NodeFactory.nodes[key] = des
            return des
        }
    }
    
    def getInitializer(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def init = new InitializerNode(key: key)
            NodeFactory.nodes[key] = init
            return init
        }
    }
    
    def getMethodNode(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def meth = new MethodNode(key: key)
            NodeFactory.nodes[key] = meth
            return meth
        }
    }
    
    def getLiteral(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def lit = new EnumLiteralNode(key: key)
            NodeFactory.nodes[key] = lit
            return lit
        }
    }
    
    def getProperty(key) {
        if (NodeFactory.nodes[key])
            return NodeFactory.nodes[key]
        else {
            def prop = new PropertyNode(key: key)
            NodeFactory.nodes[key] = prop
            return prop
        }
    }
}
