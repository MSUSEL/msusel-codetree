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

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class MeasureSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Measure meas = new Measure();
        a(meas).shouldBe("valid");
//        //a(meas.errors().get("author")).shouldBeEqual("Author must be provided");
        meas.set("measureKey", "meas", "value", 1.0);
        a(meas).shouldBe("valid");
        meas.save();
        meas = Measure.findById(1);
        a(meas.getId()).shouldNotBeNull();
        a(meas.get("measureKey")).shouldBeEqual("meas");
        a(meas.get("value")).shouldBeEqual(1.0);
        a(Measure.count()).shouldBeEqual(1);
    }

    @Test
    public void canSetReference() {
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);

        meas.add(ref);
        a(meas.getAll(Reference.class).size()).shouldBeEqual(1);
        meas.remove(ref);
        a(meas.getAll(Reference.class).size()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }

    @Test
    public void onlyAddsOneReference() {
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        Reference ref = Reference.createIt("refKey", "ref");
        Reference ref2 = Reference.createIt("refKey", "ref2");

        meas.setReference(ref);
        a(meas.getAll(Reference.class).size()).shouldBeEqual(1);
        meas.setReference(ref2);
        a(meas.getAll(Reference.class).size()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        meas.add(ref);

        a(Measure.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        meas.delete(true);
        a(Measure.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }
}
