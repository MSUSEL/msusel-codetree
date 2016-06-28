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

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sparqline.quamoco.codetree.json.FieldNodeDeserializer;
import com.sparqline.quamoco.codetree.json.FileNodeDeserializer;
import com.sparqline.quamoco.codetree.json.MethodNodeDeserializer;
import com.sparqline.quamoco.codetree.json.ModuleNodeDeserializer;
import com.sparqline.quamoco.codetree.json.ProjectNodeDeserializer;
import com.sparqline.quamoco.codetree.json.TypeNodeDeserializer;

/**
 * CodeTree -
 *
 * @author Isaac Griffith
 */
public class CodeTree {

    @Expose
    private ProjectNode project;

    /**
     *
     */
    public CodeTree() {

    }

    public ProjectNode getProject() {
        return project;
    }

    public void setProject(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Project key cannot be null or empty.");

        project = new ProjectNode(key);
    }

    public void setProject(ProjectNode pn) {
        if (pn == null)
            return;

        project = pn;
    }

    /**
     * @param file
     * @param line
     * @return
     */
    public String findMethod(final String file, final int line) {
        if (file == null || file.isEmpty() || line < 0) {
            return "";
        }

        FileNode fn = project.getFile(file);
        if (fn != null) {
            return fn.getMethod(line);
        }

        return "";
    }
    
    public MethodNode findMethod(final String identifier) {
        String[] ids = identifier.split("#");
        TypeNode tn = findType(ids[0]);
        
        if (tn != null)
            return tn.getMethod(ids[1]);
        
        return null;
    }

    /**
     * @param file
     * @param line
     * @return
     */
    public String findClass(final String file, final int line) {
        if (file == null || file.isEmpty() || line < 0) {
            return "";
        }

        FileNode fn = project.getFile(file);

        if (fn != null) {
            return fn.getType(line);
        }

        return "";
    }

    /**
     * @param file
     * @return
     */
    public FileNode getFile(final String file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        return project.getFile(file);
    }

    /**
     * @param file
     * @param node
     */
    public synchronized void addFile(final FileNode node) {
        if (node == null) {
            return;
        }

        project.addFile(node);
    }
    
    public synchronized void updateProject(final ProjectNode node) {
        if (node == null)
            return;
        
        if (project == null)
            this.project = node;
        else
            project.update(node);
    }

    public synchronized void updateFile(final FileNode node) {
        if (node == null)
            return;

        if (project.getFile(node.getQIdentifier()) == null)
            addFile(node);

        getFile(node.getQIdentifier()).update(node);
    }

    /**
     * @return
     */
    public Set<FileNode> getFiles() {
        Set<FileNode> files = Sets.newConcurrentHashSet();

        files.addAll(project.getFiles());

        return files;
    }

    /**
     * @param file
     */
    public void removeFile(final String file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        project.removeFile(file);
    }

    /**
     *
     */
    public void printTree() {
        for (FileNode fn : project.getFiles()) {
            System.out.println(fn.getName());
            final Set<TypeNode> types = fn.getTypes();
            for (final CodeNode type : types) {
                System.out.println("\t" + type.getName());
                for (final CodeNode method : ((TypeNode) type).getMethods()) {
                    System.out.println("\t\t" + method.getName());
                }
            }
        }
    }

    /**
     * @param string
     * @return
     */
    public FileNode findFile(final String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        return project.getFile(string);
    }

    /**
     * @param fnode
     * @param line
     * @return
     */
    public TypeNode findType(final FileNode fnode, final int line) {
        if (fnode == null || line < 1) {
            return null;
        }

        for (final TypeNode tnode : fnode.getTypes()) {
            if (tnode.getQIdentifier().equals(fnode.getType(line))) {
                return tnode;
            }
        }

        return null;
    }

    /**
     * @param type
     * @param line
     * @return
     */
    public MethodNode findMethod(final TypeNode type, final int line) {
        if (type == null || line < 1) {
            return null;
        }

        return type.getMethod(line);
    }

    /**
     * @return
     */
    public Set<MethodNode> getMethods() {
        final Set<MethodNode> methods = Sets.newHashSet();

        project.getFiles().parallelStream().forEach((file) -> {
            for (final TypeNode type : file.getTypes()) {
                methods.addAll(type.getMethods());
            }
        });

        return methods;
    }

    /**
     * @return
     */
    public Set<TypeNode> getTypes() {
        final Set<TypeNode> types = Sets.newConcurrentHashSet();

        project.getFiles().parallelStream().forEach((file) -> {
            types.addAll(file.getTypes());
        });

        return types;
    }

    /**
     * @param key
     * @return
     */
    public TypeNode findType(final String key) {
        for (final FileNode fnode : project.getFiles()) {
            for (final TypeNode tnode : fnode.getTypes()) {
                if (tnode.getQIdentifier().equals(key)) {
                    return tnode;
                }
            }
        }

        return null;
    }

    public void merge(CodeTree other) {
        ProjectNode pn = other.getProject();

        if (project == null)
            project = pn;
        else
            project.update(pn);
    }

    public static CodeTree createFromJson(String json) {
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
     * @return
     */
    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CodeTree)) {
            return false;
        }
        CodeTree other = (CodeTree) obj;
        if (project == null) {
            if (other.project != null) {
                return false;
            }
        }
        else if (!project.equals(other.project)) {
            return false;
        }
        return true;
    }
}
