package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ParameterSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Parameter param = new Parameter();
        a(param).shouldBe("valid");
//        //a(param.errors().get("author")).shouldBeEqual("Author must be provided");
        param.set("name", "param");
        a(param).shouldBe("valid");
        param.save();
        param = Parameter.findById(1);
        a(param.getId()).shouldNotBeNull();
        a(param.get("name")).shouldBeEqual("param");
        a(Parameter.count()).shouldBeEqual(1);
    }

    @Test
    public void canSetType() {
        Parameter param = Parameter.createIt("name", "param");
        param.save();

        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        param.setType(typeRef);

        a(param.getAll(TypeRef.class).size()).shouldBeEqual(1);

        typeRef = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        param.setType(typeRef);

        a(param.getAll(TypeRef.class).size()).shouldBeEqual(1);
        a(TypeRef.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddModifier() {
        Parameter param = Parameter.createIt("name", "param");
        param.addModifier(Modifier.Values.STATIC.name());
        param.save();

        a(param.findById(1).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Parameter param = Parameter.createIt("name", "param");
        param.addModifier(Modifier.Values.STATIC.name());
        param.save();

        param = Parameter.findById(1);
        param.removeModifier(Modifier.Values.STATIC.name());
        a(param.getAll(Modifier.class).size()).shouldBeEqual(0);
        a(ParametersModifiers.count()).shouldBeEqual(0);
    }

    @Test
    public void onlyAddsOneTypeRef() {
        Parameter param = Parameter.createIt("name", "param");
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);

        param.setType(typeRef);
        a(param.getAll(TypeRef.class).size()).shouldBeEqual(1);
        param.setType(typeRef2);
        a(param.getAll(TypeRef.class).size()).shouldBeEqual(1);
        a(TypeRef.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Parameter param = Parameter.createIt("name", "param");
        param.save();

        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        param.setType(typeRef);

        param.delete(true);
        a(Parameter.count()).shouldBeEqual(0);
        a(ParametersModifiers.count()).shouldBeEqual(0);
        a(TypeRef.count()).shouldBeEqual(0);
    }
}
