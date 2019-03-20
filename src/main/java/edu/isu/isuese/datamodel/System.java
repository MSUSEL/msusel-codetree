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

import edu.isu.isuese.datamodel.util.DbUtils;
import edu.isu.isuese.datamodel.util.Filter;
import edu.isu.isuese.datamodel.util.FilterOperator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class System extends Model implements Measurable, Structure {

    public void addProject(Project p) {
        add(p);
        save();
    }

    public void removeProject(Project p) {
        remove(p);
        save();
    }

    public List<Project> getProjects() {
        return getAll(Project.class);
    }

    public void addPatternChain(PatternChain p) {
        add(p);
        save();
    }

    public void removePatternChain(PatternChain p) {
        remove(p);
        save();
    }

    public List<PatternChain> getPatternChains() {
        return getAll(PatternChain.class);
    }

    public List<SCM> getSCMs() {
        return DbUtils.getSCMs(this.getClass(), (Integer) getId());
    }

    public List<Module> getModules() {
        return DbUtils.getModules(this.getClass(), (Integer) getId());
    }

    public List<Namespace> getNamespaces() {
        return DbUtils.getNamespaces(this.getClass(), (Integer) getId());
    }

    public Namespace findNamespace(String name) {
        try {
            return DbUtils.getNamespaces(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute("name")
                            .op(FilterOperator.EQ)
                            .table("namespaces")
                            .value(name)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasNamespace(String name) {
        return findNamespace(name) != null;
    }

    public List<File> getFiles() {
        return DbUtils.getFiles(this.getClass(), (Integer) getId());
    }

    public List<Import> getImports() {
        return DbUtils.getImports(this.getClass(), (Integer) getId());
    }

    public List<Type> getTypes() {
        return DbUtils.getTypes(this.getClass(), (Integer) getId());
    }

    public Type findType(String name) {
        if (hasClass(name))
            return findClass(name);
        if (hasInterface(name))
            return findInterface(name);
        if (hasEnum(name))
            return findEnum(name);
        return null;
    }

    public boolean hasType(String name) {
        return findType(name) != null;
    }

    public List<Class> getClasses() {
        return DbUtils.getClasses(this.getClass(), (Integer) getId());
    }

    public Type findClass(String name) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute("name")
                            .op(FilterOperator.EQ)
                            .table("classes")
                            .value(name)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasClass(String name) {
        return findClass(name) != null;
    }

    public List<Interface> getInterfaces() {
        return DbUtils.getInterfaces(this.getClass(), (Integer) getId());
    }

    public Type findInterface(String name) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute("name")
                            .op(FilterOperator.EQ)
                            .table("interfaces")
                            .value(name)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasInterface(String name) {
        return findInterface(name) != null;
    }

    public List<Enum> getEnums() {
        return DbUtils.getEnums(this.getClass(), (Integer) getId());
    }

    public Type findEnum(String name) {
        try {
            return DbUtils.getClasses(this.getClass(), (Integer) getId(),
                    Filter.builder()
                            .attribute("name")
                            .op(FilterOperator.EQ)
                            .table("enums")
                            .value(name)
                            .build()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasEnum(String name) {
        return findEnum(name) != null;
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

    public List<PatternInstance> getPatternInstances() {
        return DbUtils.getPatternInstances(this.getClass(), (Integer) getId());
    }

    public List<Language> getLanguages() {
        return DbUtils.getLanguages(this.getClass(), (Integer) getId());
    }

    public List<Measure> getMeasures() {
        return DbUtils.getMeasures(this.getClass(), (Integer) getId());
    }

    public List<RoleBinding> getRoleBindings() {
        return DbUtils.getRoleBindings(this.getClass(), (Integer) getId());
    }

    public List<Finding> getFindings() {
        return DbUtils.getFindings(this.getClass(), (Integer) getId());
    }

    public List<Relation> getRelations() {
        return DbUtils.getRelations(this.getClass(), (Integer) getId());
    }

    @Override
    public String getRefKey() {
        return getIdName();
    }
}
