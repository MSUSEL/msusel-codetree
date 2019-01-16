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
package edu.isu.isuese.datamodel.util;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.*;
import edu.isu.isuese.datamodel.Class;
import edu.isu.isuese.datamodel.Enum;
import edu.isu.isuese.datamodel.System;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class DbUtilsSpec extends DBSpec {

    private System system;
    private PatternRepository patternRepo;
    private MetricRepository metricRepo;
    private RuleRepository ruleRepo;
    private Type type;
    private Finding finding;
    private Measure meas;
    private Role role;
    private PatternInstance instance;
    private Project p;

    private void populateDb() {
        system = System.createIt("name", "fake name", "version", "fake version", "language", "fake lang", "sysKey", "fake key");
        p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        SCM scm2 = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        Language lang = Language.createIt("name", "lang");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Module module2 = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns", "name", "ns");
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        File file2 = File.createIt("fileKey", "fileKey", "name", "file");
        Import imp = Import.createIt("name", "imp");
        Import imp2 = Import.createIt("name", "imp");
        type = Class.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        Type type2 = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type2.setAccessibility(Accessibility.PUBLIC);
        Type type3 = Interface.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type3.setAccessibility(Accessibility.PUBLIC);
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        Member member2 = Initializer.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member2.setAccessibility(Accessibility.PUBLIC);
        member2.addModifier(Modifier.Values.STATIC.name());
        Method method = Method.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        Method method2 = Constructor.createIt("name", "TestClass2", "start", 1, "end", 100, "compKey", "TestClass");
        method2.setAccessibility(Accessibility.PUBLIC);
        Method method3 = Destructor.createIt("name", "TestClass3", "start", 1, "end", 100, "compKey", "TestClass");
        method3.setAccessibility(Accessibility.PUBLIC);
        Parameter param = Parameter.createIt("name", "param");
        Parameter param2 = Parameter.createIt("name", "param");
        Parameter param3 = Parameter.createIt("name", "param");
        Field field = Field.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        field.setAccessibility(Accessibility.PUBLIC);
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        metricRepo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
        meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        ruleRepo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag tag = Tag.createIt("tag", "Tag");
        finding = Finding.createIt("findingKey", "finding");
        patternRepo = PatternRepository.createIt("repoKey", "repo", "name", "repo");
        PatternChain chain = PatternChain.createIt("chainKey", "chain");
        instance = PatternInstance.createIt("instKey", "inst");
        RoleBinding binding = RoleBinding.createIt();
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        role = Role.createIt("roleKey", "role", "name", "role");
        Reference ref = Reference.createIt("refKey", "ref");
        Relation rel = Relation.createIt("relKey", "rel");
        Reference to = Reference.createIt("refKey", "to");
        to.setType(RefType.TYPE);
        Reference from = Reference.createIt("refKey", "from");
        from.setType(RefType.TYPE);

        system.addProject(p);
        p.addModule(module);
        p.addModule(module2);
        p.addSCM(scm);
        p.addSCM(scm2);
        p.addLanguage(lang);
        p.addFinding(finding);
        p.addMeasure(meas);
        p.addRelation(rel);
        module.addNamespace(ns);
        module.addNamespace(ns2);
        ns.addFile(file);
        ns.addFile(file2);
        file.addImport(imp);
        file.addImport(imp2);
        file.addType(type);
        file.addType(type2);
        file.addType(type3);
        type2.addMember(member);
        type.addMember(member2);
        type.addMember(method);
        type.addMember(method2);
        type.addMember(method3);
        method.addParameter(param);
        method2.addParameter(param2);
        method3.addParameter(param3);
        type.addMember(field);
        method.setReturnType(typeRef);
        field.setType(typeRef);
        metricRepo.addMetric(metric);
        metric.addMeasure(meas);
        ruleRepo.addRule(rule);
        rule.add(finding);
        rule.add(tag);
        patternRepo.addPattern(pattern);
        pattern.addRole(role);
        pattern.addInstance(instance);
        chain.addInstance(instance);
        instance.addRoleBinding(binding);
        binding.setRoleRefPair(role, ref);
        system.addPatternChain(chain);
        rel.setToAndFromRefs(to, from);
        p.addPatternInstance(instance);

        a(system.getPatternChains().size()).shouldNotBeEqual(0);
        a(p.getPatternInstances().size()).shouldNotBeEqual(0);
    }

    @Test
    public void testLoadProperties() {
    }

    @Test
    public void testVerifyProperties() {
    }

    @Test
    public void testOpenDbConnection() {
    }

    @Test
    public void testCloseDbConnection() {
    }

    @Test
    public void testCreateAscendingJoin() {
        try {
            String sql = DbUtils.createAscendingJoin(Namespace.class, System.class, 1);
            a(sql).shouldBeEqual("SELECT * FROM systems " +
                    "JOIN projects ON systems.id = projects.system_id " +
                    "JOIN modules ON projects.id = modules.project_id " +
                    "JOIN namespaces ON modules.id = namespaces.module_id " +
                    "AND namespaces.id = 1;");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateDescendingJoin() {
        try {
            String sql = DbUtils.createDescendingJoin(System.class, Class.class, 1);
            a(sql).shouldBeEqual("SELECT * FROM classes " +
                    "JOIN files ON classes.file_id = files.id " +
                    "JOIN namespaces ON files.namespace_id = namespaces.id " +
                    "JOIN modules ON namespaces.module_id = modules.id " +
                    "JOIN projects ON modules.project_id = projects.id " +
                    "JOIN systems ON projects.system_id = 1;");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAppendFilters() {

    }

    @Test
    public void testGetSCMs() {
        populateDb();
        List<SCM> scms = DbUtils.getSCMs(System.class, (Integer) system.getId());

        the(scms.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetModules() {
        populateDb();
        List<Module> modules = DbUtils.getModules(System.class, (Integer) system.getId());

        the(modules.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetNamespaces() {
        populateDb();
        List<Namespace> nss = DbUtils.getNamespaces(System.class, (Integer) system.getId());

        the(nss.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetFiles() {
        populateDb();
        List<File> files = DbUtils.getFiles(System.class, (Integer) system.getId());

        the(files.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetImports() {
        populateDb();
        List<Import> imports = DbUtils.getImports(System.class, (Integer) system.getId());

        the(imports.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetTypes() {
        populateDb();
        List<Type> types = DbUtils.getTypes(System.class, (Integer) system.getId());
        for (Type t : types)
            java.lang.System.out.println(t);
        for (Model e : Enum.findAll())
            java.lang.System.out.println(e);
        for (Model e : Interface.findAll())
            java.lang.System.out.println(e);
        the(types.size()).shouldBeEqual(3);
    }

    @Test
    public void testGetClasses() {
        populateDb();
        List<Class> classes = DbUtils.getClasses(System.class, (Integer) system.getId());

        the(classes.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetInterfaces() {
        populateDb();
        List<Interface> interfaces = DbUtils.getInterfaces(System.class, (Integer) system.getId());

        the(interfaces.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetEnums() {
        populateDb();
        List<Enum> enums = DbUtils.getEnums(System.class, (Integer) system.getId());

        the(enums.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetMembers() {
        populateDb();
        List<Member> members = DbUtils.getMembers(System.class, (Integer) system.getId());

        the(members.size()).shouldBeEqual(6);
    }

    @Test
    public void testGetLiterals() {
        populateDb();
        List<Literal> literals = DbUtils.getLiterals(System.class, (Integer) system.getId());

        the(literals.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetInitializers() {
        populateDb();
        List<Initializer> initializers = DbUtils.getInitializers(System.class, (Integer) system.getId());

        the(initializers.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetTypedMembers() {
        populateDb();
        List<TypedMember> typedMembers = DbUtils.getTypedMembers(System.class, (Integer) system.getId());

        the(typedMembers.size()).shouldBeEqual(4);
    }

    @Test
    public void testGetFields() {
        populateDb();
        List<Field> fields = DbUtils.getFields(System.class, (Integer) system.getId());

        the(fields.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetAllMethods() {
        populateDb();
        List<Method> allMethods = DbUtils.getAllMethods(System.class, (Integer) system.getId());

        the(allMethods.size()).shouldBeEqual(3);
    }

    @Test
    public void testGetMethods() {
        populateDb();
        List<Method> allMethods = DbUtils.getMethods(System.class, (Integer) system.getId());

        the(allMethods.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetConstructors() {
        populateDb();
        List<Constructor> constructors = DbUtils.getConstructors(System.class, (Integer) system.getId());

        the(constructors.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetDestructors() {
        populateDb();
        List<Destructor> destructors = DbUtils.getDestructors(System.class, (Integer) system.getId());

        the(destructors.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetPatternInstancesFromSystem() {
        populateDb();
        List<PatternInstance> insts = DbUtils.getPatternInstances(System.class, (Integer) system.getId());

        the(insts.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetPatternInstancesFromRepository() {
        populateDb();
        List<PatternInstance> insts = DbUtils.getPatternInstances(PatternRepository.class, (Integer) patternRepo.getId());

        the(insts.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetLanguages() {
        populateDb();
        List<Language> languages = DbUtils.getLanguages(System.class, (Integer) system.getId());

        the(languages.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetMeasuresFromSystem() {
        populateDb();
        List<Measure> measures = DbUtils.getMeasures(System.class, (Integer) system.getId());

        the(measures.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetMeasuresFromRepository() {
        populateDb();
        List<Measure> measures = DbUtils.getMeasures(MetricRepository.class, (Integer) metricRepo.getId());

        the(measures.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetRoleBindings() {
        populateDb();
        List<RoleBinding> bindings = DbUtils.getRoleBindings(System.class, (Integer) system.getId());

        the(bindings.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetFindingsFromSystem() {
        populateDb();
        List<Finding> findings = DbUtils.getFindings(System.class, (Integer) system.getId());

        the(findings.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetFindingsFromRepository() {
        populateDb();
        List<Finding> findings = DbUtils.getFindings(RuleRepository.class, (Integer) ruleRepo.getId());

        the(findings.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetRelations() {
        populateDb();
        List<Relation> relations = DbUtils.getRelations(System.class, (Integer) system.getId());

        the(relations.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetTags() {
        populateDb();
        List<Tag> tags = DbUtils.getTags(RuleRepository.class, (Integer) ruleRepo.getId());

        the(tags.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetRolesFromSystem() {
        populateDb();
        List<Role> roles = DbUtils.getRoles(System.class, (Integer) system.getId());

        the(roles.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetRolesFromPatternRepository() {
        populateDb();
        List<PatternRepository> repos = PatternRepository.findAll();
        List<Role> roles = Lists.newLinkedList();
        for (PatternRepository repo : repos) {
            roles.addAll(patternRepo.getRoles());
        }

        the(roles.size()).shouldBeEqual(2);
    }

    @Test
    public void testGetParentSystem() {
        populateDb();
        List<System> systems = DbUtils.getParentSystem(Class.class, (Integer) type.getId());

        the(systems.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentProject() {
        populateDb();
        List<Project> parentProject = DbUtils.getParentProject(Class.class, (Integer) type.getId());

        the(parentProject.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentModule() {
        populateDb();
        List<Module> parentModule = DbUtils.getParentModule(Class.class, (Integer) type.getId());

        the(parentModule.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentNamespace() {
        populateDb();
        List<Module> parentModule = DbUtils.getParentModule(Class.class, (Integer) type.getId());

        the(parentModule.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentFile() {
        populateDb();
        List<Module> parentModule = DbUtils.getParentModule(Class.class, (Integer) type.getId());

        the(parentModule.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentPatternChain() {
        populateDb();
        List<PatternChain> parentChain = DbUtils.getParentPatternChain(Role.class, (Integer) role.getId());

        the(parentChain.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentPatternRepositoryPatternInstance() {
        populateDb();
        List<PatternRepository> parentRepo = DbUtils.getParentPatternRepository(PatternInstance.class, (Integer) instance.getId());

        the(parentRepo.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentPatternRepositoryFromRole() {
        populateDb();
        List<PatternRepository> parentRepo = DbUtils.getParentPatternRepository(Role.class, (Integer) role.getId());

        the(parentRepo.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentPatternInstance() {
        populateDb();
        List<PatternInstance> parentInstance = DbUtils.getParentPatternInstance(Role.class, (Integer) role.getId());

        the(parentInstance.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentMetricRepository() {
        populateDb();
        List<MetricRepository> metricRepos = DbUtils.getParentMetricRepository(Measure.class, (Integer) meas.getId());

        the(metricRepos.size()).shouldBeEqual(1);
    }

    @Test
    public void testGetParentRuleRepository() {
        populateDb();
        List<RuleRepository> ruleRepos = DbUtils.getParentRuleRepository(Finding.class, (Integer) finding.getId());

        the(ruleRepos.size()).shouldBeEqual(1);
    }
}
