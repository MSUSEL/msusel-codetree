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

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sparqline.codetree.INode;

/**
 * An abstraction of a type method. A method's unique qualified name is similar
 * to a fields but contains a parenthesized listing of the parameter types
 * associated with the method. Each method contains the following: a set of
 * parameters, and a set of statements. A method also has a return type
 * associated with it. Each parameter is simply a pair consisting of a type and
 * a name.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class MethodNode extends CodeNode {

    /**
     * Boolean flag indicating that this method is a constructor/initialization
     * method
     */
    @Expose
    private boolean             constructor;
    /**
     * Boolean flag indicating that this method is abstract
     */
    @Expose
    @SerializedName("abstract")
    private boolean             isAbstract;
    /**
     * Boolean flag indicating that this method is an accessor/mutator method
     */
    @Expose
    @SerializedName("accessor")
    private boolean             accessorMethod;
    /**
     * Boolean flag indicating that this method is static
     */
    @Expose
    @SerializedName("static")
    private boolean             isStatic;
    /**
     * Boolean flag indicating that this method is final
     */
    @Expose
    @SerializedName("final")
    private boolean             isFinal;
    /**
     * The list of contained statements in the body of this method
     */
    @Expose
    private List<StatementNode> statements;
    /**
     * The set of parameters for this method
     */
    @Expose
    private List<Parameter>     params;
    /**
     * The return type reference for this method
     */
    @Expose
    @SerializedName("returnType")
    private String              returnTypeRef;

    /**
     * Constructs a new method with the given qualified identifier and simple
     * name.
     * 
     * @param identifier
     *            Qualified Identifier
     * @param name
     *            Simple Name
     */
    public MethodNode(final String qIdentifier, String name)
    {
        super(qIdentifier, name);
        this.constructor = false;
        this.isAbstract = false;
        this.accessorMethod = false;
        statements = Lists.newArrayList();
    }

    /**
     * @return True if this method is marked as abstract, false otherwise.
     */
    public boolean isAbstract()
    {
        return isAbstract;
    }

    /**
     * @return true if this is an accessor method, false otherwise.
     */
    public boolean isAccessorMethod()
    {
        return accessorMethod;
    }

    /**
     * @param accessorMethod
     *            The new value of the accessor method flag
     */
    protected void setAccessorMethod(boolean accessorMethod)
    {
        this.accessorMethod = accessorMethod;
    }

    /**
     * @param isAbstract
     *            The new value of the abstract method flag
     */
    protected void setAbstract(boolean isAbstract)
    {
        this.isAbstract = isAbstract;
    }

    /**
     * @return true if this method is a constructor/initializer, false
     *         otherwise.
     */
    public boolean isConstructor()
    {
        return constructor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.METHOD;
    }

    /**
     * @param constructor
     *            The new value of the constructor flag.
     */
    protected void setConstructor(final boolean constructor)
    {
        this.constructor = constructor;
    }

    /**
     * @return true if this method has been marked static, false otherwise.
     */
    public boolean isStatic()
    {
        return isStatic;
    }

    /**
     * @param isStatic
     *            The new value of the static flag.
     */
    protected void setStatic(boolean isStatic)
    {
        this.isStatic = isStatic;
    }

    /**
     * @return true if this method has been marked final, false otherwise.
     */
    public boolean isFinal()
    {
        return isFinal;
    }

    /**
     * @param isFinal
     *            The new value of the final flag.
     */
    protected void setFinal(final boolean isFinal)
    {
        this.isFinal = isFinal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode m)
    {
        if (m == null)
            return;

        if (!(m instanceof MethodNode))
            return;

        MethodNode node = (MethodNode) m;
        setStart(node.getStart());
        setEnd(node.getEnd());

        setConstructor(node.isConstructor());

        for (String key : m.getMetricNames())
        {
            this.metrics.put(key, m.getMetric(key));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode cloneNoChildren()
    {
        MethodNode mnode = new MethodNode(this.qIdentifier, this.name);
        mnode.setConstructor(this.isConstructor());
        mnode.setAbstract(this.isAbstract());
        mnode.setAccessorMethod(this.isAccessorMethod());
        mnode.setRange(this.getStart(), this.getEnd());

        copyMetrics(mnode);

        return mnode;
    }

    /**
     * @return The set of statements contained within this method.
     */
    public List<StatementNode> getStatements()
    {
        return statements;
    }

    /**
     * Adds the given statement node to this method, unless that statement is
     * empty.
     * 
     * @param node
     *            StatementNode to add to this method.
     */
    public void addStatement(StatementNode node)
    {
        if (node == null)
            return;

        statements.add(node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode clone() throws CloneNotSupportedException
    {
        return cloneNoChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    /**
     * Adds the given parameter to this methods parameter list.
     * 
     * @param param
     *            Parameter to add
     */
    public void addParameter(Parameter param)
    {
        if (param == null)
            return;

        params.add(param);

        updateName();
    }

    /**
     * @return The list of parameters contained in this method.
     */
    public List<Parameter> getParams()
    {
        return Lists.newArrayList(params);
    }

    /**
     * Updates the name and qualified identifier after a change in the number of
     * parameters
     */
    private void updateName()
    {
        String simpleName = name.split("(")[0];

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(simpleName);
        nameBuilder.append("(");

        StringBuilder paramBuilder = new StringBuilder();
        params.forEach((param) -> paramBuilder.append(param.getTypeRef() + ", "));

        String paramList = paramBuilder.toString().trim();
        paramList = paramList.substring(0, paramList.length() - 1);

        nameBuilder.append(paramList);
        nameBuilder.append(")");

        name = nameBuilder.toString();
        qIdentifier = qIdentifier.substring(0, qIdentifier.lastIndexOf("#")) + "#" + name;
    }

    /**
     * Constructs a new Builder for a MethodNode with the given initial
     * simple name and qualified identifier
     * 
     * @param name
     *            Initial simple name
     * @param qID
     *            Initial qualified identifier
     * @return The MethodNode.Builder instance
     */
    public static Builder builder(String name, String qID)
    {
        return new Builder(name, qID);
    }

    /**
     * Builder for Methods implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * The MethodNode to be constructed by this Builder
         */
        private MethodNode node;

        /**
         * Constructs a new Builder for a MethodNode with the given initial
         * simple name and qualified identifier
         * 
         * @param name
         *            Initial simple name
         * @param qID
         *            Initial qualified identifier
         */
        private Builder(String name, String qID)
        {
            node = new MethodNode(qID, name);
        }

        /**
         * @return The newly constructed methodnode
         */
        @NonNull
        public MethodNode create()
        {
            return node;
        }

        /**
         * Sets the abstract flag to true and the constructor and final flags to
         * false.
         * 
         * @return this
         */
        @NonNull
        public Builder isAbstract()
        {
            return isAbstract(true);
        }

        /**
         * Sets the abstract flag to the provided value and the constructor and
         * final flags to false, if the provided value is true.
         * 
         * @param abs
         *            The new value of the abstract flag.
         * @return this
         */
        public Builder isAbstract(boolean abs)
        {
            node.setAbstract(true);

            if (abs)
            {
                node.setConstructor(false);
                node.setFinal(false);
            }

            return this;
        }

        /**
         * Sets the constructor flag to true
         * 
         * @return this
         */
        @NonNull
        public Builder constructor()
        {
            return constructor(true);
        }

        /**
         * Sets the constructor flag to the provided value, and if true, sets
         * the abstract flag to false.
         * 
         * @param con
         *            The new value of the constructor flag
         * @return this
         */
        @NonNull
        public Builder constructor(boolean con)
        {
            node.setConstructor(con);

            if (con)
            {
                node.setAbstract(false);
            }

            return this;
        }

        /**
         * Sets the accessor method flag to true and the constructor flag to
         * false
         * 
         * @return this
         */
        @NonNull
        public Builder accessor()
        {
            return accessor(true);
        }

        /**
         * Sets the accessor method flag to the provided value and the
         * constructor flag to false if the provided value is true.
         * 
         * @param acc
         *            The new value of the accessor method flag.
         * @return this
         */
        @NonNull
        public Builder accessor(boolean acc)
        {
            node.setAccessorMethod(acc);

            if (acc)
            {
                node.setConstructor(false);
            }

            return this;
        }

        /**
         * Sets the final flag to true and the abstract flag to false.
         * 
         * @return this
         */
        @NonNull
        public Builder isFinal()
        {
            return isFinal(true);
        }

        /**
         * Sets the final flag to the provided value and the abstract flag to
         * false, if the provided value is true.
         * 
         * @param fin
         *            The new value of the final flag.
         * @return this
         */
        @NonNull
        public Builder isFinal(boolean fin)
        {
            node.setFinal(fin);

            if (fin)
                node.setAbstract(false);

            return this;
        }

        /**
         * Sets the static flag to true and the abstract, accessor method,
         * constructor, and final flags to false.
         * 
         * @return this
         */
        @NonNull
        public Builder isStatic()
        {
            return isStatic(true);
        }

        /**
         * Sets the static flag to the provided value and the abstract, accessor
         * method, constructor, and final flags to false, if the provided value
         * is true.
         * 
         * @param stat
         *            The new value of the static flag.
         * @return this
         */
        @NonNull
        public Builder isStatic(boolean stat)
        {
            node.setStatic(true);

            if (stat)
            {
                node.setAbstract(false);
                node.setAccessorMethod(false);
                node.setConstructor(false);
                node.setFinal(false);
            }

            return this;
        }

        /**
         * Sets the line range of the method to be constructed to the single
         * line provided.
         * 
         * @param start
         *            The line on which this method is found.
         * @return this
         */
        @NonNull
        public Builder range(int start)
        {
            node.setStart(start);
            node.setEnd(start);

            return this;
        }

        /**
         * Sets the line range of the method to be constructed to be between the
         * values provided (inclusive).
         * 
         * @param start
         *            Start line of the range
         * @param end
         *            End line of the range
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
         * Adds the metric and measurement value to the method to be
         * constructed.
         * 
         * @param name
         *            Metric name
         * @param value
         *            Measurement value
         * @return this
         */
        @NonNull
        public Builder metric(String name, Double value)
        {
            node.addMetric(name, value);

            return this;
        }

        /**
         * Adds the provided statment to the method under construction.
         * 
         * @param stmt
         *            Statement to be added
         * @return this
         */
        @NonNull
        public Builder statement(StatementNode stmt)
        {
            node.addStatement(stmt);

            return this;
        }

        /**
         * Adds the provided parameter to the method under constructions
         * parameter list. Note that this will updated both the method's name
         * and qualified identifier.
         * 
         * @param param
         *            Parameter to be added.
         * @return this
         */
        @NonNull
        public Builder parameter(Parameter param)
        {
            node.addParameter(param);

            return this;
        }

        /**
         * Sets the parent id for the method under construction.
         * 
         * @param pID
         *            Parent's qualified identifier.
         * @return this.
         */
        @NonNull
        public Builder parent(String pID)
        {
            node.setParentID(pID);

            return this;
        }
    }
}
