package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ImportSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Import imp = new Import();
        a(imp).shouldBe("valid");
//        //a(imp.errors().get("author")).shouldBeEqual("Author must be provided");
        imp.set("name", "imp");
        a(imp).shouldBe("valid");
        imp.save();
        imp = Import.findById(1);
        a(imp.getId()).shouldNotBeNull();
        a(imp.get("name")).shouldBeEqual("imp");
        a(Import.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Import imp = Import.createIt("name", "imp");

        imp.delete();

        a(Import.count()).shouldBeEqual(0);
    }
}
