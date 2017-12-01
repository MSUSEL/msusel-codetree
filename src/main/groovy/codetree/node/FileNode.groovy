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
package codetree.node

import codetree.INode
import com.google.common.collect.Lists
import com.google.common.collect.Sets

/**
 * @author Isaac Griffith
 *
 */
class FileNode extends StructuralNode {
    
    /**
     * 
     */
    public FileNode()
    {
        // TODO Auto-generated constructor stub
    }

    def name() {
        key
    }
    
    def type() {
        "File"
    }
    
    def imports() {
        children.findAll {
            it instanceof ImportNode
        }
    }
    
    def types() {
        children.findAll {
            it instanceof TypeNode
        }
    }
    
    /**
     * Retrieves the method containing the given line from a type contained in
     * this file.
     *
     * @param line
     *            Line number for which a method is requested.
     * @return Qualified identifier of the method at the given line, or the
     *         empty string if no method exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    String findMethod(final int line)
    {
        final String type = getType(line);
        if (type != null && types.containsKey(type))
        {
            final TypeNode node = types.get(getType(line));
            if (node.getMethod(line) != null)
            {
                return node.getMethod(line).getQIdentifier();
            }
        }

        return "";
    }

    /**
     * Retrieves the qualified identifier of the type in this file which
     * contains the given line.
     *
     * @param line
     *            Line number for which a type is requested
     * @return Qualified identifier of the type at the given line, or the
     *         empty string if no type exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    String findType(final int line)
    {
        for (final String type : types.keySet())
        {
            final TypeNode node = types.get(type);
            if (node.containsLine(line))
            {
                return node.getQIdentifier();
            }
        }

        return "";
    }

    /**
     * Retrieves the type with the given qualified identifier.
     *
     * @param name
     *            Qualified identifier of the type to find in this file.
     * @return The type with the given identifier, if one exists, null
     *         otherwise or if the given identifier is null or empty.
     */
    TypeNode findType(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (types.containsKey(name))
            return types.get(name);
        else
            return addType(name);
    }
    
    /**
     * Retrieves the qualified identifier of the field at the given line, if one
     * exists.
     *
     * @param line
     *            Line to find a field at.
     * @return The qualified identifier if the given line contains a field
     *         definition, empty string otherwise.
     */
    String findField(final int line)
    {
        final String type = getType(line);

        if (type != null && types.containsKey(type))
        {
            final TypeNode node = types.get(type);
            if (node.getField(line) != null)
            {
                return node.getField(line).getQIdentifier();
            }
        }

        return "";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void update(INode node)
    {
        if (node == null)
            return;
        if (!(node instanceof FileNode))
            return;

        FileNode f = (FileNode) node;

        for (TypeNode t : f.getTypes())
        {
            if (getType(t.getQIdentifier()) != null)
            {
                getType(t.getQIdentifier()).update(t);
            }
            else
            {
                addType(t);
            }
        }

        for (String key : f.getMetricNames())
        {
            this.addMetric(key, f.getMetric(key));
        }
    }
    
    /**
     * Checks whether the provided type is contained within this file.
     *
     * @param cnode
     *            Type
     * @return true if contained in this file, false if provided type is null or
     *         not contained in this file.
     */
    public boolean hasType(TypeNode cnode)
    {
        if (cnode == null)
            return false;

        return types.containsKey(cnode.getQIdentifier());
    }

    /**
     * @return The set of methods of all types contained in this file.
     */
    public List<MethodNode> getMethods()
    {
        List<MethodNode> methods = Lists.newArrayList();
        for (String t : types.keySet())
        {
            methods.addAll(types.get(t).getMethods());
        }

        return methods;
    }
    
    /**
     * Checks if the given import is contained in this FileNode
     *
     * @param imp
     *            Import to check
     * @return true if the given import is contained in the FileNode, false if
     *         the provided value is null, empty, or not contained in the
     *         FileNode.
     */
    public boolean hasImport(String imp)
    {
        if (imp == null || imp.isEmpty())
            return false;

        return imports.contains(imp);
    }

    /**
     * @return The set of Imports contained in the FileNode
     */
    public Set<String> getImports()
    {
        return Sets.newHashSet(imports);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileNode cloneNoChildren()
    {
        FileNode fnode = new FileNode(key: key);

        copyMetrics(fnode);

        fnode.setParentKey(parentKey);

        return fnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileNode clone() throws CloneNotSupportedException
    {
        FileNode fnode = cloneNoChildren();

        for (String key : types.keySet())
        {
            fnode.addType(types.get(key).clone());
        }

        return fnode;
    }
}
