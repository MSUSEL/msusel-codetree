package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class UnknownTypeSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Type type = new UnknownType();
        a(type).shouldBe("valid");
//        //a(type.errors().get("author")).shouldBeEqual("Author must be provided");
        type.set("name", "TestUnknownType", "start", 1, "end", 100, "compKey", "TestUnknownType");
        type.setAccessibility(Accessibility.PUBLIC);
        a(type).shouldBe("valid");
        type.save();
        type = UnknownType.findById(1);
        a(type.getId()).shouldNotBeNull();
        a(type.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(type.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(type.get("name")).shouldBeEqual("TestUnknownType");
        a(type.get("compKey")).shouldBeEqual("TestUnknownType");
        a(type.get("start")).shouldBeEqual(1);
        a(type.get("end")).shouldBeEqual(100);
        a(UnknownType.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Type type = UnknownType.createIt("name", "TestUnknownType", "start", 1, "end", 100, "compKey", "TestUnknownType");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        type.delete();
        a(UnknownType.count()).shouldBeEqual(0);
    }
}
