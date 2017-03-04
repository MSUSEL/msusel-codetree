/**
 * The MIT License (MIT)
 *
 * Sonar Quamoco Plugin
 * Copyright (c) 2015-2017 Isaac Griffith, SiliconCode, LLC
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

import com.google.common.collect.Range;
import com.google.gson.annotations.Expose;
import com.sparqline.codetree.AbstractNode;

/**
 * Base class for source code entities.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
public abstract class CodeNode extends AbstractNode implements Comparable<CodeNode> {

    /**
     * The starting line value, only applicable to source code elements
     */
    @Expose
    private int            start = 1;
    /**
     * The ending line value, only applicable to source code elements
     */
    @Expose
    private int            end   = Integer.MAX_VALUE;
    /**
     * Range entity used to detect if something exists within this code node's
     * range
     */
    @Expose
    private Range<Integer> range;

    /**
     * Constructs a new CodeNode with the given qualified and unqualified
     * identifiers.
     * 
     * @param qIdentifier
     *            The qualified identifier of this node
     * @param name
     *            The short name, or unqualified name, of this node.
     */
    protected CodeNode(final String qIdentifier, final String name)
    {
        super(qIdentifier, name);
    }

    /**
     * @return the start line of this entity
     */
    public int getStart()
    {
        return start;
    }

    /**
     * @param start
     *            the start line to set
     * @throws IllegalArgumentException
     *             if the new value is greater than the current upper value of
     *             the range or the provided value is less than 1.
     */
    protected void setStart(final int start)
    {
        if (start > end)
        {
            throw new IllegalArgumentException("Start cannot be greater than end");
        }

        if (start < 1)
        {
            throw new IllegalArgumentException("Start cannot be less than 1.");
        }

        this.start = start;
        updateRange();
    }

    /**
     * @return the end line of this entity
     */
    public int getEnd()
    {
        return end;
    }

    /**
     * @param end
     *            the end line to set
     * @throws IllegalArgumentException
     *             if the new value of is less than the current lower value of
     *             the range.
     */
    protected void setEnd(final int end)
    {
        if (end < start)
        {
            throw new IllegalArgumentException("End cannot be less than start");
        }

        this.end = end;
        updateRange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final CodeNode o)
    {
        return Integer.compare(start, o.start);
    }

    /**
     * Checks whether this line range of this entity contains the provided
     * value.
     * 
     * @param line
     *            value to check
     * @return true if the line range of this entity contains the provided line,
     *         false otherwise.
     */
    public boolean containsLine(final int line)
    {
        return range.contains(line);
    }

    /**
     * Sets the line range of this entity
     * 
     * @param start
     *            lower value (inclusive) of the range
     * @param end
     *            upper value (inclusive) of the range
     */
    protected void setRange(int start, int end)
    {
        this.start = start;
        this.end = end;
        updateRange();
    }

    /**
     * Updates the range when either the lower or upper bound has changed.
     */
    private void updateRange()
    {
        range = Range.closed(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CodeNode [start=")
                .append(start)
                .append(", end=")
                .append(end)
                .append(", qIdentifier=")
                .append(qIdentifier)
                .append(", name=")
                .append(name)
                .append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((qIdentifier == null) ? 0 : qIdentifier.hashCode());
        result = prime * result + start;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof CodeNode))
        {
            return false;
        }
        CodeNode other = (CodeNode) obj;
        if (end != other.end)
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (qIdentifier == null)
        {
            if (other.qIdentifier != null)
            {
                return false;
            }
        }
        else if (!qIdentifier.equals(other.qIdentifier))
        {
            return false;
        }
        if (start != other.start)
        {
            return false;
        }
        return true;
    }
}
