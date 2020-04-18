/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.util.DbUtils;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * The base class at the head of the types representing a language type system
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Type extends Component implements ComponentContainer {

//    @Builder(buildMethodName = "create")
//    public Type(String name, int start, int end, String compKey, Accessibility accessibility) {
//        set("name", name, "start", start, "end", end, "compKey", compKey);
//        if (accessibility != null)
//            setAccessibility(accessibility);
//        else
//            setAccessibility(Accessibility.PUBLIC);
//    }

    /**
     * @return true if this type is abstract; false otherwise.
     */
    public boolean isAbstract() {
        return getBoolean("abstract");
    }

    /**
     * Marks this type as abstract or not.
     *
     * @param abst the new value of the abstract field
     */
    public void setAbstract(boolean abst) {
        setBoolean("abstract", abst);
        save();
    }

    public List<System> getParentSystems() {
        File parent = getParentFile();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    /**
     * @return The parent system of this type or null if no such parent has been defined
     */
    public System getParentSystem() {
        File file = getParentFile();
        if (file != null)
            return file.getParentSystem();
        return null;
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        getParentNamespaces().forEach(ns -> projects.addAll(ns.getParentProjects()));
        return projects;
//        return DbUtils.getParentProject(this.getClass(), (Integer) getId());
    }

    /**
     * @return The parent project of this type or null if no such parent has been defined
     */
    public Project getParentProject() {
        File file = getParentFile();
        if (file != null)
            return file.getParentProject();
        return null;
    }

    public List<Module> getParentModules() {
        List<Module> parents = Lists.newLinkedList();
        parents.add(getParentFile().getParentModule());
        return parents;
    }

    /**
     * @return The parent module of this type or null if no such parent has been defined
     */
    public Module getParentModule() {
        File file = getParentFile();
        if (file != null)
            return file.getParentModule();
        return null;
    }

    public List<Namespace> getParentNamespaces() {
        List<Namespace> parents = Lists.newLinkedList();
        File parent = getParentFile();
        if (parent != null)
            parents.addAll(getParentFile().getParentNamespaces());
        return parents;
    }

    public Namespace getParentNamespace() {
        File file = getParentFile();
        if (file != null)
            return file.getParentNamespace();
        return null;
    }

    public List<File> getParentFiles() {
        List<File> files = Lists.newLinkedList();
        if (getParentFile() != null)
            files.add(getParentFile());
        return files;
    }

    public File getParentFile() {
        return parent(File.class);
    }

    public void addMember(Member member) {
        if (member != null) {
            add(member);
            save();
        }
    }

    public void removeMember(Member member) {
        if (member != null) {
            remove(member);
            save();
        }
    }

    public List<Constructor> getConstructors() {
        return getAll(Constructor.class);
    }

    @Override
    public List<Method> getMethods() {
        return getAll(Method.class);
    }

    @Override
    public List<Destructor> getDestructors() {
        return getAll(Destructor.class);
    }

    @Override
    public List<Field> getFields() {
        return getAll(Field.class);
    }

    @Override
    public List<Initializer> getInitializers() {
        return getAll(Initializer.class);
    }

    public Initializer getStaticInitializer(int num) {
        try {
            return get(Initializer.class, "instance = ? AND number = ?", true, num).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Initializer getInstanceInitializer(int num) {
        try {
            return get(Initializer.class, "instance = ? AND number = ?", true, num).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public List<Literal> getLiterals() {
        return getAll(Literal.class);
    }

    @Override
    public String getRefKey() {
        return getString("compKey");
    }

    public Set<Type> getRealizes() {
        return DbUtils.getRelationFrom(this, RelationType.REALIZATION);
    }

    public boolean isRealizing(Type type) {
        return getRealizes().contains(type);
    }

    public void realizes(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getGeneralizes() {
        return DbUtils.getRelationFrom(this, RelationType.GENERALIZATION);
    }

    public void generalizes(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public Set<Type> getContained() {
        return DbUtils.getRelationFrom(this, RelationType.CONTAINMENT);
    }

    public void contains(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.CONTAINMENT);
    }

    public Set<Type> getAssociatedTo() {
        return DbUtils.getRelationFrom(this, RelationType.ASSOCIATION);
    }

    public boolean isAssociatedTo(Type type) {
        return getAssociatedTo().contains(type);
    }

    public void associatedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public void removeAssociatedTo(Type other) {
        deleteRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public Set<Type> getAggregatedTo() {
        return DbUtils.getRelationFrom(this, RelationType.AGGREGATION);
    }

    public void aggregatedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.AGGREGATION);
    }

    public Set<Type> getComposedTo() {
        return DbUtils.getRelationFrom(this, RelationType.COMPOSITION);
    }

    public void composedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.COMPOSITION);
    }


    public Set<Type> getDependencyTo() {
        return DbUtils.getRelationFrom(this, RelationType.DEPENDENCY);
    }

    public void dependencyTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.DEPENDENCY);
    }

    public Set<Type> getUseTo() {
        return DbUtils.getRelationFrom(this, RelationType.USE);
    }

    public boolean hasUseTo(Type type) {
        return getUseTo().contains(type);
    }

    public void useTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.USE);
    }

    public Set<Type> getRealizedBy() {
        return DbUtils.getRelationTo(this, RelationType.REALIZATION);
    }

    public void realizedBy(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getGeneralizedBy() {
        return DbUtils.getRelationTo(this, RelationType.GENERALIZATION);
    }

    public boolean isGeneralizedBy(Type type) {
        return getGeneralizedBy().contains(type);
    }

    public void generalizedBy(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public void removeGeneralizedBy(Type other) {
        deleteRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public void removeRealizes(Type other) {
        deleteRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getContainedBy() {
        return DbUtils.getRelationTo(this, RelationType.CONTAINMENT);
    }

    public void containedBy(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.CONTAINMENT);
    }

    public Set<Type> getAssociatedFrom() {
        return DbUtils.getRelationTo(this, RelationType.ASSOCIATION);
    }

    public void associatedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public Set<Type> getAggregatedFrom() {
        return DbUtils.getRelationTo(this, RelationType.AGGREGATION);
    }

    public void aggregatedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.AGGREGATION);
    }

    public Set<Type> getComposedFrom() {
        return DbUtils.getRelationTo(this, RelationType.COMPOSITION);
    }

    public void composedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.COMPOSITION);
    }

    public Set<Type> getDependencyFrom() {
        return DbUtils.getRelationTo(this, RelationType.DEPENDENCY);
    }

    public void dependencyFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.DEPENDENCY);
    }

    public Set<Type> getUseFrom() {
        return DbUtils.getRelationTo(this, RelationType.USE);
    }

    public void useFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.USE);
    }

    public List<Type> getParentTypes() {
        List<Type> parents = Lists.newArrayList();
        parents.addAll(getGeneralizes());
        parents.addAll(getRealizes());

        return parents;
    }

    public List<Type> getAncestorTypes() {
        List<Type> ancestors = Lists.newArrayList();
        Queue<Type> queue = Lists.newLinkedList();

        queue.offer(this);
        while (!queue.isEmpty()) {
            Type t = queue.poll();
            if (!t.equals(this)) {
                if (!ancestors.contains(t))
                    ancestors.add(t);
            }

            queue.addAll(t.getParentTypes());
        }

        return ancestors;
    }

    public List<Type> getChildTypes() {
        List<Type> children = Lists.newArrayList();
        children.addAll(getGeneralizedBy());
        children.addAll(getRealizedBy());

        return children;
    }

    public List<Type> getDescendentTypes() {
        List<Type> descendants = Lists.newArrayList();
        Queue<Type> queue = Lists.newLinkedList();

        queue.offer(this);
        while (!queue.isEmpty()) {
            Type t = queue.poll();
            if (!t.equals(this)) {
                if (!descendants.contains(t))
                    descendants.add(t);
            }

            queue.addAll(t.getChildTypes());
        }

        return descendants;
    }

    public List<Type> getContainedTypes() {
        List<Type> children = Lists.newArrayList();
        children.addAll(getContained());

        return children;
    }

    public List<Method> findOverridingMethods(List<Method> given) {
        List<Method> overriding = Lists.newArrayList();

        for (Method give : given) {
            for (Method curr : getMethods()) {
                if (curr.signature().equals(give.signature())) {
                    overriding.add(curr);
                }
            }
        }

        return overriding;
    }

    public List<Method> findOverridingMethods() {
        List<Method> overriding = Lists.newArrayList();
        List<Method> currMethods = Lists.newArrayList(getMethods());
        List<Type> ancestors = getAncestorTypes();

        outer:
        for (Type anc : ancestors) {
            if (anc instanceof Class) {
                for (Method ancMethod : anc.getMethods()) {
                    List<Method> toRemove = Lists.newArrayList();
                    for (Method currMethod : getMethods()) {
                        if (currMethod.signature().equals(ancMethod.signature())) {
                            overriding.add(currMethod);
                            toRemove.add(currMethod);
                        }
                    }
                    currMethods.removeAll(toRemove);
                    if (currMethods.isEmpty())
                        break outer;
                }
            }
        }

        return overriding;
    }

    public List<TemplateParam> getTemplateParams() {
        return getAll(TemplateParam.class);
    }

    public void setTemplateParams(List<TemplateParam> params) {
        if (params == null || params.isEmpty())
            return;

        for (TemplateParam param : params)
            add(param);
        save();
    }

    public void addTemplateParam(TemplateParam param) {
        if (param != null) {
            add(param);
            save();
        }
    }

    public void removeTemplateParam(TemplateParam param) {
        if (param != null) {
            remove(param);
            save();
        }
    }

    public Method findMethodBySignature(String sig) {
        for (Method m : getMethods()) {
            if (m.signature().equals(sig))
                return m;
        }

        return null;
    }

    public boolean hasMethodWithMatchingSignature(Method method) {
        for (Method m : getMethods()) {
            if (m.signature().equals(method.signature()))
                return true;
        }

        return false;
    }

    public String getFullName() {
        List<Namespace> parentNs = getParentNamespaces();
        if (parentNs != null && !parentNs.isEmpty()) {
            return parentNs.get(0).getFullName() + "." + this.getName();
        }

        return this.getName();
    }

    public List<Type> getAllTypes() {
        return Lists.newArrayList();
    }

    public List<Class> getClasses() {
        return Lists.newArrayList();
    }

    public List<Enum> getEnums() {
        return Lists.newArrayList();
    }

    public List<Interface> getInterfaces() {
        return Lists.newArrayList();
    }

    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        members.addAll(getLiterals());
        members.addAll(getInitializers());
        members.addAll(getAllTypedMembers());
        return members;
    }

    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        members.addAll(getFields());
        members.addAll(getAllMethods());
        return members;
    }

    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        methods.addAll(getMethods());
        methods.addAll(getConstructors());
        return methods;
    }

    public void updateKey() {
        File parent = parent(File.class);
        String newKey;
        if (parent != null)
            newKey = parent.getFileKey() + ":" + getName();
        else
            newKey = getName();

        setString("compKey", newKey);
        save();

        getAllMembers().forEach(Member::updateKey);
    }

    public TypeRef createTypeRef() {
        if (TypeRef.findFirst("typeFullName = ?", getFullName()) != null) {
            return TypeRef.findFirst("typeFullName = ?", getFullName());
        } else {
            Reference ref = createReference();
            return TypeRef.builder().typeName(getName()).typeFullName(getFullName()).type(TypeRefType.Type).ref(ref).create();
        }
    }

    public Reference createReference() {
        return Reference.builder().refKey(getCompKey()).refType(RefType.TYPE).create();
    }

    public Type copy(String oldPrefix, String newPrefix) {
        Type copy = copyType();

        getModifiers().forEach(copy::addModifier);
        getAllMembers().forEach(member -> copy.addMember(member.copy(oldPrefix, newPrefix)));
//        getTemplateParams().forEach(param -> copy.addTemplateParam(param.copy()));
//        getChildTypes().forEach(type -> copy.add(type.copy()));

        return copy;
    }

    protected abstract Type copyType();
}
