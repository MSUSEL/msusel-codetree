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

import java.util.List;

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
    public File(String fileKey, String name, FileType type) {
        set("fileKey", fileKey, "name", name);
        setType(type);
        save();
    }

    public void setName(String name) {
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
        List<TypedMember> members = Lists.newLinkedList();
        List<Type> types = getAllTypes();
        types.forEach(type -> {
            members.addAll(type.getMethods());
            members.addAll(type.getFields());
        });

//        return DbUtils.getTypedMembers(this.getClass(), (Integer) getId());
        return members;
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
        List<Namespace> parents = getParentNamespaces();
        List<Project> projects = Lists.newLinkedList();
        parents.forEach(ns -> {
            projects.addAll(ns.getParentProjects());
        });

//        return DbUtils.getParentProject(this.getClass(), (Integer) getId());
        return projects;
    }

    public List<Module> getParentModules() {
        return DbUtils.getParentModule(this.getClass(), (Integer) getId());
    }

    public List<Namespace> getParentNamespaces() {
        List<Namespace> namespaces = Lists.newLinkedList();
        namespaces.add(parent(Namespace.class));
        return namespaces;
    }

    @Override
    public String getRefKey() {
        return getString("fileKey");
    }
}
