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
package com.sparqline.codetree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sparqline.codetree.json.FieldNodeDeserializer;
import com.sparqline.codetree.json.FileNodeDeserializer;
import com.sparqline.codetree.json.MethodNodeDeserializer;
import com.sparqline.codetree.json.ModuleNodeDeserializer;
import com.sparqline.codetree.json.ProjectNodeDeserializer;
import com.sparqline.codetree.json.TypeNodeDeserializer;
import com.sparqline.codetree.node.FieldNode;
import com.sparqline.codetree.node.FileNode;
import com.sparqline.codetree.node.MethodNode;
import com.sparqline.codetree.node.ModuleNode;
import com.sparqline.codetree.node.ProjectNode;
import com.sparqline.codetree.node.TypeNode;
import com.sparqline.codetree.util.CodeTreeUtils;

/**
 * A data structure used to represent a model of a software system (both
 * structure and code entities). This structure can then be used for multiple
 * analyses such as metric and static analysis.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class CodeTree {

    /**
     * The ProjectNode representing the root of the tree.
     */
    @Expose
    private ProjectNode   project;
    /**
     * The CodeTreeUtils for this CodeTree
     */
    private CodeTreeUtils utils;

    /**
     * Constructs a new empty CodeTree
     */
    public CodeTree()
    {

    }

    /**
     * @return The root project node.
     */
    public ProjectNode getProject()
    {
        return project;
    }

    /**
     * Creates a new root project using the provide qualified identifier for the
     * project.
     * 
     * @param key
     *            Key Qualified Identifier of the root project for this
     *            CodeTree.
     * @throws IllegalArgumentException
     *             if the provided identifier is null or empty
     */
    public void setProject(String key)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Project key cannot be null or empty.");

        project = ProjectNode.builder(key).create();
    }

    /**
     * Sets the root project to the provided ProjectNode. If the provided
     * ProjectNode is null, nothing happens.
     * 
     * @param pn
     *            new root.
     */
    public void setProject(ProjectNode pn)
    {
        if (pn == null)
            return;

        project = pn;
    }

    /**
     * Deserializes a CodeTree object from a given JSON string.
     * 
     * @param json
     *            JSON encoded codetree object.
     * @return A newly instantiated CodeTree deserialized from the given string,
     *         or null if the provided string is null or empty.
     */
    public static CodeTree createFromJson(String json)
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
        return g.fromJson(json, CodeTree.class);
    }

    /**
     * @return Serializes this code tree and its contents to a JSON string.
     */
    public String toJSON()
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof CodeTree))
        {
            return false;
        }
        CodeTree other = (CodeTree) obj;
        if (project == null)
        {
            if (other.project != null)
            {
                return false;
            }
        }
        else if (!project.equals(other.project))
        {
            return false;
        }
        return true;
    }

    /**
     * @return A CodeTreeUtils for this CodeTree.
     */
    public CodeTreeUtils getUtils()
    {
        if (utils == null)
        {
            utils = new CodeTreeUtils(this);
        }

        return utils;
    }

}
