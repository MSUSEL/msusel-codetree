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

import edu.montana.gsoc.msusel.datamodel.measures.*
import edu.montana.gsoc.msusel.datamodel.member.*
import edu.montana.gsoc.msusel.datamodel.pattern.*
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.structural.*
import edu.montana.gsoc.msusel.datamodel.type.*

interface DataModelMediator {

    void addRelation(Type from, Type to, RelationType r)

    void addGeneralizes(Type from, Type to)

    List<Type> getGeneralizedFrom(Type from)

    List<Type> getGeneralizedTo(Type to)

    boolean hasGeneralization(Type from, Type to)

    void addRealizes(Type from, Type to)

    List<Type> getRealizedFrom(Type from)

    List<Type> getRealizedTo(Type to)

    boolean hasRealization(Type from, Type to)

    void addAssociation(Type from, Type to, boolean bidirectional)

    List<Type> getAssociatedFrom(Type from)

    List<Type> getAssociatedTo(Type to)

    void addAggregation(Type from, Type to, boolean bidirectional)

    List<Type> getAggregatedFrom(Type from)

    List<Type> getAggregatedTo(Type to)

    void addComposition(Type from, Type to, boolean bidirectional)

    List<Type> getComposedFrom(Type from)

    List<Type> getComposedTo(Type to)

    void addUse(Type from, Type to)

    List<Type> getUseFrom(Type from)

    List<Type> getUseTo(Type to)

    void addDependency(Type from, Type to)

    boolean hasDependency(Type from, Type to)

    void addContainment(Type contained, Type container)

    List<Type> getDependencyFrom(Type from)

    List<Type> getDependencyTo(Type to)

    List<Type> getContainedIn(Type container)

    List<Type> getContainedBy(Type contained)

    List<Type> getTypesUsingMethod(Method method)

    List<Method> getMethodsCalledFrom(Type type)

    List<Method> getMethodsCalledFrom(Method method)

    List<Method> getMethodsCallingMethod(Method method)

    List<Field> getFieldsUsedBy(Method method)

    List<Field> getFieldsUsedBy(Type type)

    List<Type> getAllParentClasses(Type type)

    List<Type> getAllDescendentClasses(Type type)

    void addUnknownType(Type node)

    boolean hasBiDirectionalAssociation(Type from, Type to)

    boolean hasContainmentRelation(Type from, Type to)

    boolean hasUniDirectionalAssociation(Type from, Type to)

    boolean hasUseDependency(Type from, Type to)

    Namespace languageNamespace(String s)

    void addLanguageNamespace(String s, Namespace namespaceNode)

    boolean inheritsFrom(Type type, Type gen)

    boolean realizes(Type type, Type real)

    List<Method> getMethodUseInSameClass(Method m, Type t)

    List<Field> getFieldUseInSameClass(Method m, Type t)

    void addFinding(Finding f)

    Finding getFinding(String key)

    void addMeasure(Measure m)

    Measure getMeasure(String key)

    void addMetric(Metric m)

    Metric getMetric(String key)

    void addMetricRepository(MetricRepository repo)

    MetricRepository getMetricRepository(String key)

    void addRule(Rule r)

    Rule getRule(String key)

    void addRuleRepository(RuleRepository repo)

    RuleRepository getRuleRepository(String key)

    void addPattern(Pattern p)

    Pattern getPattern(String key)

    void addPatternChain(PatternChain pc)

    PatternChain getPatternChain(String key)

    void addPatternInstance(PatternInstance i)

    PatternInstance getPatternInstance(String key)

    void addPatternRepository(PatternRepository repo)

    PatternRepository getPatternRepository(String key)

    void addRole(Role r)

    Role getRole(String key)

    void addFile(File f)

    /**
     * Searches the DataModelMediator for a File whose qualified identifier matches
     * the one provided.
     *
     * @param key
     *            Qualified Identifier
     * @return File with matching qualified identifier to the one provided,
     *         or null if no such File exists in the DataModelMediator or the
     *         provided identifier is null or empty.
     */
    File getFile(String key)

    void addModule(Module m)

    Module getModule(String key)

    void addNamespace(Namespace n)

    Namespace getNamespace(String key)

    void addProject(Project p)

    Project getProject(String key)

    void addClass(Class c)
    
    Class getClass(String key)

    void addEnum(Enum e)

    Enum getEnum(String key)

    void addInterface(Interface i)
    
    Interface getInterface(String key)
    
    void addEvent(Event e)
    
    Event getEvent(String key)
    
    void addStruct(Struct s)
    
    Struct getStruct(String key)
    
    void addConstructor(Constructor c)
    
    Constructor getConstructor(String key)
    
    void addDestructor(Destructor d)

    Destructor getDestructor(String key)
    
    void addField(Field f)

    Field getField(String key)
    
    void addInitializer(Initializer i)

    Initializer getInitializer(String key)
    
    void addLiteral(Literal l)

    Literal getLiteral(String key)
    
    void addMethod(Method m)

    Method getMethod(String key)
    
    void addProperty(Property p)

    Property getProperty(String key)

    void setSystem(System sys)

    System getSystem()

    void updateMetricRepository(MetricRepository metricRepository)

    void updatePatternRepository(PatternRepository patternRepository)

    void updatePattern(Pattern pattern)

    void updateRuleRepository(RuleRepository ruleRepository)

    /**
     * Searches for a file with the given qualified identifier in this DataModelMediator.
     *
     * @param qid
     *            Qualified identifier
     * @return The File matching the given identifier, or null if the given
     *         identifier is null or empty or if no such file exists in the
     *         model.
     */
    File findFile(final String qid)

    Namespace findNamespace(Type t)

    /**
     * Retrieves the namespace in the model with the given identifier
     *
     * @param identifier Identifier of the namespace to find
     * @return The namespace in the associated model, with the given identifier. If no such namespace exists or if the provided identifier is null or the empty string, then null is returned
     */
    Namespace findNamespace(final String identifier)

    /**
     * @return The set of all namespaces within the model.
     */
    List<Namespace> getNamespaces()

    /**
     * Searches for a Method with the given qualified identifier.
     *
     * @param identifier
     *            Qualified Identifier
     * @return Method with matching qualified identifier, or null if the
     *         identifier is null, empty, or no such Method exists.
     */
    Method findMethod(final String identifier)

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
    Module findModule(String qIdentifier)

    Module findModule(Type t)

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
    Project findProject(String qid)

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
    Type findType(final String key)

    List<Type> findTypes(Structure struct)

    List<Type> findTypes(PatternInstance inst)

    List<Type> findTypes(File file)

    List<Type> findTypes(Namespace namespace)

    /**
     * @return The set of all files within the model.
     */
    List<File> getFiles()

    /**
     * @return The set of all methods within the model.
     */
    List<Method> getMethods()

    /**
     * @return The set of all projects within the model (including the root
     *         project)
     */
    List<Project> getProjects()

    /**
     * @return The set of all known types in the model.
     */
    List<Type> getTypes()

    /**
     * Finds the file with the given identifier and removes it from its parent
     * in the DataModelMediator.
     *
     * @param file
     *            Qualified Identifier of the file to be removed.
     */
    void removeFile(final String file)

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
    void updateFile(final File node)

    File findParentFile(Component codeNode)

    List<Type> getNamespaceClasses(Namespace namespace)

    Module getModuleForType(Type type)

    List<Type> getModuleClasses(Module mod)

    List<Method> findMethods(Structure struct)

    List<File> findFiles(Structure structure)

    List<File> findFiles(Pattern pattern)

    List<File> findFiles(Namespace namespace)

    boolean hasNamespace(String key)

    Measurable findParent(Measurable decorated)

    Project currentProject()

    Component findComponentByName(String name)
}