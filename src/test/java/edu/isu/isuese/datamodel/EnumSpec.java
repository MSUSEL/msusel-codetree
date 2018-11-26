package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class EnumSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Type type = new Enum();
        a(type).shouldBe("valid");
//        //a(type.errors().get("author")).shouldBeEqual("Author must be provided");
        type.set("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        a(type).shouldBe("valid");
        type.save();
        type = Enum.findById(1);
        a(type.getId()).shouldNotBeNull();
        a(type.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(type.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(type.get("name")).shouldBeEqual("TestClass");
        a(type.get("compKey")).shouldBeEqual("TestClass");
        a(type.get("start")).shouldBeEqual(1);
        a(type.get("end")).shouldBeEqual(100);
        a(Enum.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddMembers() {
        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);
        a(member.getId()).shouldNotBeNull();
    }

    @Test
    public void canRemoveMembers() {
        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);
        a(type.getAll(Literal.class).size()).shouldBeEqual(1);

        type.remove(member);
        a(type.getAll(Literal.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddModifier() {
        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.addModifier(Modifier.Values.STATIC.name());
        type.save();

        a(Enum.findById(1).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.addModifier(Modifier.Values.STATIC.name());
        type.save();

        type = Enum.findById(1);
        type.removeModifier(Modifier.Values.STATIC.name());
        a(type.getAll(Modifier.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();
        type.addModifier(Modifier.Values.STATIC.name());

        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);

        type.delete(true);
        a(Literal.count()).shouldBeEqual(0);
        a(EnumsModifiers.count()).shouldBeEqual(0);
    }

    @Test
    public void parentsWorksCorrectly() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        Type type = Enum.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Type type2 = Enum.createIt("name", "TestClass2", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        type.add(type2);
        file.add(type);
        file.add(type2);

        a(file.getAll(Enum.class).size()).shouldBeEqual(2);
        a(type.getAll(Enum.class).size()).shouldBeEqual(1);
    }
}
