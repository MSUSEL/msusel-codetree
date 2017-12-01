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
package codetree

import com.sparqline.codetree.util.MetricNameRegistry
import groovy.transform.EqualsAndHashCode
import org.apache.commons.lang3.tuple.Pair
import org.apache.log4j.Logger

/**
 * The Abstract Class for all Nodes in a CodeTree
 * 
 * @author Isaac Griffith
 * @version 
 */
@EqualsAndHashCode(includeFields=true, excludes=["children","metrics"])
abstract class AbstractNode implements INode {

    /**
     * The collection of children of this node
     */
    def children = []
    /**
     * The unique identifying key of this node
     */
    String key
    /**
     * Data structure for recording metric values
     */
    def metrics = []
    /**
     * Logger used to output events associated with CodeNodes
     */
    protected static Logger LOG = Logger.getLogger(CodeNode.class)
    /**
     * The unique identifier of the parent node or null if there is no parent
     */
    String parentKey
    /**
     * flag indicating that the node's metrics have been aggregated
     */
    boolean aggregated = false

    /**
     * 
     */
    public AbstractNode() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addMetric(String name, Double value) {
        if (name == null || name.isEmpty() || value == null || Double.isNaN(value) || Double.isInfinite(value))
            return

        String metric = MetricNameRegistry.getInstance().lookup(name)

        if (metric != null) {
            metrics.put(metric, value)
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void incrementMetric(String name, Double increment) {
        if (name == null || name.isEmpty() || increment == null || Double.isNaN(increment)
        || Double.isInfinite(increment))
            return

        if (metrics.containsKey(MetricNameRegistry.getInstance().lookup(name)))
            metrics.put(
                    MetricNameRegistry.getInstance().lookup(name),
                    metrics.get(MetricNameRegistry.getInstance().lookup(name)) + increment)
        else
            metrics.put(MetricNameRegistry.getInstance().lookup(name), increment)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    double getMetric(String metric) {
        if (metric == null || metric.isEmpty() || !hasMetric(metric)) {
            LOG.warn(
                    "Bad Metric: " + metric + " for " + this.getClass().getSimpleName() + " with id: "
                    + this.getQIdentifier())
            return -1.0
        }

        return metrics.get(MetricNameRegistry.getInstance().lookup(metric))
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasMetric(String metric) {
        return metrics.containsKey(MetricNameRegistry.getInstance().lookup(metric))
    }

    /**
     * {@inheritDoc}
     */
    @Override
    abstract INode cloneNoChildren()

    /**
     * @param other
     *            The other node in which to copy metrics from.
     */
    protected void copyMetrics(INode other) {
        for (String key : getMetricNames()) {
            other.addMetric(key, getMetric(key))
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<String> getMetricNames() {
        metrics.keySet()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addMetrics(List<Pair<String, Double>> join) {
        for (Pair<String, Double> pair : join) {
            addMetric(pair.getKey(), pair.getValue())
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasParent() {
        parentKey != null
    }
}
