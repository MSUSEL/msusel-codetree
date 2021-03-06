package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ProjectSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Project proj = new Project();
        a(proj).shouldBe("valid");
//        //a(proj.errors().get("author")).shouldBeEqual("Author must be provided");
        proj.set("projKey", "proj", "name", "proj", "version", "1.0");
        a(proj).shouldBe("valid");
        proj.save();
        proj = Project.findById(1);
        a(proj.getId()).shouldNotBeNull();
        a(proj.get("name")).shouldBeEqual("proj");
        a(proj.get("projKey")).shouldBeEqual("proj");
        a(proj.get("version")).shouldBeEqual("1.0");
        a(Project.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddPatternInstance() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        p.addPatternInstance(inst);

        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePatternInstance() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        p.addPatternInstance(inst);

        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removePatternInstance(inst);
        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddMeasure() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        p.addMeasure(meas);

        a(p.getAll(Measure.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveMeasure() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        p.addMeasure(meas);

        a(p.getAll(Measure.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeMeasure(meas);
        a(p.getAll(Measure.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddFinding() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Finding finding = Finding.createIt("findingKey", "finding");
        p.addFinding(finding);

        a(p.getAll(Finding.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveFinding() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Finding finding = Finding.createIt("findingKey", "finding");
        p.addFinding(finding);

        a(p.getAll(Finding.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeFinding(finding);
        a(p.getAll(Finding.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddRelation() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Relation rel = Relation.createIt("relKey", "rel");
        p.addRelation(rel);

        a(p.getAll(Relation.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRelation() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Relation rel = Relation.createIt("relKey", "rel");
        p.addRelation(rel);

        a(p.getAll(Relation.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeRelation(rel);
        a(p.getAll(Relation.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canSetSCM() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        p.addSCM(scm);

        a(p.getAll(SCM.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canUnSetSCM() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        p.addSCM(scm);

        a(p.getAll(SCM.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeSCM(scm);
        a(p.getAll(SCM.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddModule() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        p.addModule(module);

        a(p.getAll(Module.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModule() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        p.addModule(module);

        a(p.getAll(Module.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeModule(module);
        a(p.getAll(Module.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddLanguage() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Language lang = Language.createIt("name", "lang");
        p.addLanguage(lang);

        a(p.getAll(Language.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveLanguage() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Language lang = Language.createIt("name", "lang");
        p.addLanguage(lang);

        a(p.getAll(Language.class).size()).shouldBeEqual(1);
        p = Project.findById(1);
        p.removeLanguage(lang);
        a(p.getAll(Language.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        Relation rel = Relation.createIt("relKey", "rel");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        Finding finding = Finding.createIt("findingKey", "finding");
        Language lang = Language.createIt("name", "lang");

        p.addLanguage(lang);
        p.addModule(module);
        p.addSCM(scm);
        p.addRelation(rel);
        p.addPatternInstance(inst);
        p.addMeasure(meas);
        p.addFinding(finding);

        a(Module.count()).shouldBeEqual(1);
        a(SCM.count()).shouldBeEqual(1);
        a(Relation.count()).shouldBeEqual(1);
        a(PatternInstance.count()).shouldBeEqual(1);
        a(Measure.count()).shouldBeEqual(1);
        a(Finding.count()).shouldBeEqual(1);
        a(Language.count()).shouldBeEqual(1);
        a(ProjectsLanguages.count()).shouldBeEqual(1);
        p.delete(true);
        a(Module.count()).shouldBeEqual(0);
        a(SCM.count()).shouldBeEqual(0);
        a(Relation.count()).shouldBeEqual(0);
        a(PatternInstance.count()).shouldBeEqual(0);
        a(Measure.count()).shouldBeEqual(0);
        a(Finding.count()).shouldBeEqual(0);
        a(ProjectsLanguages.count()).shouldBeEqual(0);
        a(Language.count()).shouldBeEqual(0);
    }
}
