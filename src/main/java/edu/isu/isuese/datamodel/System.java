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

import edu.isu.isuese.datamodel.util.DbUtils;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class System extends Model implements Measureable {

    private static final String ALL_PATTERN_CHAINS = "SELECT * FROM pattern_chains JOIN systems ON pattern_chains.system_id = 1;";
    private static final String ALL_SCM = "SELECT * FROM scms JOIN projects ON scms.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_MODULES = "SELECT * FROM modules JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_NAMESPACES = "SELECT * FROM namespaces JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_FILES = "SELECT * FROM files JOIN namespaces ON files.namespace_id = namespaces.id JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_IMPORTS = "SELECT * FROM imports JOIN files ON imports.file_id = files.id JOIN namespaces ON files.namespace_id = namespaces.id JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_TYPES = "";
    private static final String ALL_CLASSES = "SELECT * FROM classes JOIN files ON classes.file_id = files.id JOIN namespaces ON files.namespace_id = namespaces.id JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_INTERFACES = "SELECT * FROM interfaces JOIN files ON interfaces.file_id = files.id JOIN namespaces ON files.namespace_id = namespaces.id JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_ENUMS = "SELECT * FROM enums JOIN files ON enums.file_id = files.id JOIN namespaces ON files.namespace_id = namespaces.id JOIN modules ON namespaces.module_id = modules.id JOIN projects ON modules.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_MEMBERS = "";
    private static final String ALL_LITERALS = "";
    private static final String ALL_INITIALIZERS = "";
    private static final String ALL_TYPED_MEMBERS = "";
    private static final String ALL_FIELDS = "";
    private static final String ALL_METHODS = "";
    private static final String ALL_ACTUAL_METHODS = "";
    private static final String ALL_CONSTRUCTORS = "";
    private static final String ALL_DESTRUCTORS = "";
    private static final String ALL_PATTERN_INSTANCES = "SELECT * FROM pattern_instances JOIN projects ON pattern_instances.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_LANGUAGES = "SELECT * FROM languages JOIN projects_languages ON languages.id = projects_languages.language_id JOIN projects ON projects_languages.language_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_MEASURES = "SELECT * FROM measures JOIN projects ON measures.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_ROLE_BINDINGS = "SELECT * FROM role_bindings JOIN pattern_instances ON role_bindings.pattern_instance_id = pattern_instances.id JOIN projects ON pattern_instances.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_FINDINGS = "SELECT * FROM findings JOIN projects ON findings.project_id = projects.id JOIN systems ON projects.system_id = 1;";
    private static final String ALL_RELATIONS = "SELECT * FROM relations JOIN projects ON relations.project_id = projects.id JOIN systems ON projects.system_id = 1;";

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

    public List<File> getFiles() {
        return DbUtils.getFiles(this.getClass(), (Integer) getId());
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
}
