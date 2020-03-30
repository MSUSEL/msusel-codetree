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
import com.google.common.collect.Queues;
import edu.isu.isuese.datamodel.util.DbUtils;
import edu.isu.isuese.datamodel.util.Filter;
import edu.isu.isuese.datamodel.util.FilterOperator;
import lombok.Builder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Project extends Model implements Measurable, ComponentContainer {

    public static void main(String[] args) {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:data/dev.db", "dev1", "");
        //Project sys = Project.createIt("version",  "1.0", "name", "java", "projKey", "3.0");

        Project sys2 = Project.findById(1);
        java.lang.System.out.println(sys2);
        Base.close();
    }

    public Project() {
    }

    @Builder(buildMethodName = "create")
    public Project(String projKey, String name, String version, String relPath, SCM scm) {
        set("projKey", projKey, "name", name, "version", version);
        save();
        if (relPath != null || !relPath.isEmpty())
            setRelPath(relPath);
        if (scm != null)
            addSCM(scm);
    }

    public String getProjectKey() {
        return getString("projKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setRelPath(name);
        set("name", name);
        save();
    }

    public String getVersion() {
        return getString("version");
    }

    public void setVersion(String version) {
        set("version", version);
        save();
    }

    public void addLanguage(Language lang) {
        add(lang);
        save();
    }

    public void removeLanguage(Language lang) {
        remove(lang);
        save();
    }

    public List<Language> getLanguages() {
        return getAll(Language.class);
    }

    public void addMeasure(Measure meas) {
        add(meas);
        save();
    }

    public void removeMeasure(Measure meas) {
        remove(meas);
        save();
    }

    public List<Measure> getMeasures() {
        return getAll(Measure.class);
    }

    public void addFinding(Finding find) {
        add(find);
        save();
    }

    public void removeFinding(Finding find) {
        remove(find);
        save();
    }

    public List<Finding> getFindings() {
        return getAll(Finding.class);
    }

    public void addPatternInstance(PatternInstance inst) {
        add(inst);
        save();
    }

    public void removePatternInstance(PatternInstance inst) {
        remove(inst);
        save();
    }

    public List<PatternInstance> getPatternInstances() {
        return getAll(PatternInstance.class);
    }

    public void addSCM(SCM scm) {
        add(scm);
        save();
    }

    public void removeSCM(SCM scm) {
        remove(scm);
        save();
    }

    public List<SCM> getSCMs() {
        return getAll(SCM.class);
    }

    public void addModule(Module mod) {
        add(mod);
        save();
    }

    public void removeModule(Module mod) {
        remove(mod);
        save();
    }

    public List<Module> getModules() {
        return getAll(Module.class);
    }

    public void addRelation(Relation rel) {
        add(rel);
        save();
    }

    public void removeRelation(Relation rel) {
        remove(rel);
        save();
    }

    public List<Relation> getRelations() {
        return getAll(Relation.class);
    }

    public List<Namespace> getNamespaces() {
        List<Namespace> namespaces = Lists.newArrayList();
        Queue<Namespace> queue = Queues.newArrayDeque();
        getModules().forEach(mod -> {
            queue.addAll(mod.getNamespaces());
        });

        while (!queue.isEmpty()) {
            Namespace ns = queue.poll();
            queue.addAll(ns.getNamespaces());
            namespaces.add(ns);
        }

        return namespaces;
    }

    public Namespace findNamespace(String name) {
        for (Namespace ns : getNamespaces()) {
            if (ns.getName().equals(name))
                return ns;
        }
        return null;
    }

    public boolean hasNamespace(String name) {
        return findNamespace(name) != null;
    }

    public void addFile(File file) {
        if (file != null) {
            add(file);
            save();
        }
    }

    public void removeFile(File file) {
        if (file != null) {
            remove(file);
            save();
        }
    }

    public List<File> getFiles() {
        return getAll(File.class);
    }

    public List<File> getFilesByType(FileType type) {
        List<File> files = getFiles();
        List<File> ret = Lists.newArrayList();
        for (File f : files) {
            if (f.getType().equals(type))
                ret.add(f);
        }

        return ret;
    }

    public File getFile(String key) {
        try {
            return get(File.class, "fileKey = ?", key).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasFile(String key) {
        return getFile(key) != null;
    }

    public List<Import> getImports() {
        return DbUtils.getImports(this.getClass(), (Integer) getId());
    }

    @Override
    public List<Type> getAllTypes() {
        return DbUtils.getTypes(this.getClass(), (Integer) getId());
    }

    public Type findType(String attribute, String value) {
        if (hasClass(attribute, value))
            return findClass(attribute, value);
        if (hasInterface(attribute, value))
            return findInterface(attribute, value);
        if (hasEnum(attribute, value))
            return findEnum(attribute, value);
        return null;
    }

    public boolean hasType(String attribute, String value) {
        return findType(attribute, value) != null;
    }

    @Override
    public List<Class> getClasses() {
        return DbUtils.getClasses(this.getClass(), (Integer) getId());
    }

    public Type findClass(String attribute, String value) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute(attribute)
                            .op(FilterOperator.EQ)
                            .table("classes")
                            .value(value)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasClass(String attribute, String value) {
        return findClass(attribute, value) != null;
    }

    @Override
    public List<Interface> getInterfaces() {
        return DbUtils.getInterfaces(this.getClass(), (Integer) getId());
    }

    public Type findInterface(String attribute, String value) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute(attribute)
                            .op(FilterOperator.EQ)
                            .table("interfaces")
                            .value(value)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasInterface(String attribute, String value) {
        return findInterface(attribute, value) != null;
    }

    @Override
    public List<Enum> getEnums() {
        return DbUtils.getEnums(this.getClass(), (Integer) getId());
    }

    public Type findEnum(String attribute, String value) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute(attribute)
                            .op(FilterOperator.EQ)
                            .table("enums")
                            .value(value)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasEnum(String attribute, String value) {
        return findEnum(attribute, value) != null;
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
        List<System> systems = Lists.newLinkedList();
        systems.add(parent(System.class));
        return systems;
    }

    @Override
    public String getRefKey() {
        return getString("projKey");
    }

    public void addRelation(Component from, Component to, RelationType type) {
        Reference refFrom = Reference.createIt("refKey", from.getRefKey(), "type", RefType.fromComponent(from));
        Reference refTo = Reference.createIt("refKey", from.getRefKey(), "type", RefType.fromComponent(to));

        Relation rel = Relation.createIt();
        rel.setToAndFromRefs(refTo, refFrom);
        rel.setType(type);
        rel.save();

        add(rel);
    }

    public void addUnknownType(UnknownType type) {
        if (type == null)
            return;

        add(type);
        save();
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        setString("relPath", path);
        save();
    }

    public void updateKeys(String parentKey) {
        String newKey = parentKey + ":" + getName() + "-" + getVersion();
        setString("projKey", newKey);
        save();
        getModules().forEach(Module::updateKey);
    }

    public String getFullPath() {
        String path = "";

        if (parent(System.class) != null) {
            path = parent(System.class).getBasePath().replaceAll("/", java.io.File.separator);
        }

        if (!path.endsWith(java.io.File.separator)) {
            path += java.io.File.separator;
        }

        path += getRelPath();

        if (!path.endsWith(java.io.File.separator)) {
            path += java.io.File.separator;
        }

        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Project) {
            Project comp = (Project) o;
            return comp.getProjectKey().equals(this.getProjectKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProjectKey());
    }
}
