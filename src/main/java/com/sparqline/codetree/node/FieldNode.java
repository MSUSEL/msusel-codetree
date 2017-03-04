/**
 * The MIT License (MIT)
 *
 * SparQLine Code Tree
 * Copyright (c) 2015-2017 Isaac Griffith, SparQLine Analytics, LLC
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
package com.sparqline.codetree.node;

import org.eclipse.jdt.annotation.NonNull;

import com.sparqline.codetree.INode;

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
     * Constructs a new FieldNode with the given Qualfied Identifier and simple
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
        FieldNode fnode = new FieldNode(this.qIdentifier, this.name);
        fnode.setRange(this.getStart(), this.getEnd());

        copyMetrics(fnode);

        return fnode;
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

    /**
     * Constructs a new Builder for a FieldNode with the given Qualified
     * Identifier and Simple Name
     * 
     * @param name
     *            Simple Name
     * @param qID
     *            Qualified Identifier
     * @return The FieldNode.Builder instance
     */
    public static Builder builder(String name, String qID)
    {
        return new Builder(name, qID);
    }

    /**
     * Builder for Fields implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * The FieldNode to be constructed by this Builder
         */
        private FieldNode node;

        /**
         * Constructs a new Builder for a FieldNode with the given Qualified
         * Identifier and Simple Name
         * 
         * @param name
         *            Simple Name
         * @param qID
         *            Qualified Identifier
         */
        private Builder(String name, String qID)
        {
            node = new FieldNode(qID, name);
        }

        /**
         * @return The newly constructed FieldNode
         */
        @NonNull
        public FieldNode create()
        {
            return node;
        }

        /**
         * Sets the line range of this field
         * 
         * @param start
         *            Start of the range (inclusive)
         * @param end
         *            End of the range (inclusive)
         * @return this
         */
        @NonNull
        public Builder range(int start, int end)
        {
            node.setStart(start);
            node.setEnd(end);

            return this;
        }

        /**
         * Sets the line range to the given line for the FieldNode under
         * construction.
         * 
         * @param line
         *            Line containing the field
         * @return this
         */
        @NonNull
        public Builder range(int line)
        {
            return range(line, line);
        }

        /**
         * The the metric and measurement value to the FieldNode under
         * construction.
         * 
         * @param metric
         *            Metric name
         * @param value
         *            Measurement value
         * @return this
         */
        @NonNull
        public Builder metric(String metric, Double value)
        {
            node.addMetric(metric, value);

            return this;
        }

        /**
         * Sets the Qualified Identifier of the parent of the FieldNode under
         * construction
         * 
         * @param pID
         *            Parents Qualified Identifier
         * @return this
         */
        @NonNull
        public Builder parent(String pID)
        {
            node.setParentID(pID);

            return this;
        }
    }
}
