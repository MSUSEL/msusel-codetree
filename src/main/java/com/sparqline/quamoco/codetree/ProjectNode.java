/**
 * 
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
import com.sparqline.quamoco.codetree.json.ModuleNodeDeserializer;
import com.sparqline.quamoco.codetree.json.ProjectNodeDeserializer;
import com.sparqline.quamoco.codetree.json.TypeNodeDeserializer;

/**
 * @author fate
 *
 */
public class ProjectNode extends CodeNode {

    @Expose
    private Map<String, ProjectNode> subprojects;
    @Expose
    private Map<String, ModuleNode>  modules;
    @Expose
    private Map<String, FileNode>    files;
    @Expose
    private String                   parentQID = null;

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

    public void setParentQID(String qid) {        
        if (qid != null && qid.equals(this.qIdentifier))
            throw new IllegalArgumentException("Parent qID key cannot match this.qIdentifier.");
        parentQID = qid;
    }

    public String getParentQID() {
        return parentQID;
    }

    public boolean hasParent() {
        return parentQID != null;
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

        node.setParentQID(this.qIdentifier);
    }

    public ProjectNode addSubProject(String key) {
        if (key == null || key.isEmpty())
            return null;

        if (subprojects.containsKey(key))
            return subprojects.get(key);

        ProjectNode node = new ProjectNode(key);
        subprojects.put(key, node);
        node.setParentQID(this.qIdentifier);
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

    public ProjectNode removeSubProject(String id) {
        if (id == null || id.isEmpty())
            return null;

        if (subprojects.containsKey(id)) {
            ProjectNode n = subprojects.remove(id);
            n.setParentQID(null);
            return n;
        }

        return null;
    }

    public ProjectNode removeSubProject(ProjectNode node) {
        if (node == null)
            return node;

        if (subprojects.containsKey(node.getQIdentifier())) {
            ProjectNode n = subprojects.remove(node.getQIdentifier());
            n.setParentQID(null);
            return n;
        }

        return node;
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
            files.get(node.getQIdentifier()).update(node);
        else
            files.put(node.getQIdentifier(), node);

        node.setParentID(this.qIdentifier);
    }

    public FileNode addFile(String path) {
        if (path == null || path.isEmpty())
            return null;

        if (files.containsKey(path))
            return files.get(path);

        FileNode fn = new FileNode(path);
        files.put(path, fn);

        fn.setParentID(this.parentQID);

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
    
    public List<TypeNode> getTypes() {
    	List<TypeNode> list = Lists.newArrayList();
    	
    	for (ProjectNode pn : subprojects.values()) {
    		list.addAll(pn.getTypes());
    	}
    	
    	for (ModuleNode mn : modules.values()) {
    		list.addAll(mn.getTypes());
    	}
    	
    	for (FileNode fn : files.values()) {
    		list.addAll(fn.getTypes());
    	}
    	
    	return list;
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

    public String getContentsJSON(int tabSize) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < tabSize; i++)
            tabs.append("\t");
        String tab = tabs.toString();

        StringBuilder builder = new StringBuilder();
        builder.append(tab + "{" + "\n");
        builder.append(tab + "\tname:" + name + ",\n");
        builder.append(tab + "\tqIdentifier:" + qIdentifier + ",\n");
        builder.append(tab + "\tparentQID:" + parentQID + ",\n");
        builder.append(tab + "\tmetrics:{\n");
        for (String k : metrics.keySet()) {
            builder.append(tab + "\t\t" + k + ":" + metrics.get(k) + ",\n");
        }
        builder.append(tab + "\t}\n");
        builder.append(tab + "subprojects:{\n");
        for (String k : subprojects.keySet()) {
            builder.append(tab + "\t\t" + k + ":" + subprojects.get(k).getContentsJSON(tabSize + 1) + ",\n");
        }
        builder.append(tab + "\t}\n");
        builder.append(tab + "}");
        return builder.toString();
    }

    /**
     * @return
     */
    public boolean hasChildren() {
        return !subprojects.isEmpty();
    }

    /**
     * @return
     */
    public List<MethodNode> getMethods() {
        List<MethodNode> methods = Lists.newArrayList();

        for (String f : files.keySet()) {
            methods.addAll(files.get(f).getMethods());
        }

        return methods;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#cloneNoChildren()
     */
    @Override
    public ProjectNode cloneNoChildren() {
        ProjectNode pnode = new ProjectNode(qIdentifier);

        pnode.setEnd(getEnd());
        pnode.setStart(getStart());

        copyMetrics(pnode);

        pnode.setParentQID(this.parentQID);

        return pnode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected ProjectNode clone() throws CloneNotSupportedException {
        ProjectNode pnode = cloneNoChildren();

        for (String key : files.keySet()) {
            pnode.addFile(files.get(key).clone());
        }

        for (String key : modules.keySet()) {
            pnode.addModule(modules.get(key).clone());
        }

        for (String key : subprojects.keySet()) {
            pnode.addSubProject(subprojects.get(key).clone());
        }

        return pnode;
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

    /**
     * @param qIdentifier
     * @return
     */
    public boolean hasModule(String qIdentifier) {
        return modules.containsKey(qIdentifier);
    }

    /**
     * @param string
     * @return
     */
    public boolean hasFile(String string) {
        return files.containsKey(string);
    }
}
