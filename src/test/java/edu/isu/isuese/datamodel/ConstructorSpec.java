package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ConstructorSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Method method = new Constructor();
        a(method).shouldBe("valid");
//        //a(method.errors().get("author")).shouldBeEqual("Author must be provided");
        method.set("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        a(method).shouldBe("valid");
        method.save();
        method = Constructor.findById(1);
        a(method.getId()).shouldNotBeNull();
        a(method.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(method.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(method.get("name")).shouldBeEqual("TestClass");
        a(method.get("compKey")).shouldBeEqual("TestClass");
        a(method.get("start")).shouldBeEqual(1);
        a(method.get("end")).shouldBeEqual(100);
        a(Constructor.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddParam() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.save();

        Parameter param = Parameter.createIt("name", "param");
        method.add(param);

        a(Parameter.count()).shouldBeEqual(1);
        a(method.getAll(Parameter.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveParam() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.save();

        Parameter param = Parameter.createIt("name", "param");
        method.add(param);

        method = Constructor.findById(1);
        method.remove(param);

        a(Parameter.count()).shouldBeEqual(0);
        a(method.getAll(Parameter.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canSetReturnType() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.save();

        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        method.setReturnType(typeRef);

        a(method.getAll(TypeRef.class).size()).shouldBeEqual(1);

        typeRef = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        method.setReturnType(typeRef);

        a(method.getAll(TypeRef.class).size()).shouldBeEqual(1);
        a(TypeRef.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddModifier() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.addModifier(Modifier.Values.STATIC.name());
        method.save();

        a(Constructor.findById(1).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.addModifier(Modifier.Values.STATIC.name());
        method.save();

        method = Constructor.findById(1);
        method.removeModifier(Modifier.Values.STATIC.name());
        a(method.getAll(Modifier.class).size()).shouldBeEqual(0);
    }

    @Test
    public void onlyAddsOneTypeRef() {
        TypedMember method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);

        method.setType(typeRef);
        a(method.getAll(TypeRef.class).size()).shouldBeEqual(1);
        method.setType(typeRef2);
        a(method.getAll(TypeRef.class).size()).shouldBeEqual(1);
        a(TypeRef.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Method method = Constructor.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        method.save();

        Parameter param = Parameter.createIt("name", "param");
        method.add(param);

        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        method.setReturnType(typeRef);

        method.delete(true);
        a(Parameter.count()).shouldBeEqual(0);
        a(ConstructorsModifiers.count()).shouldBeEqual(0);
        a(TypeRef.count()).shouldBeEqual(0);
    }
}
