/**
 * 
 */
package com.sparqline.quamoco.codetree;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparqline.quamoco.codetree.json.FieldNodeDeserializer;
import com.sparqline.quamoco.codetree.json.FileNodeDeserializer;
import com.sparqline.quamoco.codetree.json.MethodNodeDeserializer;
import com.sparqline.quamoco.codetree.json.ModuleNodeDeserializer;
import com.sparqline.quamoco.codetree.json.ProjectNodeDeserializer;
import com.sparqline.quamoco.codetree.json.TypeNodeDeserializer;

/**
 * @author fate
 *
 */
public class ProjectNode extends CodeNode {

    private Map<String, ProjectNode> subprojects;
    private Map<String, ModuleNode>  modules;
    private Map<String, FileNode>    files;

    public ProjectNode() {
        subprojects = Maps.newHashMap();
        modules = Maps.newHashMap();
        files = Maps.newHashMap();
    }

    public ProjectNode(String qIdentifier) {
        super(qIdentifier, qIdentifier, 1, 1);
        subprojects = Maps.newHashMap();
        modules = Maps.newHashMap();
        files = Maps.newHashMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#getType()
     */
    @Override
    public String getType() {
        return CodeNodeType.PROJECT;
    }

    public void addSubProject(ProjectNode node) {
        if (node == null)
            return;

        if (subprojects.containsKey(node.getQIdentifier()))
            subprojects.get(node.getQIdentifier()).update(node);
        else
            subprojects.put(node.getQIdentifier(), node);
    }

    public ProjectNode addSubProject(String key) {
        if (key == null || key.isEmpty())
            return null;

        if (subprojects.containsKey(key))
            return subprojects.get(key);

        ProjectNode node = new ProjectNode(key);
        subprojects.put(key, node);
        return node;
    }

    public ProjectNode getSubProject(String key) {
        if (key == null || key.isEmpty())
            return null;

        return subprojects.get(key);
    }

    public Set<ProjectNode> getSubProjects() {
        return Sets.newHashSet(subprojects.values());
    }

    public void addModule(ModuleNode node) {
        if (node == null)
            return;

        if (modules.containsKey(node.getQIdentifier()))
            modules.get(node.getQIdentifier()).update(node);
        else
            modules.put(node.getQIdentifier(), node);
    }

    public ModuleNode addModule(String key) {
        if (key == null || key.isEmpty())
            return null;

        if (modules.containsKey(key))
            return modules.get(key);

        ModuleNode node = new ModuleNode(key);
        modules.put(key, node);
        return node;
    }

    public ModuleNode getModule(String key) {
        if (key == null || key.isEmpty())
            return null;

        return modules.get(key);
    }

    public Set<ModuleNode> getModules() {
        return Sets.newHashSet(modules.values());
    }

    public void addFile(FileNode node) {
        if (node == null)
            return;

        if (files.containsKey(node.getQIdentifier()))
            files.get(node).update(node);
        else
            files.put(node.getQIdentifier(), node);
    }

    public FileNode addFile(String path) {
        if (path == null || path.isEmpty())
            return null;

        if (files.containsKey(path))
            return files.get(path);

        FileNode fn = new FileNode(path);
        files.put(path, fn);
        return fn;
    }

    public FileNode getFile(String path) {
        if (path == null || path.isEmpty())
            return null;

        return files.get(path);
    }

    public Set<FileNode> getFiles() {
        return Sets.newHashSet(files.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#update(com.sparqline.quamoco.codetree.CodeNode)
     */
    @Override
    public void update(CodeNode c) {
        if (c == null)
            return;

        if (!(c instanceof ProjectNode))
            return;

        ProjectNode pn = (ProjectNode) c;

        for (ModuleNode m : pn.getModules()) {
            if (getModule(m.getQIdentifier()) != null) {
                getModule(m.getQIdentifier()).update(m);
            }
            else {
                addModule(m);
            }
        }

        for (ProjectNode p : pn.getSubProjects()) {
            if (getSubProject(p.getQIdentifier()) != null) {
                getSubProject(p.getQIdentifier()).update(p);
            }
            else {
                addSubProject(p);
            }
        }

        for (FileNode f : pn.getFiles()) {
            if (getFile(f.getQIdentifier()) != null) {
                getFile(f.getQIdentifier()).update(f);
            }
            else {
                addFile(f);
            }
        }
        
        for (String key : pn.metrics.keySet()) {
            this.metrics.put(key, pn.metrics.get(key));
        }
    }

    /**
     * @return
     */
    public Set<String> getFileKeys() {
        return files.keySet();
    }

    /**
     * @param file
     */
    public void removeFile(String file) {
        files.remove(file);
    }

    public static ProjectNode createFromJson(String json) {
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
        return g.fromJson(json, ProjectNode.class);
    }
    
    /**
     * @return
     */
    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
