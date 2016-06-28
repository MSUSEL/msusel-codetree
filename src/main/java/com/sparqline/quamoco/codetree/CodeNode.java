/**
 * The MIT License (MIT)
 *
 * Sonar Quamoco Plugin
 * Copyright (c) 2015 Isaac Griffith, SiliconCode, LLC
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
package com.sparqline.quamoco.codetree;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.gson.annotations.Expose;

/**
 * CodeNode -
 *
 * @author Isaac Griffith
 */
public abstract class CodeNode implements Comparable<CodeNode> {

    @Expose
    private int                   start = 1;
    @Expose
    private int                   end   = Integer.MAX_VALUE;
    @Expose
    private Range<Integer>        range;
    @Expose
    protected Map<String, Double> metrics;
    @Expose
    protected String              qIdentifier;
    @Expose
    protected String              name;

    protected CodeNode() {
        metrics = Maps.newHashMap();
        qIdentifier = "";
        name = "";
    }

    public CodeNode(final String qIdentifier, final String name, final int start, final int end) {
        this.qIdentifier = qIdentifier;
        this.name = name;

        if (qIdentifier == null || qIdentifier.isEmpty() || name == null || name.isEmpty())
            throw new IllegalArgumentException("Name and QIdentifier can be neither null nor empty.");

        metrics = Maps.newHashMap();
        setStart(start);
        setEnd(end);
        range = Range.closed(start, end);
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(final int start) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot be greater than end");
        }

        if (start < 1) {
            throw new IllegalArgumentException("Start cannot be less than 1.");
        }

        this.start = start;
        updateRange();
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(final int end) {
        if (end < start) {
            throw new IllegalArgumentException("End cannot be less than start");
        }

        this.end = end;
        updateRange();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final CodeNode o) {
        return Integer.compare(start, o.start);
    }

    public boolean containsLine(final int line) {
        return range.contains(line);
    }

    /**
     * @return
     */
    public abstract String getType();

    private void updateRange() {
        range = Range.closed(start, end);
    }

    public void addMetric(String name, Double value) {
        if (name == null || name.isEmpty() || value == null || Double.isNaN(value) || Double.isInfinite(value))
            return;

        metrics.put(name, value);
    }

    public void incrementMetric(String name, Double increment) {
        if (name == null || name.isEmpty() || increment == null || Double.isNaN(increment)
                || Double.isInfinite(increment))
            return;

        if (metrics.containsKey(name))
            metrics.put(name, metrics.get(name));
        else
            metrics.put(name, increment);
    }

    public Double getMetric(String metric) {
        if (metric == null || metric.isEmpty() || !hasMetric(metric))
            return Double.NaN;

        return metrics.get(metric);
    }

    public boolean hasMetric(String metric) {
        return metrics.containsKey(metric);
    }

    public void updateLocation(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @return the qIdentifier
     */
    public String getQIdentifier() {
        return qIdentifier;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CodeNode [start=").append(start).append(", end=").append(end).append(", qIdentifier=")
                .append(qIdentifier).append(", name=").append(name).append("]");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((qIdentifier == null) ? 0 : qIdentifier.hashCode());
        result = prime * result + start;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CodeNode)) {
            return false;
        }
        CodeNode other = (CodeNode) obj;
        if (end != other.end) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        if (qIdentifier == null) {
            if (other.qIdentifier != null) {
                return false;
            }
        }
        else if (!qIdentifier.equals(other.qIdentifier)) {
            return false;
        }
        if (start != other.start) {
            return false;
        }
        return true;
    }

    public abstract void update(CodeNode c);
}
