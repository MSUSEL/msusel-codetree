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
package edu.montana.gsoc.msusel.codetree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import edu.montana.gsoc.msusel.codetree.json.FileNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.json.ProjectNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.json.TypeNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.node.FileNode;
import edu.montana.gsoc.msusel.codetree.node.MethodNode;
import edu.montana.gsoc.msusel.codetree.node.ProjectNode;
import edu.montana.gsoc.msusel.codetree.node.TypeNode;
import edu.montana.gsoc.msusel.codetree.util.CodeTreeUtils;
import edu.montana.gsoc.msusel.codetree.json.MethodNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.node.ModuleNode;
import edu.montana.gsoc.msusel.codetree.json.FieldNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.json.ModuleNodeDeserializer;
import edu.montana.gsoc.msusel.codetree.node.FieldNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A data structure used to represent a model of a software system (both
 * structure and code entities). This structure can then be used for multiple
 * analyses such as name and static analysis.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
@EqualsAndHashCode(of = {"project"})
public class CodeTree {

    @Expose
    @Getter
    private ProjectNode project;
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

        setProject(ProjectNode.builder(key).create());
    }

    public void setProject(ProjectNode node) {
        this.project = node;
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
