package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class TagSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Tag t = new Tag();
//        a(t).shouldNotBe("valid");
//        a(t.errors().get(""));
        t.set("tag", "tag");
        a(t).shouldBe("valid");
        t.save();
        a(t.getId()).shouldNotBeNull();
        a(t.get("tag")).shouldBeEqual("tag");
        a(Tag.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Tag t = Tag.createIt("tag", "tag");

        a(Tag.count()).shouldBeEqual(1);
        t.delete();
        a(Tag.count()).shouldBeEqual(0);
    }
}