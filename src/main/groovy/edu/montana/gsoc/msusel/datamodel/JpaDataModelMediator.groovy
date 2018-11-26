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
package edu.montana.gsoc.msusel.datamodel

import com.google.inject.Inject
import edu.montana.gsoc.msusel.dao.*
import edu.montana.gsoc.msusel.datamodel.measures.*
import edu.montana.gsoc.msusel.datamodel.member.*
import edu.montana.gsoc.msusel.datamodel.pattern.*
import edu.montana.gsoc.msusel.datamodel.relations.Relation
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.structural.*
import edu.montana.gsoc.msusel.datamodel.type.*

class JpaDataModelMediator implements DataModelMediator {

    @Inject ClassDAO classDao
    @Inject EventDAO eventDao
    @Inject StructDAO structDao
    @Inject ConstructorDAO constructorDao
    @Inject DestructorDAO destructorDao
    @Inject EnumDAO enumDao
    @Inject FieldDAO fieldDao
    @Inject FileDAO fileDao
    @Inject FindingDAO findingDao
    @Inject InitializerDAO initializerDao
    @Inject InterfaceDAO interfaceDao
    @Inject LiteralDAO literalDao
    @Inject PropertyDAO propertyDao
    @Inject MeasureDAO measureDao
    @Inject MethodDAO methodDao
    @Inject MetricDAO metricDao
    @Inject MetricRepositoryDAO metricRepoDao
    @Inject ModuleDAO moduleDao
    @Inject NamespaceDAO namespaceDao
    @Inject PatternChainDAO patternChainDao
    @Inject PatternDAO patternDao
    @Inject PatternInstanceDAO patternInstDao
    @Inject PatternRepositoryDAO patternRepoDao
    @Inject ProjectDAO projectDao
    @Inject RelationDAO relationDao
    @Inject RoleDAO roleDao
    @Inject RuleDAO ruleDao
    @Inject RuleRepositoryDAO ruleRepoDao
    @Inject SystemDAO systemDao
    System system
    Map<String, Namespace> langNS = [:]
    Map<Namespace, List<Type>> langTypes = [:]

    @Override
    System getSystem() {
        system
    }

    @Override
    void setSystem(System sys) {
        if (sys)
            this.system = sys
    }

    @Override
    void addUnknownType(Type node) {
        String key = node.key()
        String name = node.name()
        def group = (key =~ /^(.+):(.+)(\.${name})$/)
        def ns = group[0][2]
        if (langNS[ns]) {
            if (langTypes[langNS[ns]])
                langTypes[langNS[ns]] << node
            else
                langTypes[langNS[ns]] = [node]
        }
    }

    @Override
    Namespace languageNamespace(String s) {
        langNS[s]
    }

    @Override
    void addLanguageNamespace(String s, Namespace namespace) {
        if (s && namespace && !langNS.containsKey(s))
            langNS[s] = namespace
    }

    @Override
    void addGeneralizes(Type from, Type to) {
        addRelation(from, to, RelationType.GENERALIZATION)
    }

    @Override
    List<Type> getGeneralizedFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.GENERALIZATION)
    }

    @Override
    List<Type> getGeneralizedTo(Type to) {
        relationDao.findToRelations(to, RelationType.GENERALIZATION)
    }

    // TODO Fix this
    @Override
    boolean hasGeneralization(Type from, Type to) {
        return false
    }

    @Override
    void addRealizes(Type from, Type to) {
        addRelation(from, to, RelationType.REALIZATION)
    }

    @Override
    List<Type> getRealizedFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.REALIZATION)
    }

    @Override
    List<Type> getRealizedTo(Type to) {
        relationDao.findToRelations(to, RelationType.REALIZATION)
    }

    // TODO Fix this
    @Override
    boolean hasRealization(Type from, Type to) {
        return false
    }

    @Override
    void addAssociation(Type from, Type to, boolean bidirectional) {
        if (bidirectional)
            addRelation(to, from, RelationType.ASSOCIATION)
        addRelation(from, to, RelationType.ASSOCIATION)
    }

    @Override
    List<Type> getAssociatedFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.ASSOCIATION)
    }

    @Override
    List<Type> getAssociatedTo(Type to) {
        relationDao.findToRelations(to, RelationType.ASSOCIATION)
    }

    @Override
    void addAggregation(Type from, Type to, boolean bidirectional) {
        if (bidirectional)
            addRelation(to, from, RelationType.AGGREGATION)
        addRelation(from, to, RelationType.AGGREGATION)
    }

    @Override
    List<Type> getAggregatedFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.AGGREGATION)
    }

    @Override
    List<Type> getAggregatedTo(Type to) {
        relationDao.findToRelations(to, RelationType.AGGREGATION)
    }

    @Override
    void addComposition(Type from, Type to, boolean bidirectional) {
        if (bidirectional)
            addRelation(to, from, RelationType.COMPOSITION)
        addRelation(from, to, RelationType.COMPOSITION)
    }

    @Override
    List<Type> getComposedFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.COMPOSITION)
    }

    @Override
    List<Type> getComposedTo(Type to) {
        relationDao.findToRelations(to, RelationType.COMPOSITION)
    }

    @Override
    void addUse(Type from, Type to) {
        addRelation(from, to, RelationType.USE)
    }

    @Override
    List<Type> getUseFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.USE)
    }

    @Override
    List<Type> getUseTo(Type to) {
        relationDao.findToRelations(to, RelationType.USE)
    }

    @Override
    void addDependency(Type from, Type to) {
        addRelation(from, to, RelationType.DEPENDENCY)
    }

    // TODO Fix this
    @Override
    boolean hasDependency(Type from, Type to) {
        return false
    }

    @Override
    void addContainment(Type from, Type to) {
        addRelation(from, to, RelationType.CONTAINMENT)
    }

    @Override
    List<Type> getDependencyFrom(Type from) {
        relationDao.findFromRelations(from, RelationType.DEPENDENCY)
    }

    @Override
    List<Type> getDependencyTo(Type to) {
        relationDao.findToRelations(to, RelationType.DEPENDENCY)
    }

    @Override
    List<Type> getContainedIn(Type container) {
        relationDao.findFromRelations(container, RelationType.CONTAINMENT)
    }

    @Override
    List<Type> getContainedBy(Type contained) {
        relationDao.findToRelations(contained, RelationType.CONTAINMENT)
    }

    @Override
    List<Type> getTypesUsingMethod(Method method) {
        relationDao.findTypesUsingMember(method)
    }

    @Override
    List<Method> getMethodsCalledFrom(Type type) {
        relationDao.findMethodsCalledFrom(type)
    }

    @Override
    List<Method> getMethodsCalledFrom(Method method) {
        relationDao.<Method>findMembersUsedInMethod(method)
    }

    @Override
    List<Method> getMethodsCallingMethod(Method method) {
        relationDao.findMethodsUsingMember(method)
    }

    @Override
    List<Field> getFieldsUsedBy(Method method) {
        relationDao.findFieldsUsedBy(method)
    }

    @Override
    List<Field> getFieldsUsedBy(Type type) {
        relationDao.findFieldsUsedBy(type)
    }

    @Override
    List<Type> getAllParentClasses(Type type) {
        relationDao.findAllParentClasses(type)
    }

    @Override
    List<Type> getAllDescendentClasses(Type type) {
        relationDao.findAllDescendantClasses(type)
    }

    @Override
    boolean hasBiDirectionalAssociation(Type from, Type to) {
        relationDao.hasBidirectionalAssociation(from, to)
    }

    @Override
    boolean hasContainmentRelation(Type from, Type to) {
        relationDao.hasContainmentRelation(from, to)
    }

    @Override
    boolean hasUniDirectionalAssociation(Type from, Type to) {
        relationDao.hasUniDirectionalAssociation(from, to)
    }

    @Override
    boolean hasUseDependency(Type from, Type to) {
        relationDao.hasUseDependency(from, to)
    }

    @Override
    boolean inheritsFrom(Type type, Type gen) {
        relationDao.hasGeneralization(type, gen)
    }

    @Override
    boolean realizes(Type type, Type real) {
        relationDao.hasRealization(type, real)
    }

    @Override
    List<Method> getMethodUseInSameClass(Method m, Type t) {
        relationDao.findMethodUseInSameClass(m, t)
    }

    @Override
    List<Field> getFieldUseInSameClass(Method m, Type t) {
        relationDao.findFieldUseInSameClass(m, t)
    }

    @Override
    void updateMetricRepository(MetricRepository metricRepository) {
        metricRepoDao.update(metricRepository)
    }

    @Override
    void updatePatternRepository(PatternRepository patternRepository) {
        patternRepoDao.update(patternRepository)
    }

    @Override
    void updatePattern(Pattern pattern) {
        patternDao.update(pattern)
    }

    @Override
    void updateRuleRepository(RuleRepository ruleRepository) {
        ruleRepoDao.update(ruleRepository)
    }

    @Override
    void addRelation(Type from, Type to, RelationType r) {
        Relation rel = Relation.builder()
                .system(system)
                .type(r)
                .src(Reference.builder().refKey(from.key()).refType(RefType.TYPE).create())
                .dest(Reference.builder().refKey(to.key()).refType(RefType.TYPE).create())
                .relKey("${system.sysKey}:${r.toString()}::${from.name()}->${to.name()}")
                .create()
        relationDao.makePersistent(rel)
    }

    @Override
    void addFinding(Finding f) {
        findingDao.makePersistent(f)
    }

    @Override
    Finding getFinding(String key) {
        findingDao.findByKey(key)
    }

    @Override
    void addMeasure(Measure m) {
        measureDao.makePersistent(m)
    }

    @Override
    Measure getMeasure(String key) {
        measureDao.findByKey(key)
    }

    @Override
    void addMetric(Metric m) {
        metricDao.makePersistent(m)
    }

    @Override
    Metric getMetric(String key) {
        metricDao.findByKey(key)
    }

    @Override
    void addMetricRepository(MetricRepository repo) {
        metricRepoDao.makePersistent(repo)
    }

    @Override
    MetricRepository getMetricRepository(String key) {
        metricRepoDao.findByKey(key)
    }

    @Override
    void addRule(Rule r) {
        ruleDao.makePersistent(r)
    }

    @Override
    Rule getRule(String key) {
        ruleDao.findByKey(key)
    }

    @Override
    void addRuleRepository(RuleRepository repo) {
        ruleRepoDao.makePersistent(repo)
    }

    @Override
    RuleRepository getRuleRepository(String key) {
        ruleRepoDao.findByKey(key)
    }

    @Override
    void addPattern(Pattern p) {
        patternDao.makePersistent(p)
    }

    @Override
    Pattern getPattern(String key) {
        patternDao.findByKey(key)
    }

    @Override
    void addPatternChain(PatternChain pc) {
        patternChainDao.makePersistent(pc)
    }

    @Override
    PatternChain getPatternChain(String key) {
        patternChainDao.findByKey(key)
    }

    @Override
    void addPatternInstance(PatternInstance i) {
        patternInstDao.makePersistent(i)
    }

    @Override
    PatternInstance getPatternInstance(String key) {
        patternInstDao.findByKey(key)
    }

    @Override
    void addPatternRepository(PatternRepository repo) {
        patternRepoDao.makePersistent(repo)
    }

    @Override
    PatternRepository getPatternRepository(String key) {
        patternRepoDao.findByKey(key)
    }

    @Override
    void addRole(Role r) {
        roleDao.makePersistent(r)
    }

    @Override
    Role getRole(String key) {
        roleDao.findByKey(key)
    }

    @Override
    void addFile(File f) {
        fileDao.makePersistent(f)
    }

    @Override
    File getFile(String key) {
        fileDao.findByKey(key)
    }

    @Override
    void addModule(Module m) {
        moduleDao.makePersistent(m)
    }

    @Override
    Module getModule(String key) {
        moduleDao.findByKey(key)
    }

    @Override
    void addNamespace(Namespace n) {
        namespaceDao.makePersistent(n)
    }

    @Override
    Namespace getNamespace(String key) {
        namespaceDao.findByKey(key)
    }

    @Override
    void addProject(Project p) {
        projectDao.makePersistent(p)
    }

    @Override
    Project getProject(String key) {
        projectDao.findByKey(key)
    }

    @Override
    void addClass(Class c) {
        classDao.makePersistent(c)
    }

    @Override
    Class getClass(String key) {
        classDao.findByKey(key)
    }

    @Override
    void addEnum(Enum e) {
        enumDao.makePersistent(e)
    }

    @Override
    Enum getEnum(String key) {
        enumDao.findByKey(key)
    }

    @Override
    void addInterface(Interface i) {
        interfaceDao.makePersistent(i)
    }

    @Override
    Interface getInterface(String key) {
        interfaceDao.findByKey(key)
    }

    @Override
    void addEvent(Event e) {
        eventDao.makePersistent(e)
    }

    @Override
    Event getEvent(String key) {
        eventDao.findByKey(key)
    }

    @Override
    void addStruct(Struct s) {
        structDao.makePersistent(s)
    }

    @Override
    Struct getStruct(String key) {
        structDao.findByKey(key)
    }

    @Override
    void addConstructor(Constructor c) {
        constructorDao.makePersistent(c)
    }

    @Override
    Constructor getConstructor(String key) {
        constructorDao.findByKey(key)
    }

    @Override
    void addDestructor(Destructor d) {
        destructorDao.makePersistent(d)
    }

    @Override
    Destructor getDestructor(String key) {
        destructorDao.findByKey(key)
    }

    @Override
    void addField(Field f) {
        fieldDao.makePersistent(f)
    }

    @Override
    Field getField(String key) {
        fieldDao.findByKey(key)
    }

    @Override
    void addInitializer(Initializer i) {
        initializerDao.makePersistent(i)
    }

    @Override
    Initializer getInitializer(String key) {
        initializerDao.findByKey(key)
    }

    @Override
    void addLiteral(Literal l) {
        literalDao.makePersistent(l)
    }

    @Override
    Literal getLiteral(String key) {
        literalDao.findByKey(key)
    }

    @Override
    void addMethod(Method m) {
        methodDao.makePersistent(m)
    }

    @Override
    Method getMethod(String key) {
        methodDao.findByKey(key)
    }

    @Override
    void addProperty(Property p) {
        propertyDao.makePersistent(p)
    }

    @Override
    Property getProperty(String key) {
        propertyDao.findByKey(key)
    }

    @Override
    File findFile(String qid) {
        fileDao.findByKey(qid)
    }

    @Override
    Namespace findNamespace(String identifier) {
        namespaceDao.findByKey(identifier)
    }

    @Override
    List<Namespace> getNamespaces() {
        namespaceDao.findAll()
    }

    @Override
    Method findMethod(String identifier) {
        methodDao.findByKey(identifier)
    }

    @Override
    Module findModule(String qIdentifier) {
        moduleDao.findByKey(qIdentifier)
    }

    // TODO Fix this
    @Override
    Module findModule(Type t) {
        return null
    }

    @Override
    Project findProject(String qid) {
        projectDao.findByKey(qid)
    }

    @Override
    List<Type> findTypes(Module mod) {
        return moduleDao.findTypes(mod)
    }

    @Override
    List<Type> findTypes(Project proj) {
        return projectDao.findTypes(proj)
    }

    @Override
    List<Type> findTypes(PatternInstance inst) {
        patternInstDao.findTypes(inst)
    }

    @Override
    List<Type> findTypes(File file) {
        fileDao.findTypes(file)
    }

    @Override
    List<Type> findTypes(Namespace namespace) {
        namespaceDao.findTypes(namespace)
    }

    @Override
    List<File> getFiles() {
        fileDao.findAll()
    }

    @Override
    List<Method> getMethods() {
        methodDao.findAll()
    }

    @Override
    List<Project> getProjects() {
        projectDao.findAll()
    }

    @Override
    void removeFile(String file) {
        fileDao.makeTransient(fileDao.findByKey(file))
    }

    @Override
    List<Type> getNamespaceClasses(Namespace namespace) {
        namespaceDao.findTypes(namespace)
    }

    @Override
    List<Method> findMethods(Module mod) {
        return moduleDao.findMethods(mod)
    }

    @Override
    List<Method> findMethods(Project proj) {
        return projectDao.findMethods(proj)
    }

    @Override
    List<File> findFiles(Project proj) {
        return projectDao.findFiles(proj)
    }

    @Override
    List<File> findFiles(Module mod) {
        return moduleDao.findFiles(mod)
    }

    // TODO Fix this
    @Override
    List<File> findFiles(Pattern pattern) {
        return null
    }

    @Override
    List<File> findFiles(Namespace namespace) {
        namespaceDao.findFiles(namespace)
    }

    // TODO Fix this
    @Override
    boolean hasNamespace(String key) {
        return false
    }

    // TODO Fix this
    @Override
    Measurable findParent(Measurable decorated) {
        return null
    }

    // TODO Fix this
    @Override
    Project currentProject() {
        return null
    }

    // TODO Fix this
    @Override
    Component findComponentByName(String name) {
        return null
    }

    @Override
    Namespace findNamespace(Type t) {
        switch (t) {
            case Class:
                return classDao.findNamespace((Class) t)
            case Interface:
                return interfaceDao.findNamespace((Interface) t)
            case Event:
                return eventDao.findNamespace((Event) t)
            case Struct:
                return structDao.findNamespace((Struct) t)
        }
        return null
    }

    @Override
    List<Type> getTypes() {
        List<Type> types = []

        types += classDao.findAll()
        types += interfaceDao.findAll()
        types += eventDao.findAll()
        types += structDao.findAll()

        types
    }

    @Override
    File findParentFile(Component codeNode) {
        switch (codeNode) {
            case Class:
                return classDao.findParentFile((Class) codeNode)
            case Interface:
                return interfaceDao.findParentFile((Interface) codeNode)
            case Event:
                return eventDao.findParentFile((Event) codeNode)
            case Struct:
                return structDao.findParentFile((Struct) codeNode)
            case Literal:
                return literalDao.findParentFile((Literal) codeNode)
            case Initializer:
                return initializerDao.findParentFile((Initializer) codeNode)
            case Field:
                return fieldDao.findParentFile((Field) codeNode)
            case Constructor:
                return constructorDao.findParentFile((Constructor) codeNode)
            case Method:
                return methodDao.findParentFile((Method) codeNode)
            case Destructor:
                return destructorDao.findParentFile((Destructor) codeNode)
        }

        return null
    }

    @Override
    Module getModuleForType(Type type) {
        switch (t) {
            case Class:
                return classDao.findModule((Class) t)
            case Interface:
                return interfaceDao.findModule((Interface) t)
            case Event:
                return eventDao.findModule((Event) t)
            case Struct:
                return structDao.findModule((Struct) t)
        }
        return null
    }

    @Override
    List<Type> getModuleClasses(Module mod) {
        moduleDao.findTypes(mod)
    }

    // TODO Fix this
    @Override
    Type findType(String key) {
        return null
    }

    // TODO Fix this
    @Override
    void updateFile(File node) {

    }
}