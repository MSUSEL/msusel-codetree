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
import org.javalite.activejdbc.annotations.HasMany;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsTo(parent = Project.class, foreignKeyName = "project_id")
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
        if (file != null)
            file.setParentNSID(getId());
    }

    public void removeFile(File file) {
        if (file != null && file.getParentNSID() != null && file.getParentNSID().equals(getId()))
            file.setParentNSID(null);
    }

    public List<File> getFiles() {
        return File.find("parent_ns_id = ?", getId());
    }

    public void addNamespace(Namespace ns) {
        if (ns != null)
            ns.setParentNSID(getId());
    }

    public void removeNamespace(Namespace ns) {
        if (ns != null && ns.getParentNSID() != null && ns.getParentNSID().equals(getId()))
            ns.setParentNSID(null);
    }

    public void setParentNSID(Object id) {
        set("parent_ns_id", id);
        save();
    }

    public Object getParentNSID() {
        return get("parent_ns_id");
    }

    public List<Namespace> getNamespaces() {
        return Namespace.find("parent_ns_id = ?", getId());
    }

    public boolean containsNamespace(Namespace ns) {
        return ns != null && ns.getParentNSID() != null && ns.getParentNSID().equals(getId());
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getFiles().forEach(f -> imports.addAll(f.getImports()));
        getNamespaces().forEach(ns -> imports.addAll(ns.getImports()));
        return imports;
    }

    public void addType(Type type) {
        if (type != null) {
            add(type);
        }
        save();
    }

    public void removeType(Type type) {
        if (type != null)
            remove(type);
        save();
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        types.addAll(getClasses());
        types.addAll(getInterfaces());
        types.addAll(getEnums());
        return types;
    }

    @Override
    public List<Class> getClasses() {
        List<Class> classes = Lists.newArrayList(getAll(Class.class));
        getNamespaces().forEach(ns -> classes.addAll(ns.getClasses()));
        return classes;
    }

    @Override
    public List<Interface> getInterfaces() {
        List<Interface> interfaces = Lists.newArrayList(getAll(Interface.class));
        getNamespaces().forEach(ns -> interfaces.addAll(ns.getInterfaces()));
        return interfaces;
    }

    @Override
    public List<Enum> getEnums() {
        List<Enum> enums = Lists.newArrayList(getAll(Enum.class));
        getNamespaces().forEach(ns -> enums.addAll(ns.getEnums()));
        return enums;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getAllTypes().forEach(f -> members.addAll(f.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getEnums().forEach(enm -> literals.addAll(enm.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newArrayList();
        getAllTypes().forEach(f -> initializers.addAll(f.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getAllTypes().forEach(f -> members.addAll(f.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getAllTypes().forEach(f -> fields.addAll(f.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(f -> methods.addAll(f.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(f -> methods.addAll(f.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getAllTypes().forEach(f -> constructors.addAll(f.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getAllTypes().forEach(f -> destructors.addAll(f.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        if (getParentProject() != null)
            getParentProject().getParentSystems();
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        if (getParentProject() != null)
            return getParentProject().getParentSystem();
        return null;
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        if (getParentProject() != null)
            projects.add(getParentProject());

        return projects;
    }

    @Override
    public Project getParentProject() {
        return parent(Project.class);
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
        if (getParentNSID() != null)
            return Namespace.findById(getParentNSID());
        return null;
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        Namespace ns = getParentNamespace();
        if (ns == null)
            return getParentProject();
        else
            return ns;
    }

    public Object getParentModuleID() {
        return get("parent_mod_id");
    }

    public void setParentModuleID(Object id) {
        set("parent_mod_id", id);
    }

    public List<Module> getParentModules() {
        List<Module> modules = Lists.newLinkedList();
        Module mod = getParentModule();
        if (mod != null)
            modules.add(mod);
        return modules;
    }

    public Module getParentModule() {
        if (getParentModuleID() != null)
            return Module.findById(getParentModuleID());
        return null;
    }

    @Override
    public String getRefKey() {
        return getString("nsKey");
    }

    public void updateKey() {
        Project proj = parent(Project.class);
        String parentKey = null;
        String newKey;

        if (proj != null)
            parentKey = proj.getProjectKey();

        if (parentKey != null) {
            newKey = parentKey + ":" + getFullName();
        } else {
            newKey = getFullName();
        }

        setString("nsKey", newKey);
        save();
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        setString("relPath", path);
        save();
    }

    public String getFullPath(FileType type, int index) {  // FIXME
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
        for(File f : getFiles()) {
            Type t = f.findInterface(attribute, value);
            if (t != null)
                return t;
        }

        for (Namespace ns : getNamespaces()) {
            Type t = ns.findInterface(attribute, value);
            if (t != null)
                return t;
        }

        return null;
    }

    public Type findClass(String attribute, String value) {
        for(File f : getFiles()) {
            Type t = f.findClass(attribute, value);
            if (t != null)
                return t;
        }

        for (Namespace ns : getNamespaces()) {
            Type t = ns.findClass(attribute, value);
            if (t != null)
                return t;
        }

        return null;
    }

    public Type findEnum(String attribute, String value) {
        for(File f : getFiles()) {
            Type t = f.findEnum(attribute, value);
            if (t != null)
                return t;
        }

        for (Namespace ns : getNamespaces()) {
            Type t = ns.findEnum(attribute, value);
            if (t != null)
                return t;
        }

        return null;
    }

    public Namespace copy(String oldPrefix, String newPrefix) {
        Namespace copy = Namespace.builder()
                .name(this.getName())
                .nsKey(this.getName())
                .relPath(this.getRelPath())
                .create();

        getAllTypes().forEach(type -> copy.addType(type.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public Reference createReference() {
        return Reference.builder().refKey(getNsKey()).refType(RefType.NAMESPACE).create();
    }

    /**
     * @return The parent file of this Measurable
     */
    @Override
    public File getParentFile() {
        return null;
    }
}
