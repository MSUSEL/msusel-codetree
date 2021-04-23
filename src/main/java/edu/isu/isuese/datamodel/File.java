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
        set("fileKey", fileKey, "name", name, "pathIndex", 0);
        setType(type);
        if (relPath != null && !relPath.isEmpty()) {
            setRelPath(relPath);
        }
        setStart(start);
        setEnd(end);
        setParseStage(0);
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
        if (t != null)
            t.setFileID(this.getId());
    }

    public void removeType(Type t) {
        if (t != null && t.getFileID() != null && t.getFileID().equals(getId()))
            t.setFileID(null);
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
        return Class.find("parent_file_id = ?", this.getId());
    }

    public Class findClass(String attribute, Object value) {
        try {
            return (Class) Class.find(attribute + " = ? and parent_file_id = ?", value, this.getId()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public List<Interface> getInterfaces() {
        return Interface.find("parent_file_id = ?", this.getId());
    }

    public Interface findInterface(String attribute, Object value) {
        try {
            return (Interface) Interface.find(attribute + " = ? and parent_file_id = ?", value, this.getId()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public List<Enum> getEnums() {
        return Enum.find("parent_file_id = ?", this.getId());
    }

    public Enum findEnum(String attribute, Object value) {
        try {
            return (Enum) Enum.find(attribute + " = ? and parent_file_id = ?", value, this.getId()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
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
        getEnums().forEach(type -> literals.addAll(type.getLiterals()));
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

    public void setPathIndex(int index) {
        setInteger("pathIndex", index);
    }

    public int getPathIndex() {
        return getInteger("pathIndex");
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
        String[] oldSplit = oldPrefix.split(":");
        String[] newSplit = newPrefix.split(":");
        String toReplace = "";
        String replacement = "";
        if (oldSplit.length == 2) {
            toReplace = oldSplit[0];
            replacement = newSplit[0];
        } else {
            toReplace = oldSplit[1];
            replacement = newSplit[1];
        }
        String copyName = getName().replace(toReplace, replacement);

        File copy = File.builder()
                .name(copyName)
                .fileKey(oldPrefix + ":" + copyName)
                .relPath(this.getRelPath())
                .type(this.getType())
                .start(this.getStart())
                .end(this.getEnd())
                .create();
        copy.save();

        Namespace ns = this.getParentNamespace();
        if (ns != null) {
            String key = ns.getNsKey();
            String copyKey = key.replace(oldPrefix, newPrefix);

            Namespace nsCopy = Namespace.findFirst("nsKey = ?", key.replace(oldPrefix, newPrefix));
            if (nsCopy != null) {
                nsCopy.addFile(copy);
                nsCopy.save();
            }
        }
        copy.save();

        getAllTypes().forEach(type -> {
            String copyKey = type.getCompKey().replace(oldPrefix, newPrefix);
            if (type instanceof Class)
                copy.addType(Class.findFirst("compKey = ?", copyKey));
            else if (type instanceof Enum)
                copy.addType(Enum.findFirst("compKey = ?", copyKey));
            else if (type instanceof Interface)
                copy.addType(Interface.findFirst("compKey = ?", copyKey));
        });
        copy.save();

        return copy;
    }

    public List<System> getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystem();
        return null;
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

    @Override
    public String getRefKey() {
        return getString("fileKey");
    }

    public void updateKey() {
        Project parent = getParentProject();
        String newKey;
        if (parent != null)
            newKey = parent.getProjectKey() + ":" + getName();
        else
            newKey = getName();

        setString("fileKey", newKey);
        save();
        refresh();
        getAllTypes().forEach(Type::updateKey);
    }

    public Type getTypeByName(String name) {
        for (Type t : getAllTypes()) {
            if (t.getName().equals(name))
                return t;
        }

        return null;
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

    public String getFullPath() {
        char sep = java.io.File.separatorChar;
        Project proj = getParentProject();
        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getFullPath(getType(), getPathIndex()) + getRelPath();
        else {
            switch (getType()) {
                case BINARY:
                    return proj.getFullPath() + proj.getBinaryPath(getPathIndex()) + sep + getRelPath();
                case SOURCE:
                    return proj.getFullPath() + proj.getSrcPath(getPathIndex()) + sep + getRelPath();
                case TEST:
                    return proj.getFullPath() + proj.getTestPath(getPathIndex()) + sep + getRelPath();
                default:
                    return proj.getFullPath() + getRelPath();
            }
        }
    }

    public void setParentNSID(Object id) {
        set("parent_ns_id", id);
        save();
    }

    public Object getParentNSID() {
        return get("parent_ns_id");
    }

    public Namespace getParentNamespace() {
        if (getParentNSID() != null)
            return Namespace.findById(getParentNSID());
        return null;
    }

    /**
     * @return The parent file of this Measurable
     */
    @Override
    public File getParentFile() {
        return this;
    }

    public int getParseStage() {
        if (getInteger("parseStage") == null)
            return 0;
        else
            return getInteger("parseStage");
    }

    public void setParseStage(int stage) {
        setInteger("parseStage", stage);
        save();
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
}
