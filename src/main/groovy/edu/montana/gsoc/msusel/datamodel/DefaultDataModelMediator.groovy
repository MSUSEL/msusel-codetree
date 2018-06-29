/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
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
package edu.montana.gsoc.msusel.datamodel

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Lists
import com.google.common.collect.Queues
import com.google.common.collect.Sets
import com.google.common.collect.Table
import edu.montana.gsoc.msusel.datamodel.environment.EnvironmentLoader
import edu.montana.gsoc.msusel.datamodel.measures.*
import edu.montana.gsoc.msusel.datamodel.member.*
import edu.montana.gsoc.msusel.datamodel.pattern.*
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Module
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.structural.Project
import edu.montana.gsoc.msusel.datamodel.structural.Structure
import edu.montana.gsoc.msusel.datamodel.type.*
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class DefaultDataModelMediator implements DataModelMediator {

    Project project
    Table<Type, Type, List<RelationType>> table = HashBasedTable.create()

    List<Type> unknownTypes = []
    Map<String, Namespace> langNs = [:]
    EnvironmentLoader loader
    System system
    List<Project> projects
    List<Module> modules
    List<Namespace> namespaces
    List<File> files
    List<Type> types
    List<Member> members
    List<PatternChain> patternChains
    List<Pattern> patterns
    List<Role> roles
    List<PatternInstance> instances
    List<PatternRepository> patternRepos
    List<RoleBinding> bindings
    List<Finding> findings
    List<Rule> rules
    List<RuleRepository> ruleRepos
    List<Measure> measures
    List<Metric> metrics
    List<MetricRepository> metricRepos

    /**
     *
     */
    DefaultDataModelMediator() {

    }

    void addRelation(Type from, Type to, RelationType r) {
        List<RelationType> list = table.get(from, to) != null ? table.get(from, to) : []
        if (r != null)
            list << r
        table.put(from, to, list)
    }

    void addGeneralizes(Type from, Type to) {
        addRelation(from, to, RelationType.GENERALIZATION)
    }

    List<Type> getGeneralizedFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue().contains(RelationType.GENERALIZATION) })
    }

    List<Type> getGeneralizedTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.GENERALIZATION) })
    }

    @Override
    boolean hasGeneralization(Type from, Type to) {
        return false
    }

    def extractRelations(map) {
        def list = []
        map.each {
            list << it.getKey()
        }
        list
    }

    void addRealizes(Type from, Type to) {
        addRelation(from, to, RelationType.REALIZATION)
    }

    List<Type> getRealizedFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue().contains(RelationType.REALIZATION) })
    }

    List<Type> getRealizedTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.REALIZATION) })
    }

    @Override
    boolean hasRealization(Type from, Type to) {
        return false
    }

    void addAssociation(Type from, Type to, boolean bidirectional) {
        addRelation(from, to, RelationType.ASSOCIATION)
        if (bidirectional)
            addRelation(to, from, RelationType.ASSOCIATION)
    }

    List<Type> getAssociatedFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue().contains(RelationType.ASSOCIATION) })
    }

    List<Type> getAssociatedTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.ASSOCIATION) })
    }

    void addAggregation(Type from, Type to, boolean bidirectional) {
        addRelation(from, to, RelationType.AGGREGATION)
        if (bidirectional)
            addRelation(to, from, RelationType.AGGREGATION)
    }

    List<Type> getAggregatedFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue() == RelationType.ASSOCIATION })
    }

    List<Type> getAggregatedTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue() == RelationType.ASSOCIATION })
    }

    void addComposition(Type from, Type to, boolean bidirectional) {
        addRelation(from, to, RelationType.COMPOSITION)
        if (bidirectional)
            addRelation(to, from, RelationType.COMPOSITION)
    }

    List<Type> getComposedFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue().contains(RelationType.COMPOSITION) })
    }

    List<Type> getComposedTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.COMPOSITION) })
    }

    void addUse(Type from, Type to) {
        addRelation(from, to, RelationType.USE)
    }

    List<Type> getUseFrom(Type from) {
        extractRelations(table.row(from).findAll { it.getValue() == RelationType.USE })
    }

    List<Type> getUseTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.USE) })
    }

    void addDependency(Type from, Type to) {
        addRelation(from, to, RelationType.DEPENDENCY)
    }

    @Override
    boolean hasDependency(Type from, Type to) {
        return false
    }

    void addContainment(Type contained, Type container) {
        addRelation(contained, container, RelationType.CONTAINMENT)
    }

    List<Type> getDependencyFrom(Type from) {
        extractRelations(table.row(from).values().findAll { it.contains(RelationType.DEPENDENCY) })
    }

    List<Type> getDependencyTo(Type to) {
        extractRelations(table.column(to).findAll { it.getValue().contains(RelationType.DEPENDENCY) })
    }

    List<Type> getContainedIn(Type container) {
        extractRelations(table.column(container).findAll { it.getValue().contains(RelationType.CONTAINMENT) })
    }

    List<Type> getContainedBy(Type contained) {
        extractRelations(table.row(contained).findAll { it.getValue().contains(RelationType.CONTAINMENT) })
    }

    List<Type> getTypesUsingMethod(Method method) {
        []
    }

    List<Type> getMethodsCalledFrom(Type type) {
        []
    }

    List<Type> getMethodsCalledFrom(Method method) {
        []
    }

    List<Type> getMethodsCallingMethod(Method method) {
        []
    }

    List<Type> getFieldsUsedBy(Method method) {
        []
    }

    @Override
    List<Field> getFieldsUsedBy(Type type) {
        []
    }

    List<Type> getAllParentClasses(Type type) {
        []
    }

    List<Type> getAllDescendentClasses(Type type) {
        []
    }

    void addUnknownType(Type node) {
        if (node != null && !unknownTypes.contains(node))
            unknownTypes.add(node)
    }

    boolean hasBiDirectionalAssociation(Type from, Type to) {
        hasUniDirectionalAssociation(from, to) && hasUniDirectionalAssociation(to, from)
    }

    boolean hasContainmentRelation(Type from, Type to) {
        table.get(from, to)?.contains(RelationType.CONTAINMENT)
    }

    boolean hasUniDirectionalAssociation(Type from, Type to) {
        table.get(from, to)?.contains(RelationType.ASSOCIATION)
    }

    boolean hasUseDependency(Type from, Type to) {
        table.get(from, to)?.contains(RelationType.DEPENDENCY)
    }

    Namespace languageNamespace(String s) {
        langNs[s]
    }

    void addLanguageNamespace(String s, Namespace namespaceNode) {
        println "NS: ${s}@${namespaceNode}"
        if (s != null && !s.empty && namespaceNode != null)
            langNs[s] = namespaceNode
    }

    boolean inheritsFrom(Type type, Type gen) {
        table.get(type, gen)?.contains(RelationType.GENERALIZATION)
    }

    boolean realizes(Type type, Type real) {
        table.get(type, real)?.contains(RelationType.REALIZATION)
    }

    @Override
    System getSystem() {
        system
    }

    @Override
    void updateMetricRepository(MetricRepository metricRepository) {

    }

    @Override
    void updatePatternRepository(PatternRepository patternRepository) {

    }

    @Override
    void updatePattern(Pattern pattern) {

    }

    @Override
    void updateRuleRepository(RuleRepository ruleRepository) {

    }

    @Override
    void setSystem(System sys) {
        if (sys)
            system = sys
    }

    @Override
    List<Method> getMethodUseInSameClass(Method m, Type t) {
        return null
    }

    @Override
    List<Field> getFieldUseInSameClass(Method m, Type t) {
        return null
    }

    @Override
    void addFinding(Finding f) {
        if (f && !findings.contains(f))
            findings << f
    }

    @Override
    Finding getFinding(String key) {
        if (key) {
            return findings.find { it.findingKey == key}
        }
        null
    }

    @Override
    void addMeasure(Measure m) {
        if (m && !measures.contains(m))
            measures << m
    }

    @Override
    Measure getMeasure(String key) {
        if (key) {
            return measures.find { it.measureKey == key}
        }
        null
    }

    @Override
    void addMetric(Metric m) {
        if (m && !metrics.contains(m))
            metrics << m
    }

    @Override
    Metric getMetric(String key) {
        if (key) {
            return metrics.find { it.metricKey == key}
        }
        null
    }

    @Override
    void addMetricRepository(MetricRepository repo) {
        if (repo && !metricRepos.contains(repo))
            metricRepos << repo
    }

    @Override
    MetricRepository getMetricRepository(String key) {
        if (key) {
            return metricRepos.find { it.repoKey == key}
        }
        null
    }

    @Override
    void addRule(Rule r) {
        if (r && !rules.contains(r))
            rules << r
    }

    @Override
    Rule getRule(String key) {
        if (key) {
            return rules.find { it.ruleKey == key}
        }
        null
    }

    @Override
    void addRuleRepository(RuleRepository repo) {
        if (repo && !ruleRepos.contains(repo))
            ruleRepos << repo
    }

    @Override
    RuleRepository getRuleRepository(String key) {
        if (key) {
            return ruleRepos.find { it.repoKey == key}
        }
        null
    }

    @Override
    void addPattern(Pattern p) {
        if (p && !patterns.contains(p))
            patterns << p
    }

    @Override
    Pattern getPattern(String key) {
        if (key) {
            return patterns.find { it.patternKey == key}
        }
        null
    }

    @Override
    void addPatternChain(PatternChain pc) {
        if (pc && !patternChains.contains(pc))
            patternChains << pc
    }

    @Override
    PatternChain getPatternChain(String key) {
        if (key) {
            return patternChains.find { it.chainKey == key}
        }
        null
    }

    @Override
    void addPatternInstance(PatternInstance i) {
        if (i && !instances.contains(i))
            instances << i
    }

    @Override
    PatternInstance getPatternInstance(String key) {
        if (key) {
            return instances.find { it.instKey == key}
        }
        null
    }

    @Override
    void addPatternRepository(PatternRepository repo) {
        if (repo && !patternRepos.contains(repo))
            patternRepos << repo
    }

    @Override
    PatternRepository getPatternRepository(String key) {
        if (key) {
            return patternRepos.find { it.repoKey == key}
        }
        null
    }

    @Override
    void addRole(Role r) {
        if (r && !roles.contains(r))
            roles << r
    }

    @Override
    Role getRole(String key) {
        if (key) {
            return roles.find { it.roleKey == key}
        }
        null
    }

    @Override
    void addFile(File f) {
        if (f && !files.contains(f))
            files << f
    }

    /**
     * Searches the DataModelMediator for a File whose qualified identifier matches
     * the one provided.
     *
     * @param file
     *            Qualified Identifier
     * @return File with matching qualified identifier to the one provided,
     *         or null if no such File exists in the DataModelMediator or the
     *         provided identifier is null or empty.
     */
    @Override
    File getFile(String key) {
        if (key) {
            return files.find { it.fileKey == key}
        }
        null
    }

    @Override
    void addModule(Module m) {
        if (m && !modules.contains(m))
            modules << m
    }

    @Override
    Module getModule(String key) {
        if (key) {
            return modules.find { it.structKey == key}
        }
        null
    }

    @Override
    void addNamespace(Namespace n) {
        if (n && !namespaces.contains(n))
            namespaces << n
    }

    @Override
    Namespace getNamespace(String key) {
        if (key) {
            return namespaces.find { it.nsKey == key}
        }
        null
    }

    @Override
    void addProject(Project p) {
        if (p && !projects.contains(p))
            projects << p
    }

    @Override
    Project getProject(String key) {
        if (key) {
            return projects.find { it.structKey == key}
        }
        null
    }

    @Override
    void addClass(Class c) {
        if (c && !types.contains(c))
            types << c
    }

    @Override
    Class getClass(String key) {
        if (key) {
            return (Class) types.find { it instanceof Class && it.compKey == key}
        }
        null
    }

    @Override
    void addEnum(Enum e) {
        if (e && !types.contains(e))
            types << e
    }

    @Override
    Enum getEnum(String key) {
        if (key) {
            return (Enum) types.find { it instanceof Enum && it.compKey == key}
        }
        null
    }

    @Override
    void addInterface(Interface i) {
        if (i && !types.contains(i))
            types << i
    }

    @Override
    Interface getInterface(String key) {
        if (key) {
            return (Interface) types.find { it instanceof Interface && it.compKey == key}
        }
        null
    }

    @Override
    void addEvent(Event e) {
        if (e && !types.contains(e))
            types << e
    }

    @Override
    Event getEvent(String key) {
        if (key) {
            return (Event) types.find { it instanceof Event && it.compKey == key}
        }
        null
    }

    @Override
    void addStruct(Struct s) {
        if (s && !types.contains(s))
            types << s
    }

    @Override
    Struct getStruct(String key) {
        if (key) {
            return (Struct) types.find { it instanceof Struct && it.compKey == key}
        }
        null
    }

    @Override
    void addConstructor(Constructor c) {
        if (c && !members.contains(c))
            members << c
    }

    @Override
    Constructor getConstructor(String key) {
        if (key) {
            return (Constructor) members.find { it instanceof Constructor && it.compKey == key}
        }
        null
    }

    @Override
    void addDestructor(Destructor d) {
        if (d && !members.contains(d))
            members << d
    }

    @Override
    Destructor getDestructor(String key) {
        if (key) {
            return (Destructor) members.find { it instanceof Destructor && it.compKey == key}
        }
        null
    }

    @Override
    void addField(Field f) {
        if (f && !members.contains(f))
            members << f
    }

    @Override
    Field getField(String key) {
        if (key) {
            return (Field) members.find { it instanceof Field && it.compKey == key}
        }
        null
    }

    @Override
    void addInitializer(Initializer i) {
        if (i && !members.contains(i))
            members << i
    }

    @Override
    Initializer getInitializer(String key) {
        if (key) {
            return (Initializer) members.find { it instanceof Initializer && it.compKey == key}
        }
        null
    }

    @Override
    void addLiteral(Literal l) {
        if (l && !members.contains(l))
            members << l
    }

    @Override
    Literal getLiteral(String key) {
        if (key) {
            return (Literal) members.find { it instanceof Literal && it.compKey == key}
        }
        null
    }

    @Override
    void addMethod(Method m) {
        if (m && !members.contains(m))
            members << m
    }

    @Override
    Method getMethod(String key) {
        if (key) {
            return (Method) members.find { it instanceof Method && it.compKey == key}
        }
        null
    }

    @Override
    void addProperty(Property p) {
        if (p && !members.contains(p))
            members << p
    }

    @Override
    Property getProperty(String key) {
        if (key) {
            return (Property) members.find { it instanceof Property && it.compKey == key}
        }
        null
    }

    /**
     * Searches for a file with the given qualified identifier in this DataModelMediator.
     *
     * @param qid
     *            Qualified identifier
     * @return The File matching the given identifier, or null if the given
     *         identifier is null or empty or if no such file exists in the
     *         model.
     */
    File findFile(final String qid) {
        if (qid == null || qid.isEmpty()) {
            return null
        }

        for (Project p : getProjects()) {
            if (p.hasFile(qid)) {
                return p.findFile(qid)
            } else {
                for (Module m : p.getModules()) {
                    if (m.hasFile(qid))
                        return m.findFile(qid)
                }
            }
        }
        return null
    }

    Namespace findNamespace(Type t) {

    }

    /**
     * Retrieves the namespace in the model with the given identifier
     *
     * @param identifier Identifier of the namespace to find
     * @return The namespace in the associated model, with the given identifier. If no such namespace exists or if the provided identifier is null or the empty string, then null is returned
     */
    Namespace findNamespace(final String identifier) {
        if (identifier == null || identifier.isEmpty())
            return null

        for (Project p : getProjects()) {
            if (p.hasNamespace(identifier))
                return p.getNamespace(identifier)
            else {
                for (Module m : p.modules()) {
                    if (m.hasNamespace(identifier))
                        return m.getNamespace(identifier)
                }
            }
        }
    }

    /**
     * @return The set of all namespaces within the model.
     */
    List<Namespace> getNamespaces() {
        List<Namespace> namespaces = Lists.newArrayList()

        Queue<Project> queue = Queues.newArrayDeque()

        queue.add(tree.getSystem())

        while (!queue.isEmpty()) {
            Project pn = queue.poll()
            namespaces.addAll(pn.namespaces())
            queue.addAll(pn.subprojects())
        }

        namespaces
    }

    /**
     * Searches for a Method with the given qualified identifier.
     *
     * @param identifier
     *            Qualified Identifier
     * @return Method with matching qualified identifier, or null if the
     *         identifier is null, empty, or no such Method exists.
     */
    Method findMethod(final String identifier) {
        if (identifier == null || identifier.isEmpty())
            return null
        String[] ids = identifier.split("#")
        Type tn = findType(ids[0])

        if (tn != null)
            return tn.getMethod(ids[1])

        null
    }

    /**
     * Searches the DataModelMediator for a Module with the given qualified
     * identifier.
     *
     * @param qIdentifier
     *            Qualified Identifier
     * @return The Module with matching qualified identifier, or null if the
     *         provided qualified identifier is null or empty or no such
     *         matching Module exists.
     */
    Module findModule(String qIdentifier) {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return null

        Set<Project> projects = getProjects()

        for (Project p : projects) {
            if (p.hasModule(qIdentifier))
                return p.getModule(qIdentifier)
        }

        null
    }

    @Override
    Module findModule(Type t) {
        return null
    }
/**
     * Searches the code model for a project with a qualified identifier matching
     * the qualified identifier provided.
     *
     * @param qid
     *            Qualified Identifier
     * @return Project with matching qualified identifier as the one
     *         provided, or null if no such Project exists in the DataModelMediator
     *         or the provided identifier is null or empty.
     */
    Project findProject(String qid) {
        if (qid == null || qid.isEmpty())
            return null

        Queue<Project> queue = Queues.newArrayDeque()

        if (this.tree.getSystem() != null)
            queue.offer(this.tree.getSystem())

        while (!queue.isEmpty()) {
            Project node = queue.poll()
            if (node.getKey() == qid)
                return node

            for (Project pn : node.getSubProjects()) {
                queue.offer(pn)
            }
        }

        null
    }

    /**
     * Searches the DataModelMediator for a Type whose qualified identifier matches
     * the one provided.
     *
     * @param key
     *            Qualified Identifier
     * @return Type with matching qualified identifier to the one provided,
     *         or null if no such Type exists in the DataModelMediator or the
     *         provided identifier is null or empty.
     */
    Type findType(final String key) {
        if (key == null || key.isEmpty())
            return null

        Type ret = null

        for (final Type type : getTypes()) {
            if (type.key == key) {
                ret = type
            }
        }

        if (ret == null) {
            for (final Type type : tree.unknownTypes) {
                if (type.key == key) {
                    ret = type
                }
            }
        }

        ret
    }

    List<Type> findTypes(Structure struct) {
        []
    }

    List<Type> findTypes(PatternInstance inst) {
        []
    }

    List<Type> findTypes(File file) {
        []
    }

    List<Type> findTypes(Namespace namespace) {
        []
    }

    /**
     * @return The set of all files within the model.
     */
    List<File> getFiles() {
        Set<File> files = Sets.newHashSet()

        Queue<Project> queue = Queues.newArrayDeque()

        queue.add(tree.getSystem())

        while (!queue.isEmpty()) {
            Project pn = queue.poll()
            files.addAll(pn.files())
            queue.addAll(pn.subprojects())
        }

        files
    }

    /**
     * @return The set of all methods within the model.
     */
    List<Method> getMethods() {
        final Set<Method> methods = Sets.newHashSet()

        getTypes().forEach({ type -> methods.addAll(type.methods()) })

        methods
    }

    /**
     * @return The set of all projects within the model (including the root
     *         project)
     */
    List<Project> getProjects() {
        Set<Project> projects = Sets.newHashSet()
        Queue<Project> q = Queues.newArrayDeque()

        if (tree.getSystem() != null)
            q.offer(tree.getSystem())

        while (!q.isEmpty()) {
            Project project = q.poll()
            projects.add(project)
            q.addAll(project.subprojects())
        }

        projects
    }

    /**
     * @return The set of all known types in the model.
     */
    List<Type> getTypes() {
        final Set<Type> types = Sets.newHashSet()

        getFiles().forEach({ file -> types.addAll(findTypes(file)) })

        types
    }

    /**
     * Merges the DataModelMediator this class operates upon with the one provided.
     *
     * @param other
     *            DataModelMediator to merge into the currently operated on DataModelMediator.
     */
    void merge(DataModelMediator other) {
        if (other == null)
            return

        Project pn = other.getSystem()

        if (tree.getSystem() == null) {
            tree.setSystem(pn)
        }
        if (pn.hasParent()) {
            if (pn.getParentKey() == tree.getSystem().getQIdentifier()) {
                tree.getSystem().addSubProject(pn)
            }
        } else if (tree.getSystem() == pn) {
            tree.getSystem().update(pn)
        }
    }

    /**
     * Finds the file with the given identifier and removes it from its parent
     * in the DataModelMediator.
     *
     * @param file
     *            Qualified Identifier of the file to be removed.
     */
    void removeFile(final String file) {
        if (file == null || file.isEmpty()) {
            return
        }
    }

    /**
     * Updates a file in the DataModelMediator using the provided file. This method first
     * searches the model for a corresponding file matching the provided one. If
     * found the existing file is merged with the provided one. If no such file
     * exists, the new one is added to the model. Finally, if the provided file
     * is null, nothing happens.
     *
     * @param node
     *            File to be used to update the model.
     */
    synchronized void updateFile(final File node) {
        if (node == null)
            return

        Structure container = findProject(node.getParentKey()) != null ? findProject(node.getParentKey()) : findModule(node.getParentKey())

        if (container.findFile(node.fileKey) == null) {
            container.addChild(node)
        } else {
            container.findFile(node.fileKey).update(node)
        }
    }

    File findParentFile(Component codeNode) {
        findFile(codeNode.compKey)
    }

    List<Type> getNamespaceClasses(Namespace namespace) {
        null
    }

    Module getModuleForType(Type type) {
        null
    }

    @Override
    List<Type> getModuleClasses(Module mod) {
        return null
    }

    Set<Type> getModuleClasses(Set<Type> types) {
        null
    }

    List<Method> findMethods(Structure struct) {
        []
    }

    List<File> findFiles(Structure structure) {
        null
    }

    @Override
    List<File> findFiles(Pattern pattern) {
        return null
    }

    List<File> findFiles(Namespace namespace) {
        []
    }

    @Override
    boolean hasNamespace(String key) {
        return false
    }

    @Override
    Measurable findParent(Measurable decorated) {
        return null
    }

    @Override
    Project currentProject() {
        return project
    }

    @Override
    Component findComponentByName(String name) {
        null
    }

    def storeMeasure(Measure m) {
        measures.put(m.getItemKey(), m.getMetricKey(), m.getValue())
    }

    def retrieveMeasure(Measurable node, String metric) {
        measures.get(node.getKey(), metric)
    }

    boolean hasMetric(Measurable c, String metric) {
        measures.get(c.getKey(), metric) != null
    }

    List<Double> getAllClassValues(String metric) {
        List<Double> values = []

        tree.getTypes().each { values << (double) retrieve(it, metric) }

        values
    }

    List<Double> getAllFileValues(String metric) {
        List<Double> values = []

        tree.getFiles().each { values << (double) retrieve(it, metric) }

        values
    }

    List<Double> getAllMethodValues(String metric) {
        List<Double> values = []

        tree.getMethods().each { values << (double) retrieve(it, metric) }

        values
    }

    double getProjectMetric(String metric) {
        if (hasMetric(getProject(), metric))
            (double) retrieve(tree.getSystem(), metric)
        else
            0.0d
    }
}