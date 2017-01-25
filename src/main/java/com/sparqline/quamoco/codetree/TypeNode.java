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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;

/**
 * TypeNode -
 *
 * @author Isaac Griffith
 */
public class TypeNode extends CodeNode {

    @Expose
    private final Map<String, MethodNode> methods = Maps.newHashMap();
    @Expose
    private final Map<String, FieldNode>  fields  = Maps.newHashMap();
    @Expose
    private String                        parentFileID;
    @Expose
    private boolean                       isInterface;
    @Expose
    private boolean                       isAbstract;

    private TypeNode()
    {
        super();
    }

    /**
     * @param identifier
     * @param start
     * @param end
     */
    public TypeNode(final String qIdentifier, final String identifier, final int start, final int end)
    {
        super(qIdentifier, identifier, start, end);
    }

    public void removeMethod(final MethodNode method)
    {
        if (method == null)
        {
            return;
        }

        methods.remove(method.getQIdentifier());
    }

    public MethodNode getMethod(final int line)
    {
        if (line >= getStart() && line <= getEnd())
        {
            Collection<MethodNode> nodes = methods.values();
            for (MethodNode node : nodes)
            {
                if (node.containsLine(line))
                {
                    return node;
                }
            }
        }

        return null;
    }

    public MethodNode getMethod(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (methods.containsKey(this.qIdentifier + "#" + name))
            return methods.get(this.qIdentifier + "#" + name);

        return addMethod(name);
    }

    public MethodNode addMethod(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (methods.containsKey(name))
            return methods.get(name);

        MethodNode m = new MethodNode(this.qIdentifier + "#" + name, name, false, 1, 1);
        methods.put(name, m);
        return m;
    }

    public boolean addMethod(final MethodNode child)
    {
        if (child == null)
        {
            return false;
        }

        if (child.getStart() < getStart() || child.getEnd() > getEnd())
        {
            throw new IllegalArgumentException(
                    "A method's start cannot be less than the type's start line, and a method's end cannot exceed a type's end line.");
        }

        methods.put(child.getQIdentifier(), child);

        return true;
    }

    public Set<MethodNode> getMethods()
    {
        return Sets.newHashSet(methods.values());
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.quamoco.codetree.CodeNode#getType()
     */
    @Override
    public String getType()
    {
        return CodeNodeType.TYPE;
    }

    public void removeField(final FieldNode field)
    {
        if (field == null)
        {
            return;
        }

        fields.remove(field.getQIdentifier());
    }

    public FieldNode getField(final int line)
    {
        if (line >= getStart() && line <= getEnd())
        {
            Collection<FieldNode> nodes = fields.values();
            for (final FieldNode node : nodes)
            {
                if (node.containsLine(line))
                {
                    return node;
                }
            }
        }

        return null;
    }

    public boolean addField(final FieldNode child)
    {
        if (child == null)
        {
            return false;
        }

        if (child.getStart() < getStart() || child.getEnd() > getEnd())
        {
            throw new IllegalArgumentException(
                    "A field's start cannot be less than the type's start line, and a field's end cannot exceed a type's end line.");
        }

        fields.put(child.getQIdentifier(), child);

        return true;
    }

    public Set<FieldNode> getFields()
    {
        return Sets.newHashSet(fields.values());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        if (obj instanceof TypeNode)
        {
            TypeNode other = (TypeNode) obj;

            if (!other.qIdentifier.equals(qIdentifier))
                return false;
        }

        return true && super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sparqline.quamoco.codetree.CodeNode#update(com.sparqline.quamoco.
     * codetree.CodeNode)
     */
    @Override
    public void update(CodeNode t)
    {
        if (t == null)
            return;

        if (!(t instanceof TypeNode))
            return;

        TypeNode type = (TypeNode) t;

        updateLocation(t.getStart(), t.getEnd());

        for (MethodNode m : type.getMethods())
        {
            if (getMethod(m.getQIdentifier()) != null)
            {
                getMethod(m.getQIdentifier()).update(m);
            }
            else
            {
                addMethod(m);
            }
        }

        for (FieldNode f : type.getFields())
        {
            if (getField(f.getQIdentifier()) != null)
            {
                getField(f.getQIdentifier()).update(f);
            }
            else
            {
                addField(f);
            }
        }

        for (String key : t.metrics.keySet())
        {
            this.metrics.put(key, t.metrics.get(key));
        }
    }

    /**
     * @param name
     * @return
     */
    private FieldNode getField(String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (fields.containsKey(name))
            return fields.get(name);

        return addField(name);
    }

    public FieldNode addField(final String name)
    {
        if (name == null || name.isEmpty())
            return null;

        if (fields.containsKey(name))
            return fields.get(name);

        FieldNode f = new FieldNode(this.qIdentifier + "#" + name, name, 1);
        fields.put(name, f);
        return f;
    }

    /**
     * @param cnode
     * @return
     */
    public boolean hasMethod(MethodNode cnode)
    {
        return methods.containsKey(cnode.getQIdentifier());
    }

    public String getParentFileID()
    {
        return parentFileID;
    }

    public void setParentFileID(String parentFileID)
    {
        this.parentFileID = parentFileID;
    }

    /*
     * (non-Javadoc)
     * @see com.sparqline.quamoco.codetree.CodeNode#cloneNoChildren()
     */
    @Override
    public TypeNode cloneNoChildren()
    {
        TypeNode tnode = new TypeNode(qIdentifier, name, getStart(), getEnd());

        copyMetrics(tnode);

        return tnode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    protected TypeNode clone() throws CloneNotSupportedException
    {
        TypeNode tnode = cloneNoChildren();

        for (String key : fields.keySet())
        {
            tnode.addField(fields.get(key).clone());
        }

        for (String key : methods.keySet())
        {
            tnode.addMethod(methods.get(key).clone());
        }

        return tnode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        // TODO Auto-generated method stub
        super.finalize();
    }

    /**
     * @return
     */
    public boolean isInterface()
    {
        return isInterface;
    }

    /**
     * @return
     */
    public boolean isAbstract()
    {
        return isAbstract;
    }
}
