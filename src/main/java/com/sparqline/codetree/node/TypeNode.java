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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;
import com.sparqline.codetree.INode;

/**
 * An abstraction of a type. Currently a type is based on the notion from the
 * Object-Oriented paradigm and can be either a Class or an Interface. Types can
 * contain a set of Fields and a set of Methods. The unique qualified name for a
 * type is a combination of its package.type name, whereas its name is simply
 * the type name.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class TypeNode extends CodeNode {

    /**
     * Map of method nodes indexed by their qualified name
     */
    @Expose
    private final Map<String, MethodNode> methods = Maps.newHashMap();
    /**
     * Map of field nodes indexed by their qualified name
     */
    @Expose
    private final Map<String, FieldNode>  fields  = Maps.newHashMap();
    /**
     * Boolean indicator that when true indicates that this type is an
     * interface, false it is a class
     */
    @Expose
    private boolean                       isInterface;
    /**
     * Boolean indicator that when true indicates that this type is abstract,
     * false it is not abstract
     */
    @Expose
    private boolean                       isAbstract;

    /**
     * Constructs a new empty type with the given qualified identifier, simple
     * identifier, start and end lines
     * 
     * @param qIdentifier
     *            The Unique Qualified identifier for this type
     *            (package-name.typename)
     * @param identifier
     *            The Simple identifier for this type (typename)
     */
    protected TypeNode(final String qIdentifier, final String identifier)
    {
        super(qIdentifier, identifier);
    }

    /**
     * @param method
     *            Method node to be removed from this type, if null or not
     *            contained, nothing happens
     */
    public void removeMethod(final MethodNode method)
    {
        if (method == null || !methods.containsKey(method.getQIdentifier()))
        {
            return;
        }

        methods.remove(method.getQIdentifier());

        method.setParentID(null);
    }

    /**
     * Retrieves the method contained in this type at the given line in the file
     * in which this type is declared. If the value of the line is outside the
     * range of the of the defined start and end values of this type, null is
     * returned.
     * 
     * @param line
     *            Line to search for a method at
     * @return The method containing the line specified in this type, or null if
     *         no method is defined at that line or the value of line is outside
     *         the range(start, end)
     */
    public MethodNode getMethod(final int line)
    {
        if (line >= getStart() && line <= getEnd())
        {
            Collection<MethodNode> nodes = methods.values();
            for (MethodNode node : nodes)
            {
                if (node.containsLine(line))
                {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * Searches this type for a method with the given name, where here we are
     * assuming the simple name and not the qualified identifier. In the case
     * that multiple methods exist with the same name (overloaded) then the
     * first method found will be returned.
     * 
     * @param name
     *            Simple name of the method being searched for.
     * @return Method node with name matching the given name. Null if the name
     *         is null or empty, or if no such method can be found.
     */
    public MethodNode getMethod(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (methods.containsKey(this.qIdentifier + "#" + name))
            return methods.get(this.qIdentifier + "#" + name);

        return addMethod(name);
    }

    /**
     * @param name
     *            Name of new method to add to this TypeNode
     * @return MethodNode associated with the given name, or null if the
     *         provided name is null or empty.
     */
    public MethodNode addMethod(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (methods.containsKey(name))
            return methods.get(name);

        MethodNode m = new MethodNode(this.qIdentifier + "#" + name, name);
        methods.put(name, m);
        m.setParentID(this.getQIdentifier());
        return m;
    }

    /**
     * @param method
     *            MethodNode to be added to this type
     * @return true if the provided method node was successfully added, false if
     *         the method is null
     * @throws IllegalArgumentException
     *             if the method's range is outside this types range
     */
    public boolean addMethod(final MethodNode method)
    {
        if (method == null)
        {
            return false;
        }

        if (method.getStart() < getStart() || method.getEnd() > getEnd())
        {
            throw new IllegalArgumentException(
                    "A method's start cannot be less than the type's start line, and a method's end cannot exceed a type's end line.");
        }

        methods.put(method.getQIdentifier(), method);
        method.setParentID(this.getQIdentifier());

        return true;
    }

    /**
     * @return The set of methods contained in this TypeNode
     */
    public Set<MethodNode> getMethods()
    {
        return Sets.newHashSet(methods.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.TYPE;
    }

    /**
     * @param field
     *            FieldNode to be removed
     */
    public void removeField(final FieldNode field)
    {
        if (field == null)
        {
            return;
        }

        fields.remove(field.getQIdentifier());
        field.setParentID(null);
    }

    /**
     * Search this type for a Field at the provided line
     * 
     * @param line
     *            Line to search for a field in
     * @return The FieldNode at the given line, or null if no such field exists.
     */
    public FieldNode getField(final int line)
    {
        if (line >= getStart() && line <= getEnd())
        {
            Collection<FieldNode> nodes = fields.values();
            for (final FieldNode node : nodes)
            {
                if (node.containsLine(line))
                {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * Adds the given FieldNode to the set of fields in this type, if the given
     * FieldNode is not null or outside the range of this type.
     * 
     * @param field
     *            The new field node to add to the set of fields contained in
     *            this type.
     * @return true if the field node was able to be added, false if it was
     *         null.
     * @throws IllegalArgumentException
     *             if the start line of the field is outside the range of this
     *             type
     */
    public boolean addField(final FieldNode field)
    {
        if (field == null)
        {
            return false;
        }

        if (field.getStart() < getStart() || field.getEnd() > getEnd())
        {
            throw new IllegalArgumentException(
                    "A field's start cannot be less than the type's start line, and a field's end cannot exceed a type's end line.");
        }

        if (!fields.containsKey(field.getQIdentifier()))
        {
            fields.put(field.getQIdentifier(), field);
            field.setParentID(this.getQIdentifier());
        }

        return true;
    }

    /**
     * @return The set of fields contained in this type
     */
    public Set<FieldNode> getFields()
    {
        return Sets.newHashSet(fields.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        if (obj instanceof TypeNode)
        {
            TypeNode other = (TypeNode) obj;

            if (!other.qIdentifier.equals(qIdentifier))
                return false;
        }

        return true && super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode t)
    {
        if (t == null)
            return;

        if (!(t instanceof TypeNode))
            return;

        TypeNode type = (TypeNode) t;

        setRange(type.getStart(), type.getEnd());

        for (MethodNode m : type.getMethods())
        {
            if (getMethod(m.getQIdentifier()) != null)
            {
                getMethod(m.getQIdentifier()).update(m);
            }
            else
            {
                addMethod(m);
            }
        }

        for (FieldNode f : type.getFields())
        {
            if (getField(f.getQIdentifier()) != null)
            {
                getField(f.getQIdentifier()).update(f);
            }
            else
            {
                addField(f);
            }
        }

        for (String key : t.getMetricNames())
        {
            this.metrics.put(key, t.getMetric(key));
        }
    }

    /**
     * Search this type for a field node with a matching qualified identifier.
     * 
     * @param qid
     *            The Qualified identifier of the field being searched for.
     * @return The field node requested or null if no matching field node could
     *         be found or the provided identifier is null or empty.
     */
    private FieldNode getField(String qid)
    {
        FieldNode retVal = null;

        if (qid != null && !qid.isEmpty() && fields.containsKey(qid))
            retVal = fields.get(qid);

        return retVal;
    }

    /**
     * Adds a new field with the given simple name to this type by creating such
     * field. If this field already exists the existing field is returned.
     * 
     * @param name
     *            Simple name of the new field to be added to this type.
     * @return the newly created field or null if the provided name is null or
     *         empty
     */
    public FieldNode addField(final String name)
    {
        FieldNode retVal = null;
        if (name != null && !name.isEmpty())
        {

            if (fields.containsKey(this.qIdentifier + "#" + name))
            {
                retVal = fields.get(name);
            }
            else
            {
                FieldNode f = new FieldNode(this.qIdentifier + "#" + name, name);
                fields.put(name, f);
                f.setParentID(this.getQIdentifier());
            }
        }

        return retVal;
    }

    /**
     * Tests whether the given methodNode is contained within this type
     * 
     * @param node
     *            Node to search this type for.
     * @return true if the provided node is a method contained in this type,
     *         false otherwise.
     */
    public boolean hasMethod(MethodNode node)
    {
        if (node != null)
            return methods.containsKey(node.getQIdentifier());
        else
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeNode cloneNoChildren()
    {
        TypeNode tnode = new TypeNode(qIdentifier, name);
        tnode.setStart(getStart());
        tnode.setEnd(getEnd());

        copyMetrics(tnode);

        return tnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TypeNode clone() throws CloneNotSupportedException
    {
        TypeNode tnode = cloneNoChildren();

        for (String key : fields.keySet())
        {
            tnode.addField(fields.get(key).clone());
        }

        for (String key : methods.keySet())
        {
            tnode.addMethod(methods.get(key).clone());
        }

        return tnode;
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
     * @return True if this is an interface, false otherwise.
     */
    public boolean isInterface()
    {
        return isInterface;
    }

    /**
     * @param isInterface
     *            New value for isInterface field
     */
    private void setInterface(boolean isInterface)
    {
        this.isInterface = isInterface;
    }

    /**
     * @return True if this type is abstract, false otherwise.
     */
    public boolean isAbstract()
    {
        return isAbstract;
    }

    /**
     * @param isAbstract
     *            New value for isAbstract field
     */
    private void setAbstract(boolean isAbstract)
    {
        this.isAbstract = isAbstract;
    }

    /**
     * Builder for Types implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * The TypeNode to be constructed.
         */
        private TypeNode node;

        /**
         * Creates a new Builder for a TypeNode with the given simple name and
         * qualified identifier
         * 
         * @param name
         *            Simple Name
         * @param qID
         *            Qualified Identifier
         */
        public Builder(String name, String qID)
        {
            node = new TypeNode(qID, name);
        }

        /**
         * @return The newly constructed TypeNode
         */
        @NonNull
        public TypeNode create()
        {
            return node;
        }

        /**
         * Sets the TypeNode under construction as Abstract
         * 
         * @return this
         */
        @NonNull
        public Builder isAbstract()
        {
            node.setAbstract(true);

            return this;
        }

        /**
         * Sets the TypeNode under construction as an Interface
         * 
         * @return this
         */
        @NonNull
        public Builder isInterface()
        {
            node.setInterface(true);

            return this;
        }

        /**
         * Sets the range of the TypeNode under construction (within its
         * containing file) to be between the provided starting line and ending
         * line (inclusive).
         * 
         * @param start
         *            Staring line
         * @param end
         *            Ending line
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
         * Adds the given metric and measurement value to the TypeNode under
         * construction
         * 
         * @param metric
         * @param value
         * @return
         */
        @NonNull
        public Builder metric(String metric, Double value)
        {
            node.addMetric(metric, value);

            return this;
        }

        /**
         * Adds the given field to the TypeNode under construction.
         * 
         * @param field
         *            Field to be added
         * @return this
         */
        @NonNull
        public Builder field(FieldNode field)
        {
            node.addField(field);

            return this;
        }

        /**
         * Adds the given method to the TypeNode under construction.
         * 
         * @param method
         *            Method to be added
         * @return this
         */
        @NonNull
        public Builder method(MethodNode method)
        {
            node.addMethod(method);

            return this;
        }

        /**
         * Sets the parent qualified identifier of the TypeNode under
         * construction to the given value.
         * 
         * @param pID
         *            The Qualified Identifier of the parent of the TypeNode
         *            under construction
         * @return this
         */
        @NonNull
        public Builder parent(String pID)
        {
            node.setParentID(pID);

            return this;
        }
    }
}