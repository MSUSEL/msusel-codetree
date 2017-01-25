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
import com.google.gson.annotations.Expose;

/**
 * @author fate
 *
 */
public class ModuleNode extends CodeNode {

	@Expose
	private Map<String, FileNode> files;
	@Expose
	private String parentID;

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

	public List<TypeNode> getTypes() {
		List<TypeNode> list = Lists.newArrayList();

		for (FileNode fn : files.values()) {
			list.addAll(fn.getTypes());
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sparqline.quamoco.codetree.CodeNode#update(com.sparqline.quamoco.
	 * codetree.CodeNode)
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
			} else {
				addFile(f);
			}
		}

		for (String key : c.metrics.keySet()) {
			this.metrics.put(key, c.metrics.get(key));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sparqline.quamoco.codetree.CodeNode#cloneNoChildren()
	 */
	@Override
	public ModuleNode cloneNoChildren() {
		ModuleNode mnode = new ModuleNode(this.qIdentifier);
		mnode.setEnd(this.getEnd());
		mnode.setStart(this.getStart());

		copyMetrics(mnode);

		return mnode;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected ModuleNode clone() throws CloneNotSupportedException {
		ModuleNode mnode = cloneNoChildren();

		for (String key : files.keySet()) {
			mnode.addFile(files.get(key).clone());
		}

		return mnode;
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
	 * @param string
	 * @return
	 */
	public boolean hasFile(String string) {
		return files.containsKey(string);
	}
}
