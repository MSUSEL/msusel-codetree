package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class SCMSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        SCM scm = new SCM();
        a(scm).shouldBe("valid");
//        //a(scm.errors().get("author")).shouldBeEqual("Author must be provided");
        scm.set("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        a(scm).shouldBe("valid");
        scm.save();
        scm = SCM.findById(1);
        a(scm.getId()).shouldNotBeNull();
        a(scm.get("scmKey")).shouldBeEqual("scm");
        a(scm.get("tag")).shouldBeEqual("1.0");
        a(scm.get("branch")).shouldBeEqual("dev");
        a(scm.get("url")).shouldBeEqual("git@git.somewhere.com/what");
        a(SCM.count()).shouldBeEqual(1);
    }

    @Test
    public void canSetType() {
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        scm.save();

        a(scm.getType()).shouldNotBeNull();
    }

    @Test
    public void canRemoveType() {
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        scm.save();

        scm.setType(null);

        a(scm.getType()).shouldBeNull();
    }

    @Test
    public void deleteHandlesCorrectly() {
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        scm.save();

        scm.delete();

        a(SCM.count()).shouldBeEqual(0);
    }
}
