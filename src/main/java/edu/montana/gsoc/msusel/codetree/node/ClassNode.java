package edu.montana.gsoc.msusel.codetree.node;

import com.google.common.collect.Sets;
import edu.montana.gsoc.msusel.codetree.INode;

import java.util.Set;

public class ClassNode extends TypeNode implements Cloneable {

    boolean abstrct

    /**
     *
     */
    public ClassNode()
    {
        // TODO Auto-generated constructor stub
    }

    public Set<FieldNode> getFields() {
        Set<FieldNode> fields = Sets.newHashSet();
        children.forEach(abstractNode -> {if (abstractNode instanceof FieldNode) fields.add((FieldNode)abstractNode);} );
        return fields;
    }

    public Set<MethodNode> getMethods() {
        Set<MethodNode> methods = Sets.newHashSet();
        children.forEach(abstractNode -> {if (abstractNode instanceof MethodNode) methods.add((MethodNode)abstractNode);} );
        return methods;
    }

    public Set<ConstructorNode> getConstructors() {
        Set<ConstructorNode> consts = Sets.newHashSet();
        children.forEach(abstractNode -> {if (abstractNode instanceof ConstructorNode) consts.add((ConstructorNode)abstractNode);} );
        return consts;
    }

    public Set<DestructorNode> getDestructors() {
        Set<DestructorNode> destructs = Sets.newHashSet();
        children.forEach(abstractNode -> {if (abstractNode instanceof DestructorNode) destructs.add((DestructorNode)abstractNode);} );
        return fields;
    }

    public Set<InitializerNode> getInitializers() {
        Set<InitializerNode> inits = Sets.newHashSet();
        children.forEach(abstractNode -> {if (abstractNode instanceof InitializerNode) inits.add((InitializerNode)abstractNode);} );
        return fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInterface()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String type()
    {
        return "Class";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(INode other)
    {
        TypeNode tnode = new TypeNode(key, name);
        tnode.setStart(getStart());
        tnode.setEnd(getEnd());

        copyMetrics(tnode);

        return tnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TypeNode clone() throws CloneNotSupportedException
    {
        TypeNode tnode = cloneNoChildren();

        for (FieldNode field : getFields())
        {
            tnode.addField(field.clone());
        }

        for (MethodNode method : getMethods())
        {
            tnode.addMethod(method.clone());
        }

        return tnode;
    }

    /**
     * Searches this TypeNode for a method whose line range contains the given
     * line
     *
     * @param line
     *            The line
     * @return MethodNode whose range contains the given line, or null if no
     *         such MethodNode exists in this type.
     */
    public MethodNode findMethod(int line)
    {
        if (line < getStart() || line > getEnd())
            return null;

        for (MethodNode method : getMethods())
        {
            if (method.containsLine(line))
                return method;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeNode cloneNoChildren()
    {
        TypeNode tnode = new TypeNode(key, name);
        tnode.setStart(getStart());
        tnode.setEnd(getEnd());

        copyMetrics(tnode);

        return tnode;
    }
}
