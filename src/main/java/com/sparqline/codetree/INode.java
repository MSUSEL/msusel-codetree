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
package com.sparqline.codetree;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Interface defining the base methods of all Nodes representing both code and
 * structure of a software system.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public interface INode {

    /**
     * @return Type of the node under evaluation.
     */
    String getType();

    /**
     * Adds a measurement value for the named metric to this Node
     * 
     * @param name
     *            Metric name
     * @param value
     *            Measurement Value
     */
    void addMetric(String name, Double value);

    /**
     * Increments a measurement value for the named metric by the given
     * increment value
     * 
     * @param name
     *            Metric name
     * @param increment
     *            Increment value
     */
    void incrementMetric(String name, Double increment);

    /**
     * Retrieves the stored measurement value of the named metric, if such a
     * value exists.
     * 
     * @param metric
     *            Metric name
     * @return Measurement value, or null if no such metric has been measured
     *         for this node.
     */
    Double getMetric(String metric);

    /**
     * Checks whether this node has a recorded measurement value for the named
     * metric.
     * 
     * @param metric
     *            Metric name
     * @return true if a value has been recorded for the named metric, false
     *         otherwise.
     */
    boolean hasMetric(String metric);

    /**
     * @return the Qualified Identifier of this node
     */
    String getQIdentifier();

    /**
     * @return the name of this node
     */
    String getName();

    /**
     * Merges the contents of this node with those of the other node. In the
     * case of metrics, the other node's values override this node's
     * 
     * @param other
     *            Node to be merged into this node.
     */
    void update(INode other);

    /**
     * Clones this node without concerning itself with the children of this
     * node.
     * 
     * @return A clone of this node (excluding its children).
     */
    INode cloneNoChildren();

    /**
     * @return
     */
    Set<String> getMetricNames();

    /**
     * Adds metrics in bulk, overriding any existing measurement values.
     * 
     * @param join
     *            Updates the metrics of this node with the content of the
     *            provided List of metric name and measurement value pairs.
     */
    void addMetrics(List<Pair<String, Double>> join);

    /**
     * @return This node's parent's qualified identifier
     */
    String getParentID();

    /**
     * Sets the parent qualified identifier of this node.
     * 
     * @param id
     *            parent's qualified identifier.
     */
    void setParentID(String id);

    /**
     * Checks if this node has a parent.
     * 
     * @return True if the parent id is not null, false otherwise.
     */
    boolean hasParent();

}