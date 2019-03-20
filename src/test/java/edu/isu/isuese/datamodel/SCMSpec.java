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
