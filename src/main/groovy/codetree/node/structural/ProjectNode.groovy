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
package codetree.node.structural

import codetree.CodeTree
import codetree.INode
import codetree.json.FieldNodeDeserializer
import codetree.json.FileNodeDeserializer
import codetree.json.MethodNodeDeserializer
import codetree.json.ModuleNodeDeserializer
import codetree.json.ProjectNodeDeserializer
import codetree.node.member.FieldNode
import codetree.node.member.MethodNode
import com.google.common.collect.Sets
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ProjectNode extends StructuralNode {

    @Builder(buildMethodName = "create")
    ProjectNode(String key, String parentKey, Map<String, Double> metrics) {
        super(key, parentKey, metrics)
    }


    def files() {
        children.findAll {
            it instanceof FileNode
        }
    }
    
    def modules() {
        children.findAll {
            it instanceof ModuleNode
        }
    }
    
    def subprojects() {
        children.findAll {
            it instanceof ProjectNode
        }
    }
    
    def namespaces() {
        children.findAll {
            it instanceof NamespaceNode
        }
    }

    def types()
    {
        []
    }
    
    def methods() {
        []
    }
    
    def name() {
        key
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode c)
    {
        if (c == null)
            return

        if (!(c instanceof ProjectNode))
            return

        ProjectNode project_update = (ProjectNode) c

        project_update.modules().each {ModuleNode m ->
            if (hasModule(m.key)) {
                ((ModuleNode) modules().find {ModuleNode mn -> mn.key == m.key }).update(m)
            } else {
                children << m
            }
        }

        project_update.subprojects().each {ProjectNode p ->
            if (hasSubProject(p.key)) {
                ((ProjectNode) subprojects().find {ProjectNode pn -> pn.key == p.key }).update(p)
            } else {
                children << p
            }
        }

        project_update.files().each {FileNode f ->
            if (hasFile(f.key)) {
                ((FileNode ) files().find {FileNode fn -> fn.key == f.key }).update(f)
            } else {
                children << f
            }
        }

        project_update.metricNames.each {this.addMetric(it, project_update.getMetric(it))}
    }

    /**
     * @return Set of file paths for the files contained in this project.
     */
    Set<String> getFileKeys()
    {
        def set = Sets.newHashSet()

        files().each {FileNode fn -> set << fn.key}

        set
    }
    
    /**
     * @param json
     *            Constructs a new Project from a JSON encoded string object.
     * @return The newly created project, or null if the provided string is null
     *         or empty
     */
    static ProjectNode createFromJson(String json)
    {
        if (json == null || json.isEmpty())
            return null

        GsonBuilder gb = new GsonBuilder()
        gb.registerTypeAdapter(FileNode.class, new FileNodeDeserializer())
//        gb.registerTypeAdapter(TypeNode.class, new TypeNodeDeserializer())
        gb.registerTypeAdapter(FieldNode.class, new FieldNodeDeserializer())
        gb.registerTypeAdapter(MethodNode.class, new MethodNodeDeserializer())
        gb.registerTypeAdapter(ProjectNode.class, new ProjectNodeDeserializer())
        gb.registerTypeAdapter(ModuleNode.class, new ModuleNodeDeserializer())
        Gson g = gb.create()
        return g.fromJson(json, ProjectNode.class)
    }

    /**
     * @return A JSON serialized form of this object.
     */
    String toJSON()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(this)
    }

    /**
     * @return true if this project has any subprojects, modules, or files
     */
    boolean hasChildren()
    {
        return !children.isEmpty()
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    ProjectNode cloneNoChildren()
    {
        ProjectNode pnode = new ProjectNode(key: this.key)

        copyMetrics(pnode)

        pnode.setParentKey(this.parentKey)

        pnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    ProjectNode clone() throws CloneNotSupportedException
    {
        ProjectNode pnode = cloneNoChildren()

        children.each {pnode.children << it.clone()}

        pnode
    }

    /**
     * Searches this project for a subproject with the provided qualified
     * identifier.
     *
     * @param qIdentifier
     *            Identifier to search for modules with
     * @return true if this project contains a module with the provided
     *         qIdentifier, false otherwise.
     */
    boolean hasSubProject(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return false

        return subprojects().find { it instanceof ProjectNode && it.getKey() == qIdentifier } != null
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
    boolean hasModule(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return false

        return modules().find { it instanceof ModuleNode && it.getKey() == qIdentifier } != null
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
    boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false;

        return files().find {it instanceof FileNode && it.key == path} != null
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
    boolean hasNamespace(String ns)
    {
        if (ns == null || ns.isEmpty())
            return false

        return namespaces().find {it instanceof NamespaceNode && it.key == ns} != null
    }

    NamespaceNode getNamespace(String ns) {
        if (ns == null || ns.isEmpty())
            return null

        return (NamespaceNode) namespaces().find {it instanceof NamespaceNode && it.key == ns}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type()
    {
        // TODO Auto-generated method stub
        return null;
    }

    def extractTree(tree) {
        CodeTree retVal

        ProjectNode project = this
        if (project.getParentKey() == null)
        {
            retVal = tree
        }
        else
        {
            retVal = new CodeTree()
            Stack<ProjectNode> stack = new Stack<>()
            stack.push(project)
            while (project.hasParent())
            {
                project = tree.utils.findProject(project.getParentKey())
                stack.push(project)
            }

            ProjectNode current = stack.pop().cloneNoChildren()
            ProjectNode next = null
            while (!stack.isEmpty())
            {
                if (stack.size() == 1)
                {
                    try
                    {
                        next = stack.pop().clone()
                    }
                    catch (CloneNotSupportedException e)
                    {
                        e.printStackTrace()
                    }
                }
                else
                {
                    next = stack.pop().cloneNoChildren()
                }

                if (next != null)
                {
                    current.children << next
                    current = next
                }
            }
        }

        return retVal
    }
}
