/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToParents({
        @BelongsTo(foreignKeyName="module_id", parent=Module.class),
        @BelongsTo(foreignKeyName="namespace_id", parent=Namespace.class)
})
public class Namespace extends Model implements Measurable {

    public String getNsKey() { return getString("nsKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addFile(File file) { add(file); save(); }

    public void removeFile(File file) { remove(file); save(); }

    public void addNamespace(Namespace ns) { add(ns); save(); }

    public void removeNamespace(Namespace ns) { remove(ns); save(); }

    public List<Namespace> getNamespaces() { return getAll(Namespace.class); }

    public boolean containsNamespace(Namespace ns) {
        return getNamespaces().contains(ns);
    }

    public List<Import> getImports() {
        return DbUtils.getImports(this.getClass(), (Integer) getId());
    }

    public List<Type> getTypes() {
        return DbUtils.getTypes(this.getClass(), (Integer) getId());
    }

    public List<Class> getClasses() {
        return DbUtils.getClasses(this.getClass(), (Integer) getId());
    }

    public List<Interface> getInterfaces() {
        return DbUtils.getInterfaces(this.getClass(), (Integer) getId());
    }

    public List<Enum> getEnums() {
        return DbUtils.getEnums(this.getClass(), (Integer) getId());
    }

    public List<Member> getMembers() {
        return DbUtils.getMembers(this.getClass(), (Integer) getId());
    }

    public List<Literal> getLiterals() {
        return DbUtils.getLiterals(this.getClass(), (Integer) getId());
    }

    public List<Initializer> getInitializers() {
        return DbUtils.getInitializers(this.getClass(), (Integer) getId());
    }

    public List<TypedMember> getTypedMembers() {
        return DbUtils.getTypedMembers(this.getClass(), (Integer) getId());
    }

    public List<Field> getFields() {
        return DbUtils.getFields(this.getClass(), (Integer) getId());
    }

    public List<Method> getAllMethods() {
        return DbUtils.getAllMethods(this.getClass(), (Integer) getId());
    }

    public List<Method> getMethods() {
        return DbUtils.getMethods(this.getClass(), (Integer) getId());
    }

    public List<Constructor> getConstructors() {
        return DbUtils.getConstructors(this.getClass(), (Integer) getId());
    }

    public List<Destructor> getDestructors() {
        return DbUtils.getDestructors(this.getClass(), (Integer) getId());
    }

    public List<System> getParentSystems() {
        return DbUtils.getParentSystem(this.getClass(), (Integer) getId());
    }

    public List<Project> getParentProjects() {
        return DbUtils.getParentProject(this.getClass(), (Integer) getId());
    }

    public List<Module> getParentModules() {
        List<Module> modules = Lists.newLinkedList();
        modules.add(parent(Module.class));
        return modules;
    }

    @Override
    public String getRefKey() {
        return getString("nsKey");
    }
}
