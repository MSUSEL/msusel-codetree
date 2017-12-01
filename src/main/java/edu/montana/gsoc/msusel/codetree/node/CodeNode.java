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

import com.google.common.collect.Range;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.codetree.AbstractNode;
import edu.montana.gsoc.msusel.codetree.Accessibility;
import edu.montana.gsoc.msusel.codetree.relations.Relationship;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Base class for source code entities.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
@EqualsAndHashCode(of = {"end", "start"}, callSuper = true)
public abstract class CodeNode extends AbstractNode implements Comparable<CodeNode> {

    @Getter
    protected Accessibility accessibility;
    def specifiers = [];
    @Expose
    @Getter
    private int            start = 1;
    @Expose
    @Getter
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

    protected CodeNode(int start, int end, String name, String identifier, List<Relationship> relations, String parentID, Map<String, Double> metrics) {
        super(name, identifier, relations, parentID, metrics);
        this.start = start;
        this.end = end;
        updateRange();
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
        if (start > getEnd())
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
     * @param end
     *            the end line to set
     * @throws IllegalArgumentException
     *             if the new value of is less than the current lower value of
     *             the range.
     */
    protected void setEnd(final int end)
    {
        if (end < getStart())
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
        return Integer.compare(getStart(), o.getStart());
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
        this.setStart(start);
        this.setEnd(end);
        updateRange();
    }

    /**
     * Updates the range when either the lower or upper bound has changed.
     */
    private void updateRange()
    {
        range = Range.closed(getStart(), getEnd());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "CodeNode [start=" +
                getStart() +
                ", end=" +
                getEnd() +
                ", qIdentifier=" +
                getQIdentifier() +
                ", name=" +
                getName() +
                "]";
    }
}
