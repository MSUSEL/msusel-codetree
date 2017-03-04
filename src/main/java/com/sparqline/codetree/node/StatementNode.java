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

import com.google.gson.annotations.Expose;
import com.sparqline.codetree.INode;

/**
 * An abstraction of the statements making up the body of a method. A statement
 * has a defined Statement Type and its unique qualitfied name is a combination
 * of that type and a separate long integer representing the count of that
 * statement type within the system.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class StatementNode extends CodeNode {

    /**
     * The next valid IDNum
     */
    private static long   nextIDNum = 0;
    /**
     * The type of this statement
     */
    @Expose
    private StatementType type;

    /**
     * Constructs a new StatementNode with the given type.
     * 
     * @param type
     *            The StatementNode's tyupe
     */
    protected StatementNode(StatementType type)
    {
        super(type + ":" + nextIDNum++, type.toString());
        this.type = type;
    }

    /**
     * @return StatementType of this StatementNode
     */
    public StatementType getStatementType()
    {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.STATEMENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode c)
    {
        if (c == null)
            return;

        if (c instanceof StatementNode && c.getQIdentifier().equals(this.getQIdentifier()))
        {
            StatementNode s = (StatementNode) c;
            setRange(s.getStart(), s.getEnd());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatementNode cloneNoChildren()
    {
        StatementNode fnode = new StatementNode(this.type);
        fnode.setRange(this.getStart(), this.getEnd());
        copyMetrics(fnode);

        return fnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected StatementNode clone() throws CloneNotSupportedException
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
     * Updates the nextIDNum static field to the one provided if the provided
     * value is greater than or equal to the current value. This method should
     * only be used during deserialization.
     * 
     * @param num
     *            New nextIDNum
     */
    public static void setNextIDNum(long num)
    {
        if (num < nextIDNum)
            return;

        nextIDNum = num;
    }

    /**
     * Constructs a new Builder for a StatementNode with the given
     * StatementType
     * 
     * @param type
     *            Type of the Node to be built.
     * @return The StatementNode.Builder instance
     */
    public static Builder builder(StatementType type)
    {
        return new Builder(type);
    }

    /**
     * Builder for Statements implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * StatementNode to be constructed.
         */
        private StatementNode node;

        /**
         * Constructs a new Builder for a StatementNode with the given
         * StatementType
         * 
         * @param type
         *            Type of the Node to be built.
         */
        private Builder(StatementType type)
        {
            node = new StatementNode(type);
        }

        /**
         * @return The newly built StatementNode
         */
        public StatementNode create()
        {
            return node;
        }

        /**
         * Sets the line range of the new statement to be between the given
         * start and end values.
         * 
         * @param start
         *            Starting line value
         * @param end
         *            Ending line value
         * @return this
         */
        public Builder range(int start, int end)
        {
            node.setRange(start, end);

            return this;
        }

        /**
         * Sets the line range of this new statement, assmuming that the
         * statement only spans a single line
         * 
         * @param line
         *            Line on which the statement resides in its containing
         *            file.
         * @return this
         */
        public Builder range(int line)
        {
            return range(line, line);
        }

        /**
         * Sets the qualified identifier of the parent for this statement
         * 
         * @param pID
         *            Parent's qualified identifier
         * @return this
         */
        public Builder parent(String pID)
        {
            node.setParentID(pID);

            return this;
        }
    }
}
