package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class SystemSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        System system = new System();
        a(system).shouldBe("valid");
        //a(rule.errors().get("author")).shouldBeEqual("Author must be provided");
        system.set("name", "fake name", "version", "fake version", "language", "fake lang", "sysKey", "fake key");
        a(system).shouldBe("valid");
        system.save();
        system = System.findById(1);
        a(system.getId()).shouldNotBeNull();
        a(system.get("name")).shouldBeEqual("fake name");
        a(system.get("version")).shouldBeEqual("fake version");
        a(system.get("language")).shouldBeEqual("fake lang");
        a(system.get("sysKey")).shouldBeEqual("fake key");
        a(System.count()).shouldBeEqual(1);
    }

    @Test
    public void shouldAddProject() {
        System system = new System();
        system.set("name", "fake name", "version", "fake version", "language", "fake lang", "sysKey", "fake key");

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
        System system = System.createIt("name", "fake name", "version", "fake version", "language", "fake lang", "sysKey", "fake key");
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
        System system = System.createIt("name", "fake name", "version", "fake version", "language", "fake lang", "sysKey", "fake key");
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
}
