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
package edu.montana.gsoc.msusel.codetree;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.codetree.node.CodeNode;
import edu.montana.gsoc.msusel.codetree.relations.Relationship;
import edu.montana.gsoc.msusel.codetree.util.MetricNameRegistry;

/**
 * @author Isaac Griffith
 * @version
 */
public abstract class AbstractNode implements INode {

	/**
	 * Data structure for recording metric values
	 */
	@Expose
	protected Map<String, Double> metrics;
	/**
	 * Unique qualified identifier for this entity
	 */
	@Expose
	protected String qIdentifier;
	/**
	 * Simple name of this entity
	 */
	@Expose
	protected String name;
	/**
	 * Relationships between this entity and others
	 */
	protected List<Relationship> outRelations;
	/**
	 * The unique qualified identifier of a parent entity
	 */
	@Expose
	protected String parentID;
	/**
	 * Logger used to output events associated with CodeNodes
	 */
	protected Logger LOG = Logger.getLogger(CodeNode.class);

	/**
	 * Constructs a new AbstractNode with the given qualified identifier and
	 * name
	 * 
	 * @param qIdentifier
	 *            The Qualified Identifier
	 * @param name
	 *            The Name
	 */
	protected AbstractNode(String qIdentifier, String name) {
		if (qIdentifier == null || qIdentifier.isEmpty() || name == null || name.isEmpty())
			throw new IllegalArgumentException("Name and QIdentifier can be neither null nor empty.");

		this.qIdentifier = qIdentifier;
		this.name = name;
		metrics = Maps.newHashMap();
		outRelations = Lists.newArrayList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String getType();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMetric(String name, Double value) {
		if (name == null || name.isEmpty() || value == null || Double.isNaN(value) || Double.isInfinite(value))
			return;

		String metric = MetricNameRegistry.getInstance().lookup(name);

		if (metric != null) {
			metrics.put(metric, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void incrementMetric(String name, Double increment) {
		if (name == null || name.isEmpty() || increment == null || Double.isNaN(increment)
				|| Double.isInfinite(increment))
			return;

		if (metrics.containsKey(MetricNameRegistry.getInstance().lookup(name)))
			metrics.put(MetricNameRegistry.getInstance().lookup(name),
					metrics.get(MetricNameRegistry.getInstance().lookup(name)) + increment);
		else
			metrics.put(MetricNameRegistry.getInstance().lookup(name), increment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getMetric(String metric) {
		if (metric == null || metric.isEmpty() || !hasMetric(metric)) {
			LOG.warn("Bad Metric: " + metric + " for " + this.getClass().getSimpleName() + " with id: "
					+ this.getQIdentifier());
			return -1.0;
		}

		return metrics.get(MetricNameRegistry.getInstance().lookup(metric));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMetric(String metric) {
		return metrics.containsKey(MetricNameRegistry.getInstance().lookup(metric));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQIdentifier() {
		return qIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            set the name
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void update(INode c);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract INode cloneNoChildren();

	/**
	 * @param other
	 *            The other node in which to copy metrics from.
	 */
	protected void copyMetrics(INode other) {
		for (String key : getMetricNames()) {
			other.addMetric(key, getMetric(key));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getMetricNames() {
		return metrics.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMetrics(List<Pair<String, Double>> join) {
		for (Pair<String, Double> pair : join) {
			addMetric(pair.getKey(), pair.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParentID() {
		return parentID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParentID(String id) {
		if (id == null)
			parentID = id;
		else if (id.isEmpty() || id.equals(this.qIdentifier))
			throw new IllegalArgumentException("Parent ID cannot be same as this id or empty");
		else
			parentID = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasParent() {
		return parentID != null;
	}
}
