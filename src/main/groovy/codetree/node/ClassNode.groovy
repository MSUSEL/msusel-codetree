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
class ClassNode extends TypeNode implements Cloneable {

    boolean abstrct
    
    /**
     * 
     */
    public ClassNode()
    {
        // TODO Auto-generated constructor stub
    }
    
    def getFields() {
        children.findAll { it instanceof FieldNode }
    }
    
    def getMethods() {
        children.findAll { it instanceof MethodNode }
    }
    
    def getConstructors() {
        children.findAll { it instanceof ConstructorNode }
    }
    
    def getDestructors() {
        children.findAll { it instanceof DestructorNode }
    }
    
    def getInitializers() {
        children.findAll { it instanceof InitializerNode }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInterface()
    {
        false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type()
    {
        "Class"
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode other)
    {
        TypeNode tnode = new ClassNode(qIdentifier, name);
        tnode.setStart(getStart());
        tnode.setEnd(getEnd());

        copyMetrics(tnode);

        return tnode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected TypeNode clone() throws CloneNotSupportedException
    {
        TypeNode tnode = cloneNoChildren();

        for (String key : fields.keySet())
        {
            tnode.addField(fields.get(key).clone());
        }

        for (String key : methods.keySet())
        {
            tnode.addMethod(methods.get(key).clone());
        }

        return tnode;
    }
    
    /**
     * Searches this TypeNode for a method whose line range contains the given
     * line
     *
     * @param line
     *            The line
     * @return MethodNode whose range contains the given line, or null if no
     *         such MethodNode exists in this type.
     */
    MethodNode findMethod(int line)
    {
        if (line < getStart() || line > getEnd())
            return null;

        for (MethodNode method : methods.values())
        {
            if (method.containsLine(line))
                return method;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    INode cloneNoChildren()
    {
        TypeNode tnode = new ClassNode(qIdentifier, name);
        tnode.setStart(getStart());
        tnode.setEnd(getEnd());

        copyMetrics(tnode);

        return tnode;
    }
}
