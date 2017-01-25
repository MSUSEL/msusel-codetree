/**
 * The MIT License (MIT)
 *
 * Sonar Quamoco Plugin
 * Copyright (c) 2015 Isaac Griffith, SiliconCode, LLC
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
package com.sparqline.quamoco.codetree;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sparqline.quamoco.codetree.json.FieldNodeDeserializer;
import com.sparqline.quamoco.codetree.json.FileNodeDeserializer;
import com.sparqline.quamoco.codetree.json.MethodNodeDeserializer;
import com.sparqline.quamoco.codetree.json.TypeNodeDeserializer;

/**
 * FileNode -
 *
 * @author Isaac Griffith
 */
public class FileNode extends CodeNode {

    @Expose
    private final Map<String, TypeNode> types = Maps.newHashMap();
    @Expose
    private String                      parentID;

    /**
     * 
     */
    private FileNode() {
        super();
    }

    /**
     * @param fullPath
     */
    public FileNode(final String fullPath) {
        super(fullPath, fullPath, 1, 1);
    }

    /**
     * @param name
     * @return
     */
    public TypeNode addType(final String name) {
        if (name == null || name.isEmpty())
            return null;

        if (types.containsKey(name))
            return types.get(name);

        TypeNode t = new TypeNode(name, name, 1, 1);
        types.put(name, t);
        return t;
    }

    /**
     * @param node
     * @return
     */
    public boolean addType(final TypeNode node) {
        if (node == null || types.containsKey(node.getQIdentifier())) {
            return false;
        }

        types.put(node.getQIdentifier(), node);

        return true;
    }

    /**
     * @param node
     * @return
     */
    public boolean removeType(final TypeNode node) {
        if (node == null || !types.containsKey(node.getQIdentifier())) {
            return false;
        }

        types.remove(node);
        return true;
    }

    /**
     * @return
     */
    public Set<TypeNode> getTypes() {
        final Set<TypeNode> typeSet = Sets.newTreeSet();

        for (final String key : types.keySet()) {
            typeSet.add(types.get(key));
        }

        return typeSet;
    }

    /**
     * @param line
     * @return
     */
    public String getMethod(final int line) {
        final String type = getType(line);
        if (type != null && types.containsKey(type)) {
            final TypeNode node = types.get(getType(line));
            if (node.getMethod(line) != null) {
                return node.getMethod(line).getQIdentifier();
            }
        }

        return "";
    }

    /**
     * @param line
     * @return
     */
    public String getType(final int line) {
        for (final String type : types.keySet()) {
            final TypeNode node = types.get(type);
            if (node.containsLine(line)) {
                return node.getQIdentifier();
            }
        }

        return "";
    }

    /**
     * @param name
     * @return
     */
    public TypeNode getType(final String name) {
        if (name == null || name.isEmpty())
            return null;

        if (types.containsKey(name))
            return types.get(name);
        else
            return addType(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.siliconcode.quamoco.codetree.CodeNode#getType()
     */
    @Override
    public String getType() {
        return CodeNodeType.FILE;
    }

    /**
     * @param line
     * @return
     */
    public String getField(final int line) {
        final String type = getType(line);

        if (type != null && types.containsKey(type)) {
            final TypeNode node = types.get(type);
            if (node.getField(line) != null) {
                return node.getField(line).getQIdentifier();
            }
        }

        return "";
    }

    /**
     * @return
     */
    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#update(com.sparqline.quamoco.codetree.CodeNode)
     */
    @Override
    public void update(CodeNode node) {
        if (node == null)
            return;

        updateLocation(node.getStart(), node.getEnd());

        if (!(node instanceof FileNode))
            return;

        FileNode f = (FileNode) node;

        for (TypeNode t : f.getTypes()) {
            if (getType(t.getQIdentifier()) != null) {
                getType(t.getQIdentifier()).update(t);
            }
            else {
                addType(t);
            }
        }

        for (String key : f.metrics.keySet()) {
            this.metrics.put(key, f.metrics.get(key));
        }
    }

    public static FileNode createFromJson(String json) {
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
     * @param cnode
     * @return
     */
    public boolean hasType(TypeNode cnode) {
        return types.containsKey(cnode.getQIdentifier());
    }

    /**
     * @return
     */
    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    /**
     * @return
     */
    public List<MethodNode> getMethods() {
        List<MethodNode> methods = Lists.newArrayList();
        for (String t : types.keySet()) {
            methods.addAll(types.get(t).getMethods());
        }

        return methods;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#cloneNoChildren()
     */
    @Override
    public FileNode cloneNoChildren() {
        FileNode fnode = new FileNode(qIdentifier);

        copyMetrics(fnode);

        fnode.setParentID(parentID);
        fnode.setEnd(this.getEnd());
        fnode.setStart(this.getStart());

        return fnode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected FileNode clone() throws CloneNotSupportedException {
        FileNode fnode = cloneNoChildren();

        for (String key : types.keySet()) {
            fnode.addType(types.get(key).clone());
        }

        return fnode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
