/**
 * 
 */
package com.sparqline.quamoco.codetree;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author fate
 *
 */
public class ModuleNode extends CodeNode {

    private Map<String, FileNode> files;

    public ModuleNode() {
        super();
        files = Maps.newHashMap();
    }

    public ModuleNode(String key) {
        super(key, key, 0, 0);
        files = Maps.newHashMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sparqline.quamoco.codetree.CodeNode#getType()
     */
    @Override
    public String getType() {
        return CodeNodeType.MODULE;
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

        ModuleNode mn = (ModuleNode) c;

        for (FileNode f : mn.getFiles()) {
            if (getFile(f.getQIdentifier()) != null) {
                getFile(f.getQIdentifier()).update(f);
            }
            else {
                addFile(f);
            }
        }
    }

}
