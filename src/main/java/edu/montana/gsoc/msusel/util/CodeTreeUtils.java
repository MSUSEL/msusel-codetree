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
package edu.montana.gsoc.msusel.util;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import edu.montana.gsoc.msusel.CodeTree;
import edu.montana.gsoc.msusel.INode;
import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.FileNode;
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.ModuleNode;
import edu.montana.gsoc.msusel.node.NamespaceNode;
import edu.montana.gsoc.msusel.node.ProjectNode;
import edu.montana.gsoc.msusel.node.StatementNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * This class provides utility methods for modifying a given CodeTree.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class CodeTreeUtils {

    /**
     * The tree used for the various operations herein.
     */
    private final CodeTree tree;

    /**
     * Constructs a new CodeTreeUtils for the given tree.
     * 
     * @param tree
     *            The CodeTree on which this CodeTreeUtils operates.
     */
    public CodeTreeUtils(CodeTree tree)
    {
        this.tree = tree;
    }

    /**
     * Extracts the code tree section from the root down to the provided node
     * and then everything below the node.
     * 
     * @param node
     *            Node whose CodeTree is to be extracted.
     * @return The code tree extracted for the given node. or null if the node
     *         is null or not present in the tree.
     */
    public CodeTree extractTree(INode node)
    {
        CodeTree retVal = null;

        if (node instanceof ProjectNode)
        {
            ProjectNode pnode = (ProjectNode) node;
            if (pnode.getParentID() == null)
            {
                retVal = tree;
            }
            else
            {
                retVal = new CodeTree();
                Stack<ProjectNode> stack = new Stack<>();
                stack.push(pnode);
                while (pnode.hasParent())
                {
                    pnode = findProject(pnode.getParentID());
                    stack.push(pnode);
                }

                ProjectNode current = stack.pop().cloneNoChildren();
                ProjectNode next = null;
                while (!stack.isEmpty())
                {
                    if (stack.size() == 1)
                    {
                        try
                        {
                            next = stack.pop().clone();
                        }
                        catch (CloneNotSupportedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        next = stack.pop().cloneNoChildren();
                    }

                    if (next != null)
                    {
                        current.addSubProject(next);
                        current = next;
                    }
                    next = null;
                }
            }
        }
        else if (node instanceof ModuleNode)
        {
            ModuleNode mnode = (ModuleNode) node;

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = findProject(mnode.getParentID());
            stack.push(pnode);
            while (pnode.hasParent())
            {
                pnode = findProject(pnode.getParentID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty())
            {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            try
            {
                ModuleNode mod = mnode.clone();
                current.addModule(mod);
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }
        else if (node instanceof FileNode)
        {
            FileNode fnode = (FileNode) node;

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null)
            {
                pnode = findProject(fnode.getParentID());
            }
            else
            {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent())
            {
                pnode = findProject(pnode.getParentID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            ProjectNode actual = current;
            while (!stack.isEmpty())
            {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                if (next.getQIdentifier().equals(fnode.getParentID()))
                    actual = next;

                current = next;
                next = null;
            }

            try
            {
                actual.addFile(fnode.clone());
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }
        else if (node instanceof TypeNode)
        {
            TypeNode tnode = (TypeNode) node;

            FileNode fnode = findFile(tnode.getParentID());

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null)
            {
                pnode = findProject(fnode.getParentID());
            }
            else
            {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent())
            {
                pnode = findProject(pnode.getParentID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty())
            {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            ProjectNode curProj = findProject(current.getQIdentifier());

            fnode = curProj.getFile(tnode.getParentID()).cloneNoChildren();

            curProj.addFile(fnode);

            fnode.addType(tnode.cloneNoChildren());

            retVal.setProject(root);
        }
        else if (node instanceof MethodNode)
        {
            MethodNode methnode = (MethodNode) node;

            TypeNode tnode = findType(methnode.getParentID());

            FileNode fnode = findFile(tnode.getParentID());

            retVal = new CodeTree();
            Stack<ProjectNode> stack = new Stack<>();
            ProjectNode pnode = null;
            ModuleNode mnode = null;

            if (findProject(fnode.getParentID()) != null)
            {
                pnode = findProject(fnode.getParentID());
            }
            else
            {
                mnode = findModule(fnode.getParentID());
                pnode = findProject(mnode.getParentID());
            }

            stack.push(pnode);
            while (pnode.hasParent())
            {
                pnode = findProject(pnode.getParentID());
                stack.push(pnode);
            }

            ProjectNode current = stack.pop().cloneNoChildren();
            ProjectNode root = current;
            ProjectNode next;
            while (!stack.isEmpty())
            {
                next = stack.pop().cloneNoChildren();

                current.addSubProject(next);
                current = next;
                next = null;
            }

            ProjectNode curProj = findProject(current.getQIdentifier());

            fnode = curProj.getFile(tnode.getParentID()).cloneNoChildren();

            curProj.addFile(fnode);

            tnode = tnode.cloneNoChildren();
            fnode.addType(tnode);

            try
            {
                tnode.addMethod(methnode.clone());
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }

            retVal.setProject(root);
        }

        return retVal;
    }

    /**
     * Searches for a file with the given qualified identifier in this CodeTree.
     * 
     * @param qid
     *            Qualified identifier
     * @return The FileNode matching the given identifier, or null if the given
     *         identifier is null or empty or if no such file exists in the
     *         tree.
     */
    public FileNode findFile(final String qid)
    {
        if (qid == null || qid.isEmpty())
        {
            return null;
        }

        for (ProjectNode p : getProjects())
        {
            if (p.hasFile(qid))
            {
                return p.getFile(qid);
            }
            else
            {
                for (ModuleNode m : p.getModules())
                {
                    if (m.hasFile(qid))
                        return m.getFile(qid);
                }
            }
        }
        return null;
    }

    /**
     * Searches for a MethodNode with the given qualified identifier.
     * 
     * @param identifier
     *            Qualified Identifier
     * @return MethodNode with matching qualified identifier, or null if the
     *         identifier is null, empty, or no such MethodNode exists.
     */
    public MethodNode findMethod(final String identifier)
    {
        if (identifier == null || identifier.isEmpty())
            return null;
        String[] ids = identifier.split("#");
        TypeNode tn = findType(ids[0]);

        if (tn != null)
            return tn.getMethod(ids[1]);

        return null;
    }

    /**
     * Searches the CodeTree for a ModuleNode with the given qualified
     * identifier.
     * 
     * @param qIdentifier
     *            Qualified Identifier
     * @return The ModuleNode with matching qualified identifier, or null if the
     *         provided qualified identifier is null or empty or no such
     *         matching ModuleNode exists.
     */
    public ModuleNode findModule(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return null;

        Set<ProjectNode> projs = getProjects();

        for (ProjectNode p : projs)
        {
            if (p.hasModule(qIdentifier))
                return p.getModule(qIdentifier);
        }

        return null;
    }

    /**
     * Given a node this method will search for the parent node using it's
     * parent identifier.
     * 
     * @param node
     *            Node whose parent is to be found for.
     * @return A Node with matching qualified identifier to the given node's
     *         parent identifier, or null if the provided parent id is null or
     *         no such node exists in the code tree with the parent id.
     */
    public INode findParent(INode node)
    {
        INode parent = null;
        if (node instanceof FieldNode)
        {
            parent = findType(node.getParentID());
        }
        else if (node instanceof StatementNode)
        {
            parent = findMethod(node.getParentID());
        }
        else if (node instanceof MethodNode)
        {
            parent = findType(node.getParentID());
        }
        else if (node instanceof TypeNode)
        {
            parent = findFile(node.getParentID());
        }
        else if (node instanceof FileNode)
        {
            parent = findProject(node.getParentID());
            if (parent == null)
                parent = findModule(node.getParentID());
        }
        else if (node instanceof ModuleNode)
        {
            parent = findProject(node.getParentID());
        }
        else if (node instanceof NamespaceNode)
        {
            parent = findProject(node.getParentID());
        }
        else if (node instanceof ProjectNode)
        {
            if (node.getParentID() != null)
                parent = findProject(node.getParentID());
        }

        return parent;
    }

    /**
     * Searches the code tree for a project with a qualified identifier matching
     * the qualified identifier provided.
     * 
     * @param qid
     *            Qualified Identifier
     * @return ProjectNode with matching qualified identifier as the one
     *         provided, or null if no such ProjectNode exists in the CodeTree
     *         or the provided identifier is null or empty.
     */
    public ProjectNode findProject(String qid)
    {
        if (qid == null || qid.isEmpty())
            return null;

        Queue<ProjectNode> queue = Queues.newArrayDeque();

        if (this.tree.getProject() != null)
            queue.offer(this.tree.getProject());

        while (!queue.isEmpty())
        {
            ProjectNode node = queue.poll();
            if (node.getQIdentifier().equals(qid))
                return node;

            for (ProjectNode pn : node.getSubProjects())
            {
                queue.offer(pn);
            }
        }

        return null;
    }

    /**
     * Searches the CodeTree for a TypeNode whose qualified identifier matches
     * the one provided.
     * 
     * @param key
     *            Qualified Identifier
     * @return TypeNode with matching qualified identifier to the one provided,
     *         or null if no such TypeNode exists in the CodeTree or the
     *         provided identifier is null or empty.
     */
    public TypeNode findType(final String key)
    {
        if (key == null || key.isEmpty())
            return null;

        for (final TypeNode tnode : getTypes())
        {
            if (tnode.getQIdentifier().equals(key))
            {
                return tnode;
            }
        }

        return null;
    }

    /**
     * Searches the CodeTree for a FileNode whose qualified identifier matches
     * the one provided.
     * 
     * @param file
     *            Qualified Identifier
     * @return FileNode with matching qualified identifier to the one provided,
     *         or null if no such FileNode exists in the CodeTree or the
     *         provided identifier is null or empty.
     */
    public FileNode getFile(final String file)
    {
        if (file == null || file.isEmpty())
        {
            return null;
        }

        for (FileNode fn : getFiles())
        {
            if (fn.getQIdentifier().equals(file))
                return fn;
        }

        return null;
    }

    /**
     * @return The set of all files within the tree.
     */
    public Set<FileNode> getFiles()
    {
        Set<FileNode> files = Sets.newHashSet();

        Queue<ProjectNode> queue = Queues.newArrayDeque();

        queue.add(tree.getProject());

        while (!queue.isEmpty())
        {
            ProjectNode pn = queue.poll();
            files.addAll(pn.getFiles());
            queue.addAll(pn.getSubProjects());
        }

        return files;
    }

    /**
     * @return The set of all methods within the tree.
     */
    public Set<MethodNode> getMethods()
    {
        final Set<MethodNode> methods = Sets.newHashSet();

        getTypes().forEach((type) -> {
            methods.addAll(type.getMethods());
        });

        return methods;
    }

    /**
     * @return The set of all projects within the tree (including the root
     *         project)
     */
    public Set<ProjectNode> getProjects()
    {
        Set<ProjectNode> projs = Sets.newHashSet();
        Queue<ProjectNode> q = Queues.newArrayDeque();

        if (tree.getProject() != null)
            q.offer(tree.getProject());

        while (!q.isEmpty())
        {
            ProjectNode pnode = q.poll();
            projs.add(pnode);
            q.addAll(pnode.getSubProjects());
        }

        return projs;
    }

    /**
     * @return The set of all known types in the tree.
     */
    public Set<TypeNode> getTypes()
    {
        final Set<TypeNode> types = Sets.newHashSet();

        getFiles().forEach((file) -> {
            types.addAll(file.getTypes());
        });

        return types;
    }

    /**
     * Merges the CodeTree this class operates upon with the one provided.
     * 
     * @param other
     *            CodeTree to merge into the currently operated on CodeTree.
     */
    public void merge(CodeTree other)
    {
        if (other == null)
            return;

        ProjectNode pn = other.getProject();

        if (tree.getProject() == null)
        {
            tree.setProject(pn);
        }
        if (pn.hasParent())
        {
            if (pn.getParentID().equals(tree.getProject().getQIdentifier()))
            {
                tree.getProject().addSubProject(pn);
            }
        }
        else if (tree.getProject().equals(pn))
        {
            tree.getProject().update(pn);
        }
    }

    /**
     * Finds the file with the given identifier and removes it from its parent
     * in the CodeTree.
     * 
     * @param file
     *            Qualified Identifier of the file to be removed.
     */
    public void removeFile(final String file)
    {
        if (file == null || file.isEmpty())
        {
            return;
        }

        INode parent = findParent(findFile(file));
        if (parent instanceof ProjectNode)
        {
            ((ProjectNode) parent).removeFile(file);
        }
        else if (parent instanceof ModuleNode)
        {
            ((ModuleNode) parent).removeFile(file);
        }
    }

    /**
     * Updates a file in the CodeTree using the provided file. This method first
     * searches the tree for a corresponding file matching the provided one. If
     * found the existing file is merged with the provided one. If no such file
     * exists, the new one is added to the tree. Finally, if the provided file
     * is null, nothing happens.
     * 
     * @param node
     *            File to be used to update the tree.
     */
    public synchronized void updateFile(final FileNode node)
    {
        if (node == null)
            return;

        INode p = null;
        if (findProject(node.getParentID()) != null)
            p = findProject(node.getParentID());
        else
            p = findModule(node.getParentID());

        if (p instanceof ProjectNode)
        {
            if (((ProjectNode) p).getFile(node.getQIdentifier()) == null)
            {
                ((ProjectNode) p).addFile(node);
            }

            ((ProjectNode) p).getFile(node.getQIdentifier()).update(node);
        }
        else
        {
            if (((ModuleNode) p).getFile(node.getQIdentifier()) == null)
            {
                ((ModuleNode) p).addFile(node);
            }

            ((ModuleNode) p).getFile(node.getQIdentifier()).update(node);
        }
    }

    /**
     * Updates the root project of the CodeTree by merging it with the provided
     * project.
     * 
     * @param node
     *            Project to merge into the root project of the tree.
     */
    public synchronized void updateRootProject(final ProjectNode node)
    {
        if (node == null)
            return;

        if (tree.getProject() == null)
            tree.setProject(node);
        else
            tree.getProject().update(node);
    }
}