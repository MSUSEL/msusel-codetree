/**
 * The MIT License (MIT)
 * <p>
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.codetree.node;

import java.util.List;
import java.util.Map;

import edu.montana.gsoc.msusel.codetree.INode;
import edu.montana.gsoc.msusel.codetree.relations.Relationship;
import lombok.Builder;
import lombok.Singular;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private boolean constructor;
    /**
     * Boolean flag indicating that this method is abstract
     */
    @Expose
    @SerializedName("abstract")
    private boolean isAbstract;
    /**
     * Boolean flag indicating that this method is an accessor/mutator method
     */
    @Expose
    @SerializedName("accessor")
    private boolean accessorMethod;
    /**
     * Boolean flag indicating that this method is static
     */
    @Expose
    @SerializedName("static")
    private boolean isStatic;
    /**
     * Boolean flag indicating that this method is final
     */
    @Expose
    @SerializedName("final")
    private boolean isFinal;
    /**
     * The list of contained statements in the body of this method
     */
    @Expose
    private Map<String, StatementNode> statements;
    /**
     * The set of parameters for this method
     */
    @Expose
    private List<Parameter> params;
    /**
     * The return type reference for this method
     */
    @Expose
    @SerializedName("returnType")
    private String returnTypeRef;

    @Builder(buildMethodName = "create")
    protected MethodNode(boolean constructor, boolean isAbstract, boolean accessor, boolean isStatic, boolean isFinal, @Singular Map<String, StatementNode> statements, @Singular List<Parameter> params, String returnType, int start, int end, String name, String identifier, @Singular List<Relationship> relations, String parentID, @Singular Map<String, Double> metrics) {
        super(start, end, name, identifier, relations, parentID, metrics);
        this.constructor = constructor;
        this.isAbstract = isAbstract;
        this.accessorMethod = accessor;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.isFinal = isFinal;
        if (statements != null && !statements.isEmpty())
            this.statements = Maps.newHashMap(statements);
        if (params !=  null && !params.isEmpty())
            this.params = Lists.newArrayList(params);
    }

    /**
     * Constructs a new method with the given qualified identifier and simple
     * name.
     *
     * @param qIdentifier
     *            Qualified Identifier
     * @param name
     *            Simple Name
     */
    public MethodNode(final String qIdentifier, String name) {
        super(qIdentifier, name);
        this.constructor = false;
        this.isAbstract = false;
        this.accessorMethod = false;
        this.isStatic = false;
        this.isFinal = false;
        statements = Maps.newHashMap();
    }


    /**
     * @return True if this method is marked as abstract, false otherwise.
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * @return true if this is an accessor method, false otherwise.
     */
    public boolean isAccessorMethod() {
        return accessorMethod;
    }

    /**
     * @param accessorMethod
     *            The new value of the accessor method flag
     */
    protected void setAccessorMethod(boolean accessorMethod) {
        this.accessorMethod = accessorMethod;
    }

    /**
     * @param isAbstract
     *            The new value of the abstract method flag
     */
    protected void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * @return true if this method is a constructor/initializer, false
     *         otherwise.
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return INodeType.METHOD;
    }

    /**
     * @param constructor
     *            The new value of the constructor flag.
     */
    protected void setConstructor(final boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * @return true if this method has been marked static, false otherwise.
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * @param isStatic
     *            The new value of the static flag.
     */
    protected void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * @return true if this method has been marked final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * @param isFinal
     *            The new value of the final flag.
     */
    protected void setFinal(final boolean isFinal) {
        this.isFinal = isFinal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode m) {
        if (m == null)
            return;

        if (!(m instanceof MethodNode))
            return;

        MethodNode node = (MethodNode) m;
        this.setRange(node.getStart(), node.getEnd());

        setConstructor(node.isConstructor());

        for (String key : m.getMetricNames()) {
            this.metrics.put(key, m.getMetric(key));
        }

        for (StatementNode stmt : node.getStatements()) {
            if (getStatement(stmt.getQIdentifier()) != null) {
                getStatement(stmt.getQIdentifier()).update(stmt);
            } else {
                addStatement(stmt);
            }
        }
    }

    /**
     * Returns a statement with the given identifier contained in this method
     *
     * @param identifier
     *            Identifier of the statement requested
     * @return StatementNode contained in this method with the given identifier,
     *         or null otherwise
     * @throws IllegalArgumentException
     *             if provided identifier is null or the empty string
     */
    public StatementNode getStatement(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new IllegalArgumentException("Statement identifier cannot be null or empty");

        return statements.get(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode cloneNoChildren() {
        MethodNode node = new MethodNode(this.getQIdentifier(), this.getName());
        node.setConstructor(this.isConstructor());
        node.setAbstract(this.isAbstract());
        node.setAccessorMethod(this.isAccessorMethod());
        node.setRange(this.getStart(), this.getEnd());

        copyMetrics(node);

        return node;
    }

    /**
     * @return The set of statements contained within this method.
     */
    public List<StatementNode> getStatements() {
        return Lists.newArrayList(statements.values());
    }

    /**
     * Adds the given statement node to this method, unless that statement is
     * empty.
     *
     * @param node
     *            StatementNode to add to this method.
     */
    public void addStatement(StatementNode node) {
        if (node == null)
            return;
        if (statements.containsKey(node.getQIdentifier()))
            return;

        statements.put(node.getQIdentifier(), node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodNode clone() throws CloneNotSupportedException {
        return cloneNoChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Adds the given parameter to this methods parameter list.
     *
     * @param param
     *            Parameter to add
     */
    public void addParameter(Parameter param) {
        if (param == null)
            return;

        params.add(param);

        updateName();
    }

    /**
     * @return The list of parameters contained in this method.
     */
    public List<Parameter> getParams() {
        return Lists.newArrayList(params);
    }

    /**
     * Updates the name and qualified identifier after a change in the number of
     * parameters
     */
    private void updateName() {
        String simpleName = getName().split("\\(")[0];

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(simpleName);
        nameBuilder.append("(");

        StringBuilder paramBuilder = new StringBuilder();
        params.forEach((param) -> paramBuilder.append(param.getTypeRef()).append(", "));

        String paramList = paramBuilder.toString().trim();
        paramList = paramList.substring(0, paramList.length() - 1);

        nameBuilder.append(paramList);
        nameBuilder.append(")");

        setName(nameBuilder.toString());
        setQIdentifier(getQIdentifier().substring(0, getQIdentifier().lastIndexOf("#")) + "#" + getName());
    }
}
