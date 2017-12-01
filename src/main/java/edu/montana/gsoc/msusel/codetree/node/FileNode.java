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

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.montana.gsoc.msusel.codetree.json.FileNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.json.TypeNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.json.MethodNodeDeserializer;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.codetree.INode;
import edu.montana.gsoc.msusel.codetree.json.FieldNodeDeserializer;

/**
 * An abstraction representing a file within the project (typically a source
 * code/test code file). Each file's qualified identifier is its key as defined
 * by some arbitrary system, while its name is the absolute path within the
 * filesystem it resides in. Files can then define a set of Types which are
 * defined within the file.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class FileNode extends StructuralNode {

    /**
     * Map of types contained in this file indexed by their qualified
     * identifiers
     */
    @Expose
    private final Map<String, TypeNode> types;
    /**
     * Set of imports/includes used by this file
     */
    @Expose
    private final Set<String>           imports;
    /**
     * 
     */
    @Expose
    private int                         length;

    /**
     * Constructs a new FileNode using the provided absolute path as the
     * qualified identifier
     * 
     * @param fullPath
     *            The absolute path of the file this FileNode represents.
     */
    protected FileNode(final String fullPath)
    {
        super(fullPath);
        types = Maps.newHashMap();
        imports = Sets.newHashSet();
        length = 1;
    }

    /**
     * Adds a new type to this file with the given qualified identifier
     * 
     * @param name
     *            Qualified identifier of the new Type
     * @return the newly created type, or if one already exists with the given
     *         identifier, then that one. If the provided identifier is empty or
     *         null then null is returned.
     */
    public TypeNode addType(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (types.containsKey(name))
            return types.get(name);

        TypeNode t = new TypeNode(name, name);
        types.put(name, t);
        return t;
    }

    /**
     * Adds the given type to this file.
     * 
     * @param node
     *            Type to add
     * @return true if the given type was added, false if it is null or already
     *         exists in this file.
     */
    public boolean addType(final TypeNode node)
    {
        if (node == null || types.containsKey(node.getQIdentifier()))
        {
            return false;
        }

        types.put(node.getQIdentifier(), node);

        node.setParentKey(this.getQIdentifier());

        return true;
    }

    /**
     * Removes the given type from this file if it exists in this file.
     * 
     * @param node
     *            Type to remove
     * @return true if the type was successfully remove, false if it does not
     *         exist in this file or if the provided type was null.
     */
    public boolean removeType(final TypeNode node)
    {
        if (node == null || !types.containsKey(node.getQIdentifier()))
        {
            return false;
        }

        types.remove(node.getQIdentifier());

        node.setParentKey(null);

        return true;
    }

    /**
     * @return The set of types contained in this file
     */
    public Set<TypeNode> getTypes()
    {
        final Set<TypeNode> typeSet = Sets.newTreeSet();

        for (final String key : types.keySet())
        {
            typeSet.add(types.get(key));
        }

        return typeSet;
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
    public String getMethod(final int line)
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
    public String getType(final int line)
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
    public TypeNode getType(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        return types.containsKey(name) ? types.get(name) : addType(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType()
    {
        return INodeType.FILE;
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
    public String getField(final int line)
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
     * @return The JSON representation of this File and its contents.
     */
    public String toJSON()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode node)
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
     * Constructs a new FileNode from a given JSON representation
     * 
     * @param json
     *            JSON representation
     * @return A FileNode or null if the provided string is null or empty.
     */
    public static FileNode createFromJson(String json)
    {
        if (json == null || json.isEmpty())
            return null;

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(FileNode.class, new FileNodeDeserializer());
        gb.registerTypeAdapter(TypeNode.class, new TypeNodeDeserializer());
        gb.registerTypeAdapter(FieldNode.class, new FieldNodeDeserializer());
        gb.registerTypeAdapter(MethodNode.class, new MethodNodeDeserializer());
        Gson g = gb.create();
        return g.fromJson(json, FileNode.class);
    }

    /**
     * Checks whether the provided type is contained within this file.
     * 
     * @param node
     *            Type
     * @return true if contained in this file, false if provided type is null or
     *         not contained in this file.
     */
    public boolean hasType(TypeNode node) {
        return node != null && types.containsKey(node.getQIdentifier());

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
     * Sets the length of this file.
     * 
     * @param length
     *            The new length of this File
     */
    private void setLength(int length)
    {
        if (length <= 0)
            throw new IllegalArgumentException("File length cannot be less than or equal to 0");

        this.length = length;
    }

    /**
     * @return Length of this file
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Adds the given import to the set of imports, unless the provided value is
     * empty or null.
     * 
     * @param imp
     *            Import to add
     */
    public void addImport(String imp)
    {
        if (imp == null || imp.isEmpty())
            return;

        imports.add(imp);
    }

    /**
     * Removes the given import from the set of imports, unless the given value
     * is null or empty.
     * 
     * @param imp
     *            Import to remove
     */
    public void removeImport(String imp)
    {
        if (imp == null || imp.isEmpty() || !imports.contains(imp))
            return;

        imports.remove(imp);
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
    public boolean hasImport(String imp) {
        return imp != null && !imp.isEmpty() && imports.contains(imp);

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
        FileNode node = new FileNode(getQIdentifier());

        copyMetrics(node);

        node.setParentKey(parentKey);

        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileNode clone() throws CloneNotSupportedException
    {
        FileNode node = cloneNoChildren();

        for (String key : types.keySet())
        {
            node.addType(types.get(key).clone());
        }

        return node;
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
     * Searches this FileNode for a type at the given line.
     * 
     * @param line
     *            The line within this file for which a type whose range
     *            contains this line number is being searched for.
     * @return A type in this file whose line range contains the given line
     *         number, or null if no such type exists.
     */
    public TypeNode findType(int line)
    {
        if (line > length || line <= 0)
            return null;

        for (TypeNode type : types.values())
        {
            if (type.containsLine(line))
                return type;
        }

        return null;
    }

    /**
     * Constructs a new Builder for a FileNode with the given name and
     * qualified identifier
     * 
     * @param name
     *            Name of the file
     * @param qID
     *            Qualified Identifier of the File
     * @return The FileNode.Builder instance
     */
    public static Builder builder(String name, String qID)
    {
        return new Builder(name, qID);
    }

    /**
     * Constructs a new Builder for a FileNode with the given qualified
     * identifier
     * 
     * @param qID
     *            Qualified Identifier of the File
     * @return The FileNode.Builder instance
     */
    public static Builder builder(String qID)
    {
        return new Builder(qID);
    }

    /**
     * Builder for Files implemented using the fluent interface and method
     * chaining patterns.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    public static final class Builder {

        /**
         * The FileNode to be constructed by this Builder
         */
        private final FileNode node;

        /**
         * Constructs a new Builder for a FileNode with the given name and
         * qualified identifier
         * 
         * @param name
         *            Name of the file
         * @param qID
         *            Qualified Identifier of the File
         */
        private Builder(String name, String qID)
        {
            node = new FileNode(qID);
            node.setName(name);
        }

        /**
         * Constructs a new Builder for a FileNode with the given qualified
         * identifier
         * 
         * @param qID
         *            Qualified Identifier of the File
         */
        private Builder(final String qID)
        {
            node = new FileNode(qID);
        }

        /**
         * @return The Constructed FileNode
         */
        public final @NonNull FileNode create()
        {
            return node;
        }

        /**
         * Adds the given type to the FileNode under construction
         * 
         * @param type
         *            Type to add
         * @return this
         */
        public final @NonNull Builder type(final TypeNode type)
        {
            node.addType(type);

            return this;
        }

        /**
         * The the name and measurement value to the FileNode under
         * construction.
         * 
         * @param metric
         *            Metric name
         * @param value
         *            Measurement value
         * @return this
         */
        public final @NonNull Builder metric(final String metric, final Double value)
        {
            node.addMetric(metric, value);

            return this;
        }

        /**
         * Sets the length of the FileNode under construction
         * 
         * @param length
         *            Length in lines
         * @return this
         */
        public final @NonNull Builder length(final int length)
        {
            node.setLength(length);

            return this;
        }

        /**
         * Sets the parent of the FileNode under construction to the provided
         * value.
         * 
         * @param pID
         *            Qualified Identifier of the parent of the FileNode under
         *            construction
         * @return this
         */
        public final @NonNull Builder parent(final String pID)
        {
            node.setParentKey(pID);

            return this;
        }

        /**
         * Adds the given import to the FileNode under construction.
         * 
         * @param imp
         *            Import to add
         * @return this
         */
        public final @NonNull Builder imports(final String imp)
        {
            node.addImport(imp);

            return this;
        }
    }
}
