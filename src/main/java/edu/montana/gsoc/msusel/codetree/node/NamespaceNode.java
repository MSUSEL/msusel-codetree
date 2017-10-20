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

import java.util.Map;
import java.util.Set;

import edu.montana.gsoc.msusel.codetree.INode;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Isaac Griffith
 */
public class NamespaceNode extends StructuralNode {

    /**
     * set of type references defined within this namespace.
     */
    @Expose
    private Set<TypeNode>              types;
    /**
     * Map of sub-namespaces defined within the confines of this namespace,
     * indexed by their qualified identifier.
     */
    @Expose
    @SerializedName("namespaces")
    private Map<String, NamespaceNode> subNS;

    /**
     * Constructs a new NamespaceNode with the given qualified identifier.
     * 
     * @param key
     *            Qualified Identifier
     */
    public NamespaceNode(String key)
    {
        super(key);
        types = Sets.newHashSet();
        subNS = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.NAMESPACE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode c)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode cloneNoChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return The set of types contained within this namespace.
     */
    public Set<TypeNode> getTypes()
    {
        return types;
    }

    /**
     * Adds a new type to this namespace. If the provided identifier is null or
     * empty, nothing happens.
     * 
     * @param qid
     *            Qualified Identifier of a type defined within this namespace.
     */
    public void addType(TypeNode qid)
    {
        if (qid == null)
            return;

        types.add(qid);
    }

    /**
     * Adds the given namespace as a sub-namespace of this namespace, assuming
     * that the provided namespace is not already contained or null.
     * 
     * @param ns
     *            Namespace to be added.
     */
    public void addSubNamespace(NamespaceNode ns)
    {
        if (ns == null || subNS.containsKey(ns.getQIdentifier()))
            return;

        ns.setParentID(this.getQIdentifier());
        subNS.put(ns.getQIdentifier(), ns);
    }

    /**
     * Adds a sub-namespace to this namespace by creating a new namespace with
     * the given qualified identifier. This assumes the provided identifier is
     * neither null nor empty and that no such namespace with the given id is
     * already contained.
     * 
     * @param qid
     *            The qualified identifier of a new sub-namespace to add.
     * @return The newly created and added namespace, an existing namespace with
     *         the same provided qualified identifier, or null if the provided
     *         identifier is null or empty.
     */
    public NamespaceNode addSubNamespace(String qid)
    {
        if (qid == null || qid.isEmpty())
            return null;

        if (subNS.containsKey(qid))
        {
            return subNS.get(qid);
        }

        NamespaceNode ns = new NamespaceNode(qid);
        ns.setParentID(this.getQIdentifier());
        subNS.put(qid, ns);

        return ns;
    }

    /**
     * Constructs a new Builder for a NamespaceNode with the given qualified
     * identifier
     * 
     * @param qID
     *            Qualified Identifier of the File
     * @return The NamespaceNode.Builder instance
     */
    public static Builder builder(String qID)
    {
        return new Builder(qID);
    }

    /**
     * Builder for Namespaces implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * The NamespaceNode to be constructed by this Builder
         */
        private NamespaceNode node;

        /**
         * Constructs a new Builder for a NamespaceNode with the given qualified
         * identifier
         * 
         * @param qID
         *            Qualified Identifier of the File
         */
        private Builder(String qID)
        {
            node = new NamespaceNode(qID);
        }

        /**
         * @return The Constructed NamespaceNode
         */
        @NonNull
        public NamespaceNode create()
        {
            return node;
        }

        /**
         * The the metric and measurement value to the NamespaceNode under
         * construction.
         * 
         * @param metric
         *            Metric name
         * @param value
         *            Measurement value
         * @return this
         */
        @NonNull
        public Builder metric(String metric, Double value)
        {
            node.addMetric(metric, value);

            return this;
        }

        /**
         * Adds the given type to the NamespaceNode under construction
         * 
         * @param type
         *            Type to add
         * @return this
         */
        @NonNull
        public Builder type(TypeNode type)
        {
            node.addType(type);

            return this;
        }

        /**
         * Adds the given namespace as a sub-namespace to the NamespaceNode
         * under construction
         * 
         * @param ns
         *            Namespace to add
         * @return this
         */
        @NonNull
        public Builder namespace(NamespaceNode ns)
        {
            node.addSubNamespace(ns);

            return this;
        }

        /**
         * Sets the parent of the NamespaceNode under construction to the
         * provided
         * value.
         * 
         * @param pID
         *            Qualified Identifier of the parent of the NamespaceNode
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
