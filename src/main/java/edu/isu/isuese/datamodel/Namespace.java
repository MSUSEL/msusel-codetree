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
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToParents({
        @BelongsTo(foreignKeyName = "module_id", parent = Module.class),
        @BelongsTo(foreignKeyName = "namespace_id", parent = Namespace.class)
})
public class Namespace extends Model implements Measurable, ComponentContainer {

    public Namespace() {
    }

    @Builder(buildMethodName = "create")
    public Namespace(String nsKey, String name, String relPath) {
        set("nsKey", nsKey);
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        setName(name);
        saveIt();
    }

    public String getNsKey() {
        return getString("nsKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setRelPath(name);
        set("name", name);
        save();
    }

    public void addFile(File file) {
        add(file);
        save();
    }

    public void removeFile(File file) {
        remove(file);
        save();
    }

    public List<File> getFiles() {
        return getAll(File.class);
    }

    public void addNamespace(Namespace ns) {
        add(ns);
        save();
    }

    public void removeNamespace(Namespace ns) {
        remove(ns);
        save();
    }

    public List<Namespace> getNamespaces() {
        return getAll(Namespace.class);
    }

    public boolean containsNamespace(Namespace ns) {
        return getNamespaces().contains(ns);
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getFiles().forEach(f -> imports.addAll(f.getImports()));
        getNamespaces().forEach(ns -> imports.addAll(ns.getImports()));
        return imports;
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        getFiles().forEach(f -> types.addAll(f.getAllTypes()));
        getNamespaces().forEach(ns -> types.addAll(ns.getAllTypes()));
        return types;
    }

    @Override
    public List<Class> getClasses() {
        List<Class> classes = Lists.newArrayList();
        getFiles().forEach(f -> classes.addAll(f.getClasses()));
        getNamespaces().forEach(ns -> classes.addAll(ns.getClasses()));
        return classes;
    }

    @Override
    public List<Interface> getInterfaces() {
        List<Interface> interfaces = Lists.newArrayList();
        getFiles().forEach(f -> interfaces.addAll(f.getInterfaces()));
        getNamespaces().forEach(ns -> interfaces.addAll(ns.getInterfaces()));
        return interfaces;
    }

    @Override
    public List<Enum> getEnums() {
        List<Enum> enums = Lists.newArrayList();
        getFiles().forEach(f -> enums.addAll(f.getEnums()));
        getNamespaces().forEach(ns -> enums.addAll(ns.getEnums()));
        return enums;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getFiles().forEach(f -> members.addAll(f.getAllMembers()));
        getNamespaces().forEach(ns -> members.addAll(ns.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getFiles().forEach(f -> literals.addAll(f.getLiterals()));
        getNamespaces().forEach(ns -> literals.addAll(ns.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newArrayList();
        getFiles().forEach(f -> initializers.addAll(f.getInitializers()));
        getNamespaces().forEach(ns -> initializers.addAll(ns.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getFiles().forEach(f -> members.addAll(f.getAllTypedMembers()));
        getNamespaces().forEach(ns -> members.addAll(ns.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getFiles().forEach(f -> fields.addAll(f.getFields()));
        getNamespaces().forEach(ns -> fields.addAll(ns.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getFiles().forEach(f -> methods.addAll(f.getAllMethods()));
        getNamespaces().forEach(ns -> methods.addAll(ns.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getFiles().forEach(f -> methods.addAll(f.getMethods()));
        getNamespaces().forEach(ns -> methods.addAll(ns.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getFiles().forEach(f -> constructors.addAll(f.getConstructors()));
        getNamespaces().forEach(ns -> constructors.addAll(ns.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getFiles().forEach(f -> destructors.addAll(f.getDestructors()));
        getNamespaces().forEach(ns -> destructors.addAll(ns.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        Namespace ns = getParentNamespace();
        Module mod = getParentModule();
        if (ns != null) {
            return ns.getParentSystems();
        } else if (mod != null) {
            return mod.getParentSystems();
        }
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        Namespace ns = getParentNamespace();
        Module mod = getParentModule();
        if (ns != null) {
            return ns.getParentSystem();
        } else if (mod != null) {
            return mod.getParentSystem();
        }
        return null;
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        if (getParentNamespace() != null) {
            projects.add(getParentNamespace().getParentProject());
        } else if (getParentModule() != null) {
            projects.add(getParentModule().getParentProject());
        }

        return projects;
    }

    @Override
    public Project getParentProject() {
        Namespace ns = getParentNamespace();
        Module mod = getParentModule();
        if (ns != null) {
            return ns.getParentProject();
        } else if (mod != null) {
            return mod.getParentProject();
        }
        return null;
    }

    public String getFullName() {
        Namespace parent = getParentNamespace();
        if (parent != null) {
            return parent.getFullName() + "." + this.getName();
        } else {
            return this.getName();
        }
    }

    public Namespace getParentNamespace() {
        return parent(Namespace.class);
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        Namespace ns = getParentNamespace();
        if (ns == null)
            return getParentModule();
        else
            return ns;
    }

    public List<Module> getParentModules() {
        List<Module> modules = Lists.newLinkedList();
        Module mod = parent(Module.class);
        if (mod != null)
            modules.add(parent(Module.class));
        return modules;
    }

    public Module getParentModule() {
        return parent(Module.class);
    }

    @Override
    public String getRefKey() {
        return getString("nsKey");
    }

    public void updateKey() {
        Namespace pns = parent(Namespace.class);
        Module pmod = parent(Module.class);
        String parentKey = null;
        String newKey;
        if (pns != null)
            parentKey = pns.getNsKey();
        else if (pmod != null)
            parentKey = pmod.getModuleKey();

        if (parentKey != null) {
            newKey = parentKey + ":" + getName();
        } else {
            newKey = getName();
        }

        setString("nsKey", newKey);
        save();

        getNamespaces().forEach(Namespace::updateKey);
        getFiles().forEach(File::updateKey);
    }

    public String getRelPath() { return getString("relPath"); }

    public void setRelPath(String path) { setString("relPath", path); save(); }

    public String getFullPath(FileType type, int index) {
        String path = "";
        if (parent(Namespace.class) != null) {
            path = parent(Namespace.class).getFullPath(type, index);
        } else if (parent(Module.class) != null) {
            path = parent(Module.class).getFullPath();
            if (type == FileType.SOURCE)
                path += parent(Module.class).getSrcPath(index);
            else if (type == FileType.TEST)
                path += parent(Module.class).getTestPath(index);
        }

        if (!path.endsWith(java.io.File.separator))
            path += java.io.File.separator;

        return path + getRelPath() + java.io.File.separator;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Namespace) {
            Namespace comp = (Namespace) o;
            return comp.getNsKey().equals(this.getNsKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNsKey());
    }

    public Type findInterface(String attribute, String value) {
        AtomicReference<Type> type = new AtomicReference<>();
        getFiles().forEach(f -> {
            f.getInterfaces().forEach(t -> {
                if (t.getString(attribute).equals(value))
                    type.set(t);
            });
        });

        if (type.get() != null)
            return type.get();

        getNamespaces().forEach(ns -> {
            Type t = ns.findInterface(attribute, value);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public Type findClass(String attribute, String value) {
        AtomicReference<Type> type = new AtomicReference<>();
        getFiles().forEach(f -> {
            f.getClasses().forEach(t -> {
                if (t.getString(attribute).equals(value))
                    type.set(t);
            });
        });

        if (type.get() != null)
            return type.get();

        getNamespaces().forEach(ns -> {
            Type t = ns.findClass(attribute, value);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public Type findEnum(String attribute, String value) {
        AtomicReference<Type> type = new AtomicReference<>();
        getFiles().forEach(f -> {
            f.getEnums().forEach(t -> {
                if (t.getString(attribute).equals(value))
                    type.set(t);
            });
        });

        if (type.get() != null)
            return type.get();

        getNamespaces().forEach(ns -> {
            Type t = ns.findEnum(attribute, value);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public Namespace copy(String oldPrefix, String newPrefix) {
        Namespace copy = Namespace.builder()
                .name(this.getName())
                .nsKey(this.getName())
                .relPath(this.getRelPath())
                .create();

        getNamespaces().forEach(ns -> copy.addNamespace(ns.copy(oldPrefix, newPrefix)));
        getFiles().forEach(file -> copy.addFile(file.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public Reference createReference() {
        return Reference.builder().refKey(getNsKey()).refType(RefType.NAMESPACE).create();
    }
}
