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
package edu.montana.gsoc.msusel.codetree.node;

import edu.montana.gsoc.msusel.codetree.INode;
import edu.montana.gsoc.msusel.codetree.relations.Relationship;
import lombok.Builder;
import lombok.Singular;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * An abstraction of a type variable. Here a field's unique qualified name is a
 * combination of the containing type's qualified name plus the field's variable
 * name separated by a '#'. A field has several properties including its
 * associated variable Type and whether or not it is a collection type (i.e.,
 * and array or other data structure).
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class FieldNode extends CodeNode {

    /**
     * Constructs a new FieldNode with the given Qualified Identifier and simple
     * name.
     * 
     * @param qIdentifier
     *            Qualified Identifier
     * @param name
     *            Simple Name
     */
    protected FieldNode(final String qIdentifier, String name)
    {
        super(qIdentifier, name);
    }

    @Builder(buildMethodName = "create")
    protected FieldNode(int start, int end, String name, String identifier, @Singular List<Relationship> relations, String parentID, @Singular Map<String, Double> metrics) {
        super(start, end, name, identifier, relations, parentID, metrics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.FIELD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode f)
    {
        if (f == null)
            return;

        if (f instanceof CodeNode)
        {
            CodeNode c = (CodeNode) f;
            setRange(c.getStart(), c.getEnd());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldNode cloneNoChildren()
    {
        FieldNode node = new FieldNode(this.getQIdentifier(), this.getName());
        node.setRange(this.getStart(), this.getEnd());

        copyMetrics(node);

        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FieldNode clone() throws CloneNotSupportedException
    {
        return cloneNoChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
