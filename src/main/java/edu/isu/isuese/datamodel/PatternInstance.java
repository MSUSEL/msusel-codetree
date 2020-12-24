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
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PatternInstance extends Model implements Measurable, ComponentContainer {

    public PatternInstance() {}

    @Builder(buildMethodName = "create")
    public PatternInstance(String instKey) {
        set("instKey", instKey);
        saveIt();
    }

    public String getInstKey() { return getString("instKey"); }

    public void addRoleBinding(RoleBinding binding) { add(binding); save(); }

    public List<RoleBinding> getRoleBindings() { return getAll(RoleBinding.class); }

    public void removeRoleBinding(RoleBinding binding) { remove(binding); save(); }

    public List<Role> getRoles() {
        Pattern parent = getParentPattern();
        if (parent != null)
            return parent.getRoles();
        return Lists.newArrayList();
    }

    public Pattern getParentPattern() {
        return parent(Pattern.class);
    }

    public List<System> getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        projects.add(getParentProject());
        return projects;
    }

    @Override
    public Project getParentProject() {
        return parent(Project.class);
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        return getParentProject();
    }

    public List<PatternChain> getParentPatternChain() {
        List<PatternChain> chains = Lists.newLinkedList();
        chains.add(parent(PatternChain.class));
        return chains;
    }

    @Override
    public String getRefKey() {
        return getString("instKey");
    }

    public List<Type> getTypes() {
        List<Type> types = Lists.newArrayList();
        List<RoleBinding> bindings = getRoleBindings();

        bindings.forEach(binding -> {
            Reference ref = binding.getReference();
            Component t = binding.getReference().getReferencedComponent(getParentProjects().get(0));
            if (t instanceof Type)
                types.add((Type) t);

//            if (getParentProjects().size() > 0) {
//                Type t = getParentProjects().get(0).findType("compKey", ref.getRefKey());
//                if (t != null)
//                    types.add(t);
//            }
        });

        return types;
    }

    public List<Type> getTypesBoundTo(Role role) {
        List<Type> types = Lists.newArrayList();
        List<RoleBinding> bindings = getRoleBindings();
        bindings.forEach(binding -> {
            Reference ref = binding.getReference();
            if (binding.getRole().equals(role)) {
                if (getParentProjects().size() > 0) {
                    Type t = getParentProjects().get(0).findType("compKey", ref.getRefKey());
                    if (t != null)
                        types.add(t);
                }
            }
        });
        return types;
    }

    public Role getRoleBoundTo(Type type) {
        Role role = null;
        List<RoleBinding> bindings = getRoleBindings();
        for (RoleBinding binding : bindings) {
            Reference ref = binding.getReference();
            if (ref.equals(Reference.to(type))) {
                role = binding.getRole();
                break;
            }
        }

        return role;
    }

    public List<Type> getAllTypes() { return getTypes(); }

    public List<Class> getClasses() {
        List<Class> classes = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t instanceof Class)
                classes.add((Class) t);
        });
        return classes;
    }

    public List<Enum> getEnums() {
        List<Enum> enums = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t instanceof Enum)
                enums.add((Enum) t);
        });
        return enums;
    }

    public List<Interface> getInterfaces() {
        List<Interface> interfaces = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t instanceof Interface)
                interfaces.add((Interface) t);
        });
        return interfaces;
    }

    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getAllTypes().forEach(t -> members.addAll(t.getAllMembers()));
        return members;
    }

    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getAllTypes().forEach(t -> literals.addAll(t.getLiterals()));
        return literals;
    }

    public List<Initializer> getInitializers() {
        List<Initializer> inits = Lists.newArrayList();
        getAllTypes().forEach(t -> inits.addAll(t.getInitializers()));
        return inits;
    }

    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getAllTypes().forEach(t -> members.addAll(t.getAllTypedMembers()));
        return members;
    }

    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getAllTypes().forEach(t -> fields.addAll(t.getFields()));
        return fields;
    }

    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(t -> methods.addAll(t.getAllMethods()));
        return methods;
    }

    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(t -> methods.addAll(t.getMethods()));
        return methods;
    }

    public List<Constructor> getConstructors() {
        List<Constructor> cons = Lists.newArrayList();
        getAllTypes().forEach(t -> cons.addAll(t.getConstructors()));
        return cons;
    }

    public List<Destructor> getDestructors() {
        List<Destructor> dests = Lists.newArrayList();
        getAllTypes().forEach(t -> dests.addAll(t.getDestructors()));
        return dests;
    }

    public String getFamily() {
        Pattern parent = getParentPattern();
        if (parent != null) {
            return parent.getFamily();
        } else
            return null;
    }

    public PatternInstance copy(String oldPrefix, String newPrefix) {
        PatternInstance copy = PatternInstance.builder().instKey(this.getInstKey().replace(oldPrefix, newPrefix)).create();

        getRoleBindings().forEach(bind -> copy.addRoleBinding(bind.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public List<RoleBinding> bindingsFor(Role r) {
        List<RoleBinding> bindings = Lists.newArrayList();
        getRoleBindings().forEach(rb -> {
           if (rb.getRole().equals(r))
               bindings.add(rb);
        });

        return bindings;
    }

    public Role findRole(String name) {
        for (Role r : getRoles()) {
            if (r.getName().equals(name))
                return r;
        }

        return null;
    }

    /**
     * @return The parent file of this Measurable
     */
    @Override
    public File getParentFile() {
        return null;
    }
}
