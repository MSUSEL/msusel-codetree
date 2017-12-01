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

import codetree.INode

/**
 * @author Isaac Griffith
 *
 */
class PrimitiveTypeNode extends TypeNode {

    /**
     * 
     */
    private PrimitiveTypeNode()
    {
    }

    public static PrimitiveTypeNode getInstance(String key) {
        MultitonHolder.map[key]
    }
    
    static def getTypes() {
        Set<TypeNode> types = new HashSet<>()
        types += MultitonHolder.map.values()
    }
    
    private static class MultitonHolder {
        private static def map = [:]
        
        static {
            // Shared Java and C# Primitive Types
            map["int"] = new PrimitiveTypeNode(key: "int")
            map["long"] = new PrimitiveTypeNode(key: "long")
            map["char"] = new PrimitiveTypeNode(key: "char")
            map["byte"] = new PrimitiveTypeNode(key: "byte")
            map["short"] = new PrimitiveTypeNode(key: "short")
            map["double"] = new PrimitiveTypeNode(key: "double")
            map["float"]  = new PrimitiveTypeNode(key: "float")
            map["void"] = new PrimitiveTypeNode(key: "void")
            
            // Java Primitive Types
            map["boolean"] = new PrimitiveTypeNode(key: "boolean")
            
            // C# Primitive Types
            map["sbyte"] = new PrimitiveTypeNode(key: "sbyte")
            map["ushort"] = new PrimitiveTypeNode(key: "ushort")
            map["uint"] = new PrimitiveTypeNode(key: "uint")
            map["ulong"] = new PrimitiveTypeNode(key: "ulong")
            map["object"] = new PrimitiveTypeNode(key: "object")
            map["string"] = new PrimitiveTypeNode(key: "string")
            map["decimal"] = new PrimitiveTypeNode(key: "decimal")
            map["bool"] = new PrimitiveTypeNode(key: "bool")
            map["DataTime"] = new PrimitiveTypeNode(key: "DateTime")
            map["DateSpan"] = new PrimitiveTypeNode(key: "DateSpan")
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInterface()
    {
        false
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object type()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode other)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode cloneNoChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
