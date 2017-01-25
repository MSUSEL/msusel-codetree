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

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.Queues;
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

        CodeNode p = null;
        if (findProject(node.getParentID()) != null)
            p = findProject(node.getParentID());
        else
            p = findModule(node.getParentID());

        if (p instanceof ProjectNode) {
            if (((ProjectNode) p).getFile(node.getQIdentifier()) == null) {
                ((ProjectNode) p).addFile(node);
                ((ProjectNode) p).getFile(node.getQIdentifier()).update(node);
            }
        }
        else {
            if (((ModuleNode) p).getFile(node.getQIdentifier()) == null) {
                ((ModuleNode) p).addFile(node);
                ((ModuleNode) p).getFile(node.getQIdentifier()).update(node);
            }
        }
    }

    /**
     * @return
     */
    public Set<FileNode> getFiles() {
        Set<FileNode> files = Sets.newConcurrentHashSet();

        Queue<ProjectNode> queue = Queues.newArrayDeque();

        queue.add(project);

        while (!queue.isEmpty()) {
            ProjectNode pn = queue.poll();
            files.addAll(pn.getFiles());
            queue.addAll(pn.getSubProjects());
        }

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

        for (ProjectNode p : getProjects()) {
            if (p.hasFile(string)) {
                return p.getFile(string);
            }
            else {
                for (ModuleNode m : p.getModules()) {
                    if (m.hasFile(string))
                        return m.getFile(string);
                }
            }
        }
        return null;
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

    public ModuleNode findModule(String qIdentifier) {
        Set<ProjectNode> projs = getProjects();

        for (ProjectNode p : projs) {
            if (p.hasModule(qIdentifier))
                return p.getModule(qIdentifier);
        }

        return null;
    }

    public Set<ProjectNode> getProjects() {
        Set<ProjectNode> projs = Sets.newHashSet();
        Queue<ProjectNode> q = Queues.newArrayDeque();

        if (project != null)
            q.offer(project);

        while (!q.isEmpty()) {
            ProjectNode pnode = q.poll();
            projs.add(pnode);
            q.addAll(pnode.getSubProjects());
        }

        return projs;
    }

    /**
     * @return
     */
    public Set<MethodNode> getMethods() {
        final Set<MethodNode> methods = Sets.newConcurrentHashSet();

        getTypes().parallelStream().forEach((type) -> {
            methods.addAll(type.getMethods());
        });

        return methods;
    }

    /**
     * @return
     */
    public Set<TypeNode> getTypes() {
        final Set<TypeNode> types = Sets.newConcurrentHashSet();

        getFiles().parallelStream().forEach((file) -> {
            types.addAll(file.getTypes());
        });

        return types;
    }

    /**
     * @param key
     * @return
     */
    public TypeNode findType(final String key) {
        for (final TypeNode tnode : getTypes()) {
            if (tnode.getQIdentifier().equals(key)) {
                return tnode;
            }
        }

        return null;
    }

    public void merge(CodeTree other) {
        ProjectNode pn = other.getProject();

        if (project == null) {
            project = pn;
        }
        if (pn.hasParent()) {
            if (pn.getParentQID().equals(project.getQIdentifier())) {
                project.addSubProject(pn);
            }
        }
        else if (project.equals(pn)) {
            project.update(pn);
        }
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

    public ProjectNode findProject(String qid) {
        Queue<ProjectNode> queue = Queues.newArrayDeque();

        if (this.project != null)
            queue.offer(this.project);

        while (!queue.isEmpty()) {
            ProjectNode node = queue.poll();
            if (node.getQIdentifier().equals(qid))
                return node;

            for (ProjectNode pn : node.getSubProjects()) {
                queue.offer(pn);
            }
        }

        return null;
    }

    public CodeTree extractTree(CodeNode cnode) {
        CodeTree retVal = null;

        if (cnode instanceof ProjectNode) {
            ProjectNode pnode = (ProjectNode) cnode;
            if (pnode.getParentQID() == null) {
                retVal = this;
            }
            else {
                retVal = new CodeTree();
                Stack<ProjectNode> stack = new Stack<>();
                stack.push(pnode);
                while (pnode.hasParent()) {
                    pnode = findProject(pnode.getParentQID());
                    stack.push(pnode);
                }

                ProjectNode current = stack.pop().cloneNoChildren();
                ProjectNode next = null;
                while (!stack.isEmpty()) {
                    if (stack.size() == 1) {
                        try {
                            next = stack.pop().clone();
                        }
                        catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        next = stack.pop().cloneNoChildren();
                    }

                    if (next != null) {
                        current.addSubProject(next);
                        current = next;
                    }
                    next = null;
                }
            }
        }
        else if (cnode instanceof ModuleNode) {
            ModuleNode mnode = (ModuleNode) cnode;

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = findProject(mnode.getParentID());
            stack.push(pnode);
            while (pnode.hasParent()) {
                pnode = findProject(pnode.getParentQID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty()) {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            try {
                ModuleNode mod = mnode.clone();
                current.addModule(mod);
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }
        else if (cnode instanceof FileNode) {
            FileNode fnode = (FileNode) cnode;

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null) {
                pnode = findProject(fnode.getParentID());
            }
            else {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent()) {
                pnode = findProject(pnode.getParentQID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            ProjectNode actual = null;
            while (!stack.isEmpty()) {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                if (next.getQIdentifier().equals(fnode.getParentID()))
                    actual = next;
                
                current = next;
                next = null;
            }

            try {
                actual.addFile(fnode.clone());
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }
        else if (cnode instanceof TypeNode) {
            TypeNode tnode = (TypeNode) cnode;

            FileNode fnode = findFile(tnode.getParentFileID());

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null) {
                pnode = findProject(fnode.getParentID());
            }
            else {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent()) {
                pnode = findProject(pnode.getParentQID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty()) {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            ProjectNode curProj = findProject(current.getQIdentifier());

            fnode = curProj.getFile(tnode.getParentFileID()).cloneNoChildren();

            curProj.addFile(fnode);

            fnode.addType(tnode.cloneNoChildren());

            retVal.setProject(root);
        }
        else if (cnode instanceof MethodNode) {
            MethodNode methnode = (MethodNode) cnode;

            TypeNode tnode = findType(methnode.getParentTypeID());

            FileNode fnode = findFile(tnode.getParentFileID());

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null) {
                pnode = findProject(fnode.getParentID());
            }
            else {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent()) {
                pnode = findProject(pnode.getParentQID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty()) {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            ProjectNode curProj = findProject(current.getQIdentifier());

            fnode = curProj.getFile(tnode.getParentFileID()).cloneNoChildren();

            curProj.addFile(fnode);

            tnode = tnode.cloneNoChildren();
            fnode.addType(tnode);

            try {
                tnode.addMethod(methnode.clone());
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }

        return retVal;
    }
}
