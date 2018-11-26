package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class LiteralSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Member member = new Literal();
        a(member).shouldBe("valid");
//        //a(member.errors().get("author")).shouldBeEqual("Author must be provided");
        member.set("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        a(member).shouldBe("valid");
        member.save();
        member = Literal.findById(1);
        a(member.getId()).shouldNotBeNull();
        a(member.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(member.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(member.get("name")).shouldBeEqual("TestClass");
        a(member.get("compKey")).shouldBeEqual("TestClass");
        a(member.get("start")).shouldBeEqual(1);
        a(member.get("end")).shouldBeEqual(100);
        a(Literal.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddModifier() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        member.save();

        a(Literal.findById(1).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        member.save();

        member = Literal.findById(1);
        member.removeModifier(Modifier.Values.STATIC.name());
        a(member.getAll(Modifier.class).size()).shouldBeEqual(0);
        a(LiteralsModifiers.count()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        member.delete();

        a(Literal.count()).shouldBeEqual(0);
    }
}
