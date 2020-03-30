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
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Module extends Model implements Measurable, ComponentContainer {

    public Module() {
    }

    @Builder(buildMethodName = "create")
    public Module(String name, String moduleKey, String relPath, String srcPath, String testPath) {
        set("moduleKey", moduleKey);
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        if (srcPath != null && !srcPath.isEmpty())
            setSrcPath(srcPath);
        if (testPath != null && !testPath.isEmpty())
            setTestPath(testPath);
        setName(name);
        save();
    }

    public String getModuleKey() {
        return getString("moduleKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
        save();
        setRelPath(name);
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

    public List<File> getFiles() {
        return DbUtils.getFiles(this.getClass(), (Integer) getId());
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
        projects.add(parent(Project.class));
        return projects;
    }

    @Override
    public String getRefKey() {
        return getString("moduleKey");
    }

    public void updateKey() {
        Project parent = parent(Project.class);
        String newKey;
        if (parent != null)
            newKey = parent.getProjectKey() + ":" + getName();
        else
            newKey = getName();

        setString("moduleKey", newKey);
        save();

        getNamespaces().forEach(Namespace::updateKey);
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        if (path == null || path.isEmpty()) return;
        setString("relPath", path);
        save();
    }

    public String getSrcPath() {
        return getString("srcPath");
    }

    public void setSrcPath(String path) {
        setString("srcPath", path);
        save();
    }

    public String getTestPath() {
        return getString("testPath");
    }

    public void setTestPath(String path) {
        setString("testPath", path);
        save();
    }

    public String getFullPath() {
        return parent(Project.class).getFullPath() + getRelPath() + java.io.File.separator;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Module) {
            Module comp = (Module) o;
            return comp.getModuleKey().equals(this.getModuleKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModuleKey());
    }
}
