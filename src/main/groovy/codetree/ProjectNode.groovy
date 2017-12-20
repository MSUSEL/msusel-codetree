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
/**
 * 
 */
package codetree

import codetree.node.FieldNode
import codetree.node.FileNode
import codetree.node.MethodNode
import codetree.node.ModuleNode
import codetree.node.NamespaceNode
import codetree.node.StructuralNode
import codetree.node.TypeNode
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * @author Isaac Griffith
 *
 */
class ProjectNode extends StructuralNode {

    /**
     * 
     */
    public ProjectNode()
    {
        // TODO Auto-generated constructor stub
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
    
    def methods() {
        
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
    Set<String> getFileKeys()
    {
        return files.keySet();
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
            return null;

        GsonBuilder gb = new GsonBuilder();
//        gb.registerTypeAdapter(FileNode.class, new FileNodeDeserializer());
//        gb.registerTypeAdapter(TypeNode.class, new TypeNodeDeserializer());
//        gb.registerTypeAdapter(FieldNode.class, new FieldNodeDeserializer());
//        gb.registerTypeAdapter(MethodNode.class, new MethodNodeDeserializer());
//        gb.registerTypeAdapter(ProjectNode.class, new ProjectNodeDeserializer());
//        gb.registerTypeAdapter(ModuleNode.class, new ModuleNodeDeserializer());
        Gson g = gb.create();
        return g.fromJson(json, ProjectNode.class);
    }

    /**
     * @return A JSON serialized form of this object.
     */
    String toJSON()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * @return true if this project has any subprojects, modules, or files
     */
    boolean hasChildren()
    {
        return !subprojects.isEmpty() || !modules.isEmpty() || !files.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    ProjectNode cloneNoChildren()
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
    ProjectNode clone() throws CloneNotSupportedException
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
    boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false;

        return files.containsKey(path);
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
            return false;

        return namespaces.containsKey(ns);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Object types()
    {
        // TODO Auto-generated method stub
        return null;
    }

    def extractTree(CodeTree tree) {
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
                project = findProject(project.getParentKey())
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
                    current.addSubProject(next)
                    current = next
                }
            }
        }

        return retVal
    }

    def extractTree(tree) {
        null
    }
}
