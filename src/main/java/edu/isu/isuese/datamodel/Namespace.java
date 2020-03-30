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
import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.util.List;
import java.util.Objects;

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
        return DbUtils.getImports(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Type> getAllTypes() {
        return DbUtils.getTypes(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Class> getClasses() {
        return DbUtils.getClasses(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Interface> getInterfaces() {
        return DbUtils.getInterfaces(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Enum> getEnums() {
        return DbUtils.getEnums(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Member> getAllMembers() {
        return DbUtils.getMembers(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Literal> getLiterals() {
        return DbUtils.getLiterals(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Initializer> getInitializers() {
        return DbUtils.getInitializers(this.getClass(), (Integer) getId());
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        return DbUtils.getTypedMembers(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Field> getFields() {
        return DbUtils.getFields(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Method> getAllMethods() {
        return DbUtils.getAllMethods(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Method> getMethods() {
        return DbUtils.getMethods(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Constructor> getConstructors() {
        return DbUtils.getConstructors(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Destructor> getDestructors() {
        return DbUtils.getDestructors(this.getClass(), (Integer) getId());
    }

    public List<System> getParentSystems() {
        return DbUtils.getParentSystem(this.getClass(), (Integer) getId());
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        if (getParentNamespace() != null) {
            projects.addAll(getParentNamespace().getParentProjects());
        }
        getParentModules().forEach(mod -> projects.addAll(mod.getParentProjects()));

        return projects;
//        return DbUtils.getParentProject(this.getClass(), (Integer) getId());
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

    public List<Module> getParentModules() {
        List<Module> modules = Lists.newLinkedList();
        Module mod = parent(Module.class);
        if (mod != null)
            modules.add(parent(Module.class));
        return modules;
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

    public String getFullPath(FileType type) {
        String path = "";
        if (parent(Namespace.class) != null) {
            path = parent(Namespace.class).getFullPath(type);
        } else if (parent(Module.class) != null) {
            path = parent(Module.class).getFullPath();
            if (type == FileType.SOURCE)
                path += parent(Module.class).getSrcPath();
            else if (type == FileType.TEST)
                path += parent(Module.class).getTestPath();
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
}
