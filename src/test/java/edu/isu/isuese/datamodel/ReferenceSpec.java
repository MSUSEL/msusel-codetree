package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

import java.sql.Ref;

public class ReferenceSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Reference ref = new Reference();
        a(ref).shouldBe("valid");
//        //a(ref.errors().get("author")).shouldBeEqual("Author must be provided");
        ref.set("refKey", "ref");
        ref.setType(RefType.TYPE);
        a(ref).shouldBe("valid");
        ref.save();
        ref = Reference.findById(1);
        a(ref.getId()).shouldNotBeNull();
        a(ref.get("refKey")).shouldBeEqual("ref");
        a(ref.get("type")).shouldBeEqual(RefType.TYPE.value());
        a(ref.getType()).shouldBeEqual(RefType.TYPE);
        a(Reference.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        ref.save();

        a(Reference.count()).shouldBeEqual(1);
        ref.delete();
        a(Reference.count()).shouldBeEqual(0);
    }
}
