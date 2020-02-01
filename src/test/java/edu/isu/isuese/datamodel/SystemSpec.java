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

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

import java.util.List;

public class SystemSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        System system = System.builder().create();
        a(system).shouldBe("valid");
        //a(rule.errors().get("author")).shouldBeEqual("Author must be provided");
        system.set("name", "fake name", "sysKey", "fake key");
        a(system).shouldBe("valid");
        system.save();
        system = System.findById(1);
        a(system.getId()).shouldNotBeNull();
        a(system.get("name")).shouldBeEqual("fake name");
        a(system.get("sysKey")).shouldBeEqual("fake key");
        a(System.count()).shouldBeEqual(1);
    }

    @Test
    public void shouldAddProject() {
        System system = System.builder().create();
        system.set("name", "fake name", "sysKey", "fake key");

        system.save();
        a(System.count()).shouldBeEqual(1);

        Project p = new Project();
        p.set("name", "fake project", "projKey", "fake key", "version", "fake version");
        system.addProject(p);

        a(Project.count()).shouldBeEqual(1);
        a(system.getProjects().size()).shouldBeEqual(1);

        system.removeProject(p);
        a(system.getProjects().isEmpty()).shouldBeTrue();
    }

    @Test
    public void shouldAddPatternChain() {
        System system = System.createIt("name", "fake name", "sysKey", "fake key");
        system.save();
        a(System.count()).shouldBeEqual(1);

        PatternChain p = PatternChain.createIt("chainKey", "fake key");
        system.addPatternChain(p);
        system.save();

        a(PatternChain.count()).shouldBeEqual(1);
        a(system.getPatternChains().size()).shouldBeEqual(1);

        system.removePatternChain(p);
        a(system.getPatternChains().isEmpty()).shouldBeTrue();
    }

    @Test
    public void deleteHandlesCorrectly() {
        System system = System.createIt("name", "fake name", "sysKey", "fake key");
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        PatternChain pc = PatternChain.createIt("chainKey", "fake key");
        system.addProject(p);
        system.addPatternChain(pc);
        system.save();

        a(System.count()).shouldBeEqual(1);
        a(Project.count()).shouldBeEqual(1);
        a(PatternChain.count()).shouldBeEqual(1);
        system.delete(true);
        a(System.count()).shouldBeEqual(0);
        a(Project.count()).shouldBeEqual(0);
        a(PatternChain.count()).shouldBeEqual(0);
    }

    @Test
    public void findAllTypes() {
        System system = System.createIt("name", "fake name", "sysKey", "fake key");
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        Type type = Class.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        Type type2 = Class.createIt("name", "TestClass2", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        system.addProject(p);
        p.addModule(module);
        module.addNamespace(ns);
        ns.addFile(file);
        file.addType(type);

        List<Project> projs = system.getProjects();
        a(projs.size()).shouldBeEqual(1);

        List<Module> mods = system.getModules();
        a(mods.size()).shouldBeEqual(1);

        List<Namespace> nss = system.getNamespaces();
        a(nss.size()).shouldBeEqual(1);

        List<File> files = system.getFiles();
        a(files.size()).shouldBeEqual(1);

        List<Class> classes = Class.findBySQL("SELECT * FROM classes " +
                "JOIN files ON classes.file_id = files.id " +
                "JOIN namespaces ON files.namespace_id = namespaces.id " +
                "JOIN modules ON namespaces.module_id = modules.id " +
                "JOIN projects ON modules.project_id = projects.id " +
                "JOIN systems ON projects.system_id = systems.id;");
        a(classes.size()).shouldBeEqual(1);

        List<System> systems = System.findBySQL("SELECT * FROM systems " +
                "JOIN projects ON systems.id = projects.system_id " +
                "JOIN modules ON projects.id = modules.project_id " +
                "JOIN namespaces ON modules.id = namespaces.module_id " +
                "JOIN files ON namespaces.id = 1;");
        a(systems.size()).shouldBeEqual(1);
    }

    @Test
    public void checkProjectAssociations() {
        java.lang.System.out.println(Project.associations());
        a(Project.associations().size()).shouldBeEqual(9);
    }
}
