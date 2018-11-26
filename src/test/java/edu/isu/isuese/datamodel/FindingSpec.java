package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class FindingSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Finding finding = new Finding();
        a(finding).shouldBe("valid");
//        //a(finding.errors().get("author")).shouldBeEqual("Author must be provided");
        finding.set("findingKey", "finding");
        a(finding).shouldBe("valid");
        finding.save();
        finding = Finding.findById(1);
        a(finding.getId()).shouldNotBeNull();
        a(finding.get("findingKey")).shouldBeEqual("finding");
        a(Finding.count()).shouldBeEqual(1);
    }

    @Test
    public void canSetReference() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        finding.add(ref);
        finding.save();

        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void onlyAddsOneReference() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        Reference ref2 = Reference.createIt("refKey", "ref2");

        finding.setReference(ref);
        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
        finding.setReference(ref2);
        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        finding.add(ref);
        finding.save();

        a(Finding.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        finding.delete(true);
        a(Finding.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }
}
