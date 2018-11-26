package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class LanguageSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Language lang = new Language();
        a(lang).shouldBe("valid");
//        //a(lang.errors().get("author")).shouldBeEqual("Author must be provided");
        lang.set("name", "lang");
        a(lang).shouldBe("valid");
        lang.save();
        lang = Language.findById(1);
        a(lang.getId()).shouldNotBeNull();
        a(lang.get("name")).shouldBeEqual("lang");
        a(Language.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Language lang = Language.createIt("name", "lang");

        lang.delete();

        a(Language.count()).shouldBeEqual(0);
    }
}
