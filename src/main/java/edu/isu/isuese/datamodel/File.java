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
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class File extends Model implements Measurable, ComponentContainer {

    public String getFileKey() {
        return getString("fileKey");
    }

    public File() {

    }

    @Builder(buildMethodName = "create")
    public File(String fileKey, String name, FileType type, String relPath, int start, int end) {
        set("fileKey", fileKey, "name", name);
        setType(type);
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        setStart(start);
        setEnd(end);
        save();
    }

    public void setName(String name) {
        setRelPath(getRelPath().replace(getName(), name));
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public FileType getType() {
        if (get("type") == null)
            return null;
        else
            return FileType.fromValue(getInteger("type"));
    }

    public void setType(FileType type) {
        if (type == null)
            set("type", null);
        else
            set("type", type.value());
        save();
    }

    public void addImport(Import imp) {
        add(imp);
        save();
    }

    public void removeImport(Import imp) {
        remove(imp);
        save();
    }

    public List<Import> getImports() {
        return getAll(Import.class);
    }

    public void addType(Type t) {
        add(t);
        save();
    }

    public void removeType(Type t) {
        remove(t);
        save();
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        types.addAll(getAll(Class.class));
        types.addAll(getAll(Interface.class));
        types.addAll(getAll(Enum.class));
        return types;
    }

    @Override
    public List<Class> getClasses() {
        return getAll(Class.class);
    }

    @Override
    public List<Interface> getInterfaces() {
        return getAll(Interface.class);
    }

    @Override
    public List<Enum> getEnums() {
        return getAll(Enum.class);
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newLinkedList();
        getAllTypes().forEach(type -> members.addAll(type.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newLinkedList();
        getAllTypes().forEach(type -> literals.addAll(type.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newLinkedList();
        getAllTypes().forEach(type -> initializers.addAll(type.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newLinkedList();
        getAllTypes().forEach(type -> {
            members.addAll(type.getMethods());
            members.addAll(type.getFields());
        });
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newLinkedList();
        getAllTypes().forEach(type -> fields.addAll(type.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newLinkedList();
        getAllTypes().forEach(type -> methods.addAll(type.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newLinkedList();
        getAllTypes().forEach(type -> methods.addAll(type.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newLinkedList();
        getAllTypes().forEach(type -> constructors.addAll(type.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newLinkedList();
        getAllTypes().forEach(type -> destructors.addAll(type.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        Namespace parent = getParentNamespace();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getParentSystem();
        return null;
    }

    public List<Project> getParentProjects() {
        List<Namespace> parents = getParentNamespaces();
        List<Project> projects = Lists.newLinkedList();
        parents.forEach(ns -> {
            projects.addAll(ns.getParentProjects());
        });
        return projects;
    }

    public Project getParentProject() {
        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getParentProject();
        return null;
    }

    public List<Module> getParentModules() {
        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getParentModules();
        return Lists.newArrayList();
    }

    public Module getParentModule() {
        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getParentModule();
        return null;
    }

    public List<Namespace> getParentNamespaces() {
        List<Namespace> namespaces = Lists.newLinkedList();
        if (getParentNamespace() != null)
            namespaces.add(getParentNamespace());
        return namespaces;
    }

    public Namespace getParentNamespace() {
        return parent(Namespace.class);
    }

    @Override
    public String getRefKey() {
        return getString("fileKey");
    }

    public void updateKey() {
        Namespace parent = getParentNamespace();
        String newKey;
        if (parent != null)
            newKey = parent.getNsKey() + ":" + getName();
        else
            newKey = getName();

        setString("fileKey", newKey);
        save();

        getAllTypes().forEach(Type::updateKey);
    }

    public void setStart(int start) {
        setInteger("start", start);
        save();
    }

    public int getStart() {
        return getInteger("start");
    }

    public void setEnd(int end) {
        setInteger("end", end);
        save();
    }

    public int getEnd() {
        return getInteger("end");
    }

    public String getRelPath() { return getString("relPath"); }

    public void setRelPath(String path) { setString("relPath", path); save(); }

    public String getFullPath() {
        return parent(Namespace.class).getFullPath(getType()) + getRelPath();
    }

    public List<Object> containing(int line) {
        List<Object> containing = Lists.newArrayList();
        getImports().forEach(anImport -> {
            if (anImport.getStart() <= line && anImport.getEnd() >= line) {
                containing.add(anImport);
            }
        });
        getAllTypes().forEach(aType -> {
            if (aType.getStart() <= line && aType.getEnd() >= line) {
                containing.add(aType);
            }

            aType.getAllMembers().forEach(member -> {
                if (member.getStart() <= line && member.getEnd() >= line)
                    containing.add(member);
            });
        });

        return containing;
    }

    public List<Object> following(int line) {
        List<Object> following = Lists.newArrayList();
        getImports().forEach(anImport -> {
            if (anImport.getStart() > line)
                following.add(anImport);
        });

        getAllTypes().forEach(aType -> {
            if (aType.getStart() >= line)
                following.add(aType);
            aType.getAllMembers().forEach(member -> {
                if (member.getStart() >= line)
                    following.add(member);
            });
        });

        return following;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof File) {
            File comp = (File) o;
            return comp.getFileKey().equals(getFileKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileKey());
    }

    public File copy(String oldPrefix, String newPrefix) {
        File copy = File.builder()
                .name(this.getName())
                .fileKey(this.getName())
                .relPath(this.getRelPath())
                .type(this.getType())
                .start(this.getStart())
                .end(this.getEnd())
                .create();

        getAllTypes().forEach(type -> copy.addType(type.copy(oldPrefix, newPrefix)));

        return copy;
    }
}
