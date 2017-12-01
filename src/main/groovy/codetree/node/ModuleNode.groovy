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
package codetree.node

import codetree.INode
import codetree.ProjectNode

/**
 * @author Isaac Griffith
 *
 */
class ModuleNode extends StructuralNode {

    /**
     * 
     */
    public ModuleNode() {
        // TODO Auto-generated constructor stub
    }

    def files() {
    }
    
    def types() {
        
    }

    def modules() {
    }

    def namespaces() {
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

        ModuleNode mn = (ModuleNode) c

        for (FileNode f : mn.getFiles())
        {
            if (getFile(f.getQIdentifier()) != null)
            {
                getFile(f.getQIdentifier()).update(f)
            }
            else
            {
                addFile(f)
            }
        }

        for (String key : c.getMetricNames())
        {
            this.addMetric(key, c.getMetric(key))
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    ModuleNode cloneNoChildren()
    {
        ModuleNode mnode = new ModuleNode(this.qIdentifier)

        copyMetrics(mnode)

        return mnode
    }

    /**
     * {@inheritDoc}
     */
    @Override
    ModuleNode clone() throws CloneNotSupportedException
    {
        ModuleNode mnode = cloneNoChildren()

        for (String key : files.keySet())
        {
            mnode.addFile(files.get(key).clone())
        }

        return mnode
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
    boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false

        return files.containsKey(path)
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
    NamespaceNode getNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty() || !namespaces.containsKey(nsID))
            return null

        return namespaces.get(nsID)
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
    boolean hasNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty())
            return false

        return namespaces.containsKey(nsID)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def type()
    {
        "Module"
    }
}