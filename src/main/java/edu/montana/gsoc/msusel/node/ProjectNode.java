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
package edu.montana.gsoc.msusel.node;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.INode;
import edu.montana.gsoc.msusel.json.FieldNodeDeserializer;
import edu.montana.gsoc.msusel.json.FileNodeDeserializer;
import edu.montana.gsoc.msusel.json.MethodNodeDeserializer;
import edu.montana.gsoc.msusel.json.ModuleNodeDeserializer;
import edu.montana.gsoc.msusel.json.ProjectNodeDeserializer;
import edu.montana.gsoc.msusel.json.TypeNodeDeserializer;

/**
 * An abstraction representing the project. Each ProjectNode can contain a set
 * of subprojects, a set of
 * files, and a set of modules.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class ProjectNode extends StructuralNode {

    /**
     * Map of subprojects keyed by their qualified id
     */
    @Expose
    private Map<String, ProjectNode>   subprojects;
    /**
     * Map of modules keyed by their qualified id
     */
    @Expose
    private Map<String, ModuleNode>    modules;
    /**
     * Map of files keyed by their qualified id
     */
    @Expose
    private Map<String, FileNode>      files;
    /**
     * Map of namespaces keyed by their qualified id
     */
    @Expose
    private Map<String, NamespaceNode> namespaces;

    /**
     * Constructs a new empty project with the provided qIdentifier
     * 
     * @param qIdentifier
     *            The unique qualified identifier of this project
     */
    protected ProjectNode(String qIdentifier)
    {
        super(qIdentifier);
        subprojects = Maps.newHashMap();
        modules = Maps.newHashMap();
        files = Maps.newHashMap();
        namespaces = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.PROJECT;
    }

    /**
     * @param node
     *            Subproject to be added to this projectO
     */
    public void addSubProject(ProjectNode node)
    {
        if (node == null)
            return;

        if (subprojects.containsKey(node.getQIdentifier()))
            subprojects.get(node.getQIdentifier()).update(node);
        else
            subprojects.put(node.getQIdentifier(), node);

        node.setParentID(this.qIdentifier);
    }

    /**
     * @param key
     *            Unique qualified identifier for a project to be created and
     *            added to this project
     * @return The newly created project
     */
    public ProjectNode addSubProject(String key)
    {
        if (key == null || key.isEmpty())
            return null;

        if (subprojects.containsKey(key))
            return subprojects.get(key);

        ProjectNode node = new ProjectNode(key);
        subprojects.put(key, node);
        node.setParentID(this.qIdentifier);
        return node;
    }

    /**
     * @param key
     *            Unique qualified id used to search for a subproject of this
     *            project
     * @return The project with the given key, or null if no such project is
     *         contained in this project or if the key was null or empty.
     */
    public ProjectNode getSubProject(String key)
    {
        if (key == null || key.isEmpty())
            return null;

        return subprojects.get(key);
    }

    /**
     * @return Set of subprojects contained in this project
     */
    public Set<ProjectNode> getSubProjects()
    {
        return Sets.newHashSet(subprojects.values());
    }

    /**
     * @param id
     *            Unique qualified id of the project to be removed from this
     *            project
     * @return Project that was removed from this project, or null if no such
     *         project exists or the provided key was null or empty.
     */
    public ProjectNode removeSubProject(String id)
    {
        if (id == null || id.isEmpty())
            return null;

        if (subprojects.containsKey(id))
        {
            ProjectNode n = subprojects.remove(id);
            n.setParentID(null);
            return n;
        }

        return null;
    }

    /**
     * @param node
     *            Project to be removed as a subproject of this project
     * @return the removed project nod or null if the provided project was null
     */
    public ProjectNode removeSubProject(ProjectNode node)
    {
        if (node == null)
            return node;

        if (subprojects.containsKey(node.getQIdentifier()))
        {
            ProjectNode n = subprojects.remove(node.getQIdentifier());
            n.setParentID(null);
            return n;
        }

        return node;
    }

    /**
     * @param node
     *            Module to be added to this project
     */
    public void addModule(ModuleNode node)
    {
        if (node == null)
            return;

        if (modules.containsKey(node.getQIdentifier()))
            modules.get(node.getQIdentifier()).update(node);
        else
            modules.put(node.getQIdentifier(), node);
    }

    /**
     * @param key
     *            Unique qualified identifier of a new module to add to this
     *            project.
     * @return The newly created module or an existing one if the key is already
     *         in use, or null if the provided identifier was null or empty.
     */
    public ModuleNode addModule(String key)
    {
        if (key == null || key.isEmpty())
            return null;

        if (modules.containsKey(key))
            return modules.get(key);

        ModuleNode node = new ModuleNode(key);
        modules.put(key, node);
        return node;
    }

    /**
     * Searches this project for a module with the given unique qualified
     * identifier.
     * 
     * @param key
     *            Unique qualified identifier of a module to find within this
     *            project.
     * @return The module with the matching unique identifier as that provided,
     *         or null if no such module is contained in the project or the
     *         provided identifier was null or empty.
     */
    public ModuleNode getModule(String key)
    {
        if (key == null || key.isEmpty())
            return null;

        return modules.get(key);
    }

    /**
     * @return The set of modules contained in this project.
     */
    public Set<ModuleNode> getModules()
    {
        return Sets.newHashSet(modules.values());
    }

    /**
     * @param node
     *            File to be added to this project.
     */
    public void addFile(FileNode node)
    {
        if (node == null)
            return;

        if (files.containsKey(node.getQIdentifier()))
            files.get(node.getQIdentifier()).update(node);
        else
            files.put(node.getQIdentifier(), node);

        node.setParentID(this.qIdentifier);
    }

    /**
     * @param path
     *            Absolute path of a new file to be created and added to this
     *            project.
     * @return The newly created file or an existing file, or null if the
     *         provided path is null or empty or the file does not exist.
     */
    public FileNode addFile(String path)
    {
        if (path == null || path.isEmpty() || !Files.exists(Paths.get(path)))
            return null;

        if (files.containsKey(path))
            return files.get(path);

        FileNode fn = new FileNode(path);
        files.put(path, fn);

        fn.setParentID(this.parentID);

        return fn;
    }

    /**
     * Search this project for a file with the matching absolute path
     * 
     * @param path
     *            Path of file to find
     * @return The file with matching path if it is contained in this project,
     *         or null if the path is empty, null, or
     */
    public FileNode getFile(String path)
    {
        if (path == null || path.isEmpty())
            return null;

        return files.get(path);
    }

    /**
     * @return Set of files contained in this Project
     */
    public Set<FileNode> getFiles()
    {
        return Sets.newHashSet(files.values());
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

        ProjectNode pn = (ProjectNode) c;

        for (ModuleNode m : pn.getModules())
        {
            if (getModule(m.getQIdentifier()) != null)
            {
                getModule(m.getQIdentifier()).update(m);
            }
            else
            {
                addModule(m);
            }
        }

        for (ProjectNode p : pn.getSubProjects())
        {
            if (getSubProject(p.getQIdentifier()) != null)
            {
                getSubProject(p.getQIdentifier()).update(p);
            }
            else
            {
                addSubProject(p);
            }
        }

        for (FileNode f : pn.getFiles())
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

        for (String key : pn.getMetricNames())
        {
            this.addMetric(key, pn.getMetric(key));
        }
    }

    /**
     * @return Set of file paths for the files contained in this project.
     */
    public Set<String> getFileKeys()
    {
        return files.keySet();
    }

    /**
     * @param file
     *            Path of file to be removed from this project.
     */
    public void removeFile(String file)
    {
        if (file == null || file.isEmpty())
            return;

        files.remove(file);
    }

    /**
     * @return The set of all types contained in the modules,
     *         subprojects, and files contained within this project.
     */
    public List<TypeNode> getTypes()
    {
        List<TypeNode> list = Lists.newArrayList();

        for (ProjectNode pn : subprojects.values())
        {
            list.addAll(pn.getTypes());
        }

        for (ModuleNode mn : modules.values())
        {
            list.addAll(mn.getTypes());
        }

        for (FileNode fn : files.values())
        {
            list.addAll(fn.getTypes());
        }

        return list;
    }

    /**
     * @param json
     *            Constructs a new Project from a JSON encoded string object.
     * @return The newly created project, or null if the provided string is null
     *         or empty
     */
    public static ProjectNode createFromJson(String json)
    {
        if (json == null || json.isEmpty())
            return null;

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(FileNode.class, new FileNodeDeserializer());
        gb.registerTypeAdapter(TypeNode.class, new TypeNodeDeserializer());
        gb.registerTypeAdapter(FieldNode.class, new FieldNodeDeserializer());
        gb.registerTypeAdapter(MethodNode.class, new MethodNodeDeserializer());
        gb.registerTypeAdapter(ProjectNode.class, new ProjectNodeDeserializer());
        gb.registerTypeAdapter(ModuleNode.class, new ModuleNodeDeserializer());
        Gson g = gb.create();
        return g.fromJson(json, ProjectNode.class);
    }

    /**
     * @return A JSON serialized form of this object.
     */
    public String toJSON()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * @return true if this project has any subprojects, modules, or files
     */
    public boolean hasChildren()
    {
        return !subprojects.isEmpty() || !modules.isEmpty() || !files.isEmpty();
    }

    /**
     * @return The set of all methods contained in the all types contained
     *         within the children of this project.
     */
    public List<MethodNode> getMethods()
    {
        List<MethodNode> methods = Lists.newArrayList();

        for (String f : files.keySet())
        {
            methods.addAll(files.get(f).getMethods());
        }

        return methods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectNode cloneNoChildren()
    {
        ProjectNode pnode = new ProjectNode(qIdentifier);

        copyMetrics(pnode);

        pnode.setParentID(this.parentID);

        return pnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectNode clone() throws CloneNotSupportedException
    {
        ProjectNode pnode = cloneNoChildren();

        for (String key : files.keySet())
        {
            pnode.addFile(files.get(key).clone());
        }

        for (String key : modules.keySet())
        {
            pnode.addModule(modules.get(key).clone());
        }

        for (String key : subprojects.keySet())
        {
            pnode.addSubProject(subprojects.get(key).clone());
        }

        return pnode;
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
     * Searches this project for a module with the provided qualified
     * identifier.
     * 
     * @param qIdentifier
     *            Identifier to search for modules with
     * @return true if this project contains a module with the provided
     *         qIdentifier, false otherwise.
     */
    public boolean hasModule(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return false;

        return modules.containsKey(qIdentifier);
    }

    /**
     * Searches this project for a file with the provided path and returns true
     * if such a file exists in this project.
     * 
     * @param path
     *            Absolute path to search for.
     * @return true if a file exists, within this project, with the given path,
     *         false otherwise.
     */
    public boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false;

        return files.containsKey(path);
    }

    /**
     * @return The set of all namespaces contained within this project.
     */
    public Set<NamespaceNode> getNamespaces()
    {
        return Sets.newHashSet(namespaces.values());
    }

    /**
     * @param ns
     *            The namespace to be added to this project.
     */
    protected void addNamespace(NamespaceNode ns)
    {
        if (ns == null || namespaces.containsKey(ns.getQIdentifier()))
        {
            return;
        }

        ns.setParentID(this.getQIdentifier());
        namespaces.put(ns.getQIdentifier(), ns);
    }

    /**
     * @param ns
     *            Qualified ID of the Namespace to be added to this project
     * @return the newly created namespace, an existing namespace if the
     *         provided id has already been added, or null if the provided id is
     *         null or empty.
     */
    public NamespaceNode addNamespace(String ns)
    {
        if (ns == null || ns.isEmpty())
            return null;

        if (namespaces.containsKey(ns))
            return namespaces.get(ns);

        NamespaceNode namespace = new NamespaceNode(ns);
        namespace.setParentID(this.getQIdentifier());

        namespaces.put(namespace.getQIdentifier(), namespace);

        return namespace;
    }

    /**
     * @param ns
     *            Qualified identifier of Namespace to remove from this project,
     *            if it is contained in the project.
     */
    public void removeNamespace(String ns)
    {
        if (ns == null || ns.isEmpty())
            return;

        namespaces.remove(ns);
    }

    /**
     * @param ns
     *            Namespace to remove from this project, if it is contained in
     *            the project.
     */
    public void removeNamespace(NamespaceNode ns)
    {
        if (ns == null)
            return;

        namespaces.remove(ns.getQIdentifier());
    }

    /**
     * Searches this project for a namespace matching the provided qualified
     * identifier.
     * 
     * @param ns
     *            Qualified Identifier
     * @return The namespace matching the qualified identifier, or null if no
     *         such namespace is contained in this project or the provided
     *         identifier is null or empty.
     */
    public NamespaceNode getNamespace(String ns)
    {
        if (ns == null || ns.isEmpty() || !namespaces.containsKey(ns))
            return null;

        return namespaces.get(ns);
    }

    /**
     * Checks whether this project contains a namespace matching the provided
     * qualified identifier.
     * 
     * @param ns
     *            Qualified identifier
     * @return true if this project contains a namespace matching the provided
     *         qualified identifier, false otherwise.
     */
    public boolean hasNamespace(String ns)
    {
        if (ns == null || ns.isEmpty())
            return false;

        return namespaces.containsKey(ns);
    }

    /**
     * Constructs a new Builder for a Project with the given qualified
     * identifier.
     * 
     * @param qID
     *            Qualified Identifier of the project to be constructed
     * @return the ProjectNode.Builder instance.
     */
    public static Builder builder(String qID)
    {
        return new Builder(qID);
    }

    /**
     * Builder for Projects implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static class Builder {

        /**
         * Node to be constructed
         */
        private ProjectNode node;

        /**
         * Constructs a new Builder for a Project with the given qualified
         * identifier.
         * 
         * @param qID
         *            Qualified Identifier of the project to be constructed
         */
        private Builder(String qID)
        {
            node = new ProjectNode(qID);
        }

        /**
         * @return The constructed Project
         */
        public ProjectNode create()
        {
            return node;
        }

        /**
         * Add the given module to the project under construction.
         * 
         * @param module
         *            Module to be added.
         * @return this
         */
        public Builder module(ModuleNode module)
        {
            node.addModule(module);

            return this;
        }

        /**
         * Add the given file to the project under construction
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
         * Add the given project as a subproject of the project under
         * construction
         * 
         * @param project
         *            Project to be added as a subproject
         * @return this
         */
        public Builder project(ProjectNode project)
        {
            node.addSubProject(project);

            return this;
        }

        /**
         * Adds the provided namespace to the project under construction
         * 
         * @param namespace
         *            Namespace to be added
         * @return this
         */
        public Builder namespace(NamespaceNode namespace)
        {
            node.addNamespace(namespace);

            return this;
        }

        /**
         * Adds a metric and its measurement value to this project.
         * 
         * @param metric
         *            Name of metric to add.
         * @param value
         *            Measurement value of the metric.
         * @return this
         */
        public Builder metric(String metric, Double value)
        {
            node.addMetric(metric, value);

            return this;
        }

        /**
         * Sets the qualified identifier of the parent for this project
         * 
         * @param pID
         *            Parent's qualified identifier
         * @return this
         */
        public Builder parent(String pID)
        {
            node.setParentID(pID);

            return this;
        }
    }
}
