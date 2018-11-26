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
