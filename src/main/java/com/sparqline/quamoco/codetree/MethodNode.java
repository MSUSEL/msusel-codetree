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

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

/**
 * MethodNode -
 *
 * @author Isaac Griffith
 */
public class MethodNode extends CodeNode {

	@Expose
	private boolean constructor;
	@Expose
	private String parentID;
	@Expose
	private boolean isAbstract;
	@Expose
	private boolean accessorMethod;

	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * @return the accessorMethod
	 */
	public boolean isAccessorMethod() {
		return accessorMethod;
	}

	/**
	 * @param accessorMethod the accessorMethod to set
	 */
	public void setAccessorMethod(boolean accessorMethod) {
		this.accessorMethod = accessorMethod;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	private List<StatementNode> statements;

	private MethodNode() {
		super();
		constructor = false;
		statements = Lists.newArrayList();
	}

	/**
	 * @param identifier
	 * @param start
	 * @param end
	 */
	public MethodNode(final String qIdentifier, String name, final boolean constructor, final int start,
			final int end) {
		super(qIdentifier, name, start, end);
		this.constructor = constructor;
	}

	public boolean isConstructor() {
		return constructor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.siliconcode.quamoco.codetree.CodeNode#getType()
	 */
	@Override
	public String getType() {
		return CodeNodeType.METHOD;
	}

	/**
	 * @param constructor
	 */
	public void setConstructor(final boolean constructor) {
		this.constructor = constructor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sparqline.quamoco.codetree.CodeNode#update(com.sparqline.quamoco.
	 * codetree.CodeNode)
	 */
	@Override
	public void update(CodeNode m) {
		if (m == null)
			return;

		if (!(m instanceof MethodNode))
			return;

		MethodNode node = (MethodNode) m;
		setStart(m.getStart());
		setEnd(m.getEnd());

		setConstructor(node.isConstructor());

		for (String key : m.metrics.keySet()) {
			this.metrics.put(key, m.metrics.get(key));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sparqline.quamoco.codetree.CodeNode#cloneNoChildren()
	 */
	@Override
	public MethodNode cloneNoChildren() {
		MethodNode mnode = new MethodNode(this.qIdentifier, this.name, this.constructor, this.getStart(),
				this.getEnd());

		copyMetrics(mnode);

		return mnode;
	}

	/**
	 * @return
	 */
	public String getParentTypeID() {
		return parentID;
	}

	public void setParentTypeID(String parentID) {
		this.parentID = parentID;
	}

	public List<StatementNode> getStatements() {
		return statements;
	}

	public void addStatement(StatementNode node) {
		statements.add(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected MethodNode clone() throws CloneNotSupportedException {
		return cloneNoChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
}
