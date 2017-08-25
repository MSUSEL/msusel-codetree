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
package edu.montana.gsoc.msusel.node;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.INode;

/**
 * An abstraction representing a subset of a project or system. Specifically a
 * module represents a self-contained section of the software system. A module
 * could theoretically then stand apart and be used in a completely different
 * system.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class ModuleNode extends StructuralNode {

    /**
     * The set of files composing this module
     */
    @Expose
    private Map<String, FileNode> files;

    /**
     * The set of namespaces defined within this module, or partially contained
     * within this module.
     */
    @Expose
    private Map<String, NamespaceNode> namespaces;

    /**
     * Constructs a new ModuleNode with the given qualified identifier.
     * 
     * @param key
     *            Qualified Identifier
     */
    protected ModuleNode(String key)
    {
        super(key);
        files = Maps.newHashMap();
        namespaces = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.MODULE;
    }

    /**
     * Adds the given file to the set of files contained in this module, note
     * that if a file with the same identifier is already present or if the
     * provided file is null, nothing happens.
     * 
     * @param node
     *            FileNode to be added to this Module.
     */
    public void addFile(FileNode node)
    {
        if (node == null)
            return;

        if (files.containsKey(node.getQIdentifier()))
            files.get(node).update(node);
        else
            files.put(node.getQIdentifier(), node);

        node.setParentID(this.getQIdentifier());
    }

    /**
     * Adds a new FileNode to the module with the given absolute path as its
     * qualified identifer. If the provided path is empty or null, then null is
     * returned and nothing happens. On the other hand if a file with the same
     * path already exists in this module, that file is returned. Finally, if
     * neither of previous cases are met, then a new FileNode is created and
     * added to this Module, which is then returned.
     * 
     * @param path
     *            The absoulte path of a File to be added to this Module
     * @return The newly created file, if one with the same path does not
     *         already exists, otherwise that file is returned. In the case that
     *         the provided path is null, empty, or non-existent then null is
     *         returned.
     */
    public FileNode addFile(String path)
    {
        if (path == null || path.isEmpty() || !Files.exists(Paths.get(path)))
            return null;

        if (files.containsKey(path))
            return files.get(path);

        FileNode fn = new FileNode(path);
        files.put(path, fn);

        fn.setParentID(this.getQIdentifier());
        return fn;
    }

    /**
     * Searches for a file with the given path exists in this module. If the
     * provided path is null or empty then null is returned.
     * 
     * @param path
     *            Absolute path of a file to find within this module
     * @return The FileNode with the given path as Qualified Identifier, if such
     *         a file exists in this module. A null is returned if the provided
     *         path is null or empty, or no such file could be found.
     */
    public FileNode getFile(String path)
    {
        if (path == null || path.isEmpty())
            return null;

        return files.get(path);
    }

    /**
     * @return The set of files contained within this module.
     */
    public Set<FileNode> getFiles()
    {
        return Sets.newHashSet(files.values());
    }

    /**
     * @return The set of types defined within the set of files contained within
     *         this module.
     */
    public List<TypeNode> getTypes()
    {
        List<TypeNode> list = Lists.newArrayList();

        for (FileNode fn : files.values())
        {
            list.addAll(fn.getTypes());
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode c)
    {
        if (c == null)
            return;

        if (!(c instanceof ProjectNode))
            return;

        ModuleNode mn = (ModuleNode) c;

        for (FileNode f : mn.getFiles())
        {
            if (getFile(f.getQIdentifier()) != null)
            {
                getFile(f.getQIdentifier()).update(f);
            }
            else
            {
                addFile(f);
            }
        }

        for (String key : c.getMetricNames())
        {
            this.addMetric(key, c.getMetric(key));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModuleNode cloneNoChildren()
    {
        ModuleNode mnode = new ModuleNode(this.qIdentifier);

        copyMetrics(mnode);

        return mnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModuleNode clone() throws CloneNotSupportedException
    {
        ModuleNode mnode = cloneNoChildren();

        for (String key : files.keySet())
        {
            mnode.addFile(files.get(key).clone());
        }

        return mnode;
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
     * Checks whether this module contains a file with the given absolute path
     * name.
     * 
     * @param path
     *            The absolute path of a file to find in the module.
     * @return true if a file exists in this module with the given absolute
     *         path, false otherwise.
     */
    public boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false;

        return files.containsKey(path);
    }

    /**
     * @return The set of all methods defined within the set of types defined
     *         within the set of all files contained within this module.
     */
    public Set<MethodNode> getMethods()
    {
        Set<MethodNode> methods = Sets.newHashSet();
        for (TypeNode tn : getTypes())
        {
            Sets.union(methods, tn.getMethods());
        }

        return methods;
    }

    /**
     * @return The set of namespaces wholly or partially defined within this
     *         module.
     */
    public Set<NamespaceNode> getNamespaces()
    {
        return Sets.newHashSet(namespaces.values());
    }

    /**
     * Searches this module for a namespace with the given qualified identifier.
     * If such a namespace is found it is returned, otherwise null is returned.
     * 
     * @param nsID
     *            Qualified Identifier of the namespace to search for
     * @return The found Namespace, or null if the provided identifier is null
     *         or empty or no such namespace exists within this module.
     */
    public NamespaceNode getNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty() || !namespaces.containsKey(nsID))
            return null;

        return namespaces.get(nsID);
    }

    /**
     * Checks whether a namespace with the given identifier exists within this
     * module.
     * 
     * @param nsID
     *            Qualified Identifier of the namespace to search for.
     * @return true if a namespace with the given identifier is contained in
     *         this module, false otherwise.
     */
    public boolean hasNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty())
            return false;

        return namespaces.containsKey(nsID);
    }

    /**
     * Adds the given namespace to this module, if it is not already added and
     * not null.
     * 
     * @param node
     *            Namespace to be added to this module.
     */
    public void addNamespace(NamespaceNode node)
    {
        if (node == null || namespaces.containsKey(node.getQIdentifier()))
            return;

        namespaces.put(node.getQIdentifier(), node);
        node.setParentID(this.getQIdentifier());
    }

    /**
     * Removes the file with the provided qualified identifier from this
     * ModuleNode, unless the provided identifier is null, empty, or no such
     * file exists in the module.
     * 
     * @param qid
     *            Qualified identifier.
     */
    public void removeFile(String qid)
    {
        if (qid == null || qid.isEmpty() || !files.containsKey(qid))
            return;

        files.get(qid).setParentID(null);
        files.remove(qid);
    }

    /**
     * Constructs a Builder for a ModuleNode with the given Simple Name and
     * Qualified Identifier
     * 
     * @param name
     *            Simple Name
     * @param qID
     *            Qualified Identifier
     * @return The ModuleNode.Builder instance.
     */
    public static Builder builder(String name, String qID)
    {
        return new Builder(name, qID);
    }

    /**
     * Constructs a Builder for a ModuleNode with the given Qualified Identifier
     * 
     * @param qID
     *            Qualified Identifier
     * @return The ModuleNode.Builder instance.
     */
    public static Builder builder(String qID)
    {
        return new Builder(qID);
    }

    /**
     * Builder for Modules implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * The ModuleNode to be constructed.
         */
        private ModuleNode node;

        /**
         * Constructs a Builder for a ModuleNode with the given Simple Name and
         * Qualified Identifier
         * 
         * @param name
         *            Simple Name
         * @param qID
         *            Qualified Identifier
         */
        private Builder(String name, String qID)
        {
            this(qID);
            node.setName(name);
        }

        /**
         * Constructs a Builder for a ModuleNode with the given Qualified
         * Identifier.
         * 
         * @param qID
         *            Qualified Identifier
         */
        private Builder(String qID)
        {
            node = new ModuleNode(qID);
        }

        /**
         * @return The newly constructed ModuleNode
         */
        public ModuleNode create()
        {
            return node;
        }

        /**
         * Add the given file to the ModuleNode under construction
         * 
         * @param file
         *            File to be added
         * @return this
         */
        public Builder file(FileNode file)
        {
            node.addFile(file);

            return this;
        }

        /**
         * Adds the given namespace to the ModuleNode
         * under construction
         * 
         * @param ns
         *            Namespace to add
         * @return this
         */
        @NonNull
        public Builder namespace(NamespaceNode namespace)
        {
            node.addNamespace(namespace);

            return this;
        }

        /**
         * The the metric and measurement value to the ModuleNode under
         * construction.
         * 
         * @param metric
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
         * Sets the parent of the ModuleNode under construction to the
         * provided
         * value.
         * 
         * @param pID
         *            Qualified Identifier of the parent of the ModuleNode under
         *            construction
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
