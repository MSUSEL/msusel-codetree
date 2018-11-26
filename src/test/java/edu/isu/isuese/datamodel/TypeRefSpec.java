package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class TypeRefSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        TypeRef typeRef = new TypeRef();
        a(typeRef).shouldBe("valid");
//        //a(typeRef.errors().get("author")).shouldBeEqual("Author must be provided");
        typeRef.set("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        a(typeRef).shouldBe("valid");
        typeRef.save();
        typeRef = TypeRef.findById(1);
        a(typeRef.getId()).shouldNotBeNull();
        a(typeRef.get("typeName")).shouldBeEqual("typeRef");
        a(typeRef.get("dimensions")).shouldBeNull();
        a(typeRef.get("type")).shouldBeEqual(TypeRefType.Type.value());
        a(typeRef.getType()).shouldBeEqual(TypeRefType.Type);
        a(TypeRef.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddTypeArgs() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        typeRef.addTypeArg(typeRef2);

        a(typeRef.getTypeArgs().size()).shouldBeEqual(1);
        a(typeRef.getBounds().size()).shouldBeEqual(0);
    }

    @Test
    public void canRemoveTypeArgs() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        typeRef.addTypeArg(typeRef2);
        typeRef = TypeRef.findById(1);
        typeRef.remove(typeRef2);

        a(typeRef.getTypeArgs().size()).shouldBeEqual(0);
        a(typeRef.getBounds().size()).shouldBeEqual(0);
    }

    @Test
    public void canAddBounds() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        typeRef.addBound(typeRef2);

        a(typeRef.getTypeArgs().size()).shouldBeEqual(0);
        a(typeRef.getBounds().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveBounds() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        typeRef.addBound(typeRef2);
        typeRef = TypeRef.findById(1);
        typeRef.remove(typeRef2);

        a(typeRef.getTypeArgs().size()).shouldBeEqual(0);
        a(typeRef.getBounds().size()).shouldBeEqual(0);
    }

    @Test
    public void canAddReference() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);

        typeRef.add(ref);

        a(typeRef.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveReference() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);

        typeRef.add(ref);
        typeRef = TypeRef.findById(1);
        typeRef.remove(ref);

        a(typeRef.getAll(Reference.class).size()).shouldBeEqual(0);
    }

    @Test
    public void onlyAddsOneReference() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        Reference ref = Reference.createIt("refKey", "ref");
        Reference ref2 = Reference.createIt("refKey", "ref2");

        typeRef.setReference(ref);
        a(typeRef.getAll(Reference.class).size()).shouldBeEqual(1);
        typeRef.setReference(ref2);
        a(typeRef.getAll(Reference.class).size()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        typeRef.save();

        TypeRef typeRef2 = TypeRef.createIt("typeName", "typeRef2", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        TypeRef typeRef3 = TypeRef.createIt("typeName", "typeRef3", "dimensions", null);
        typeRef.setType(TypeRefType.TypeVar);
        typeRef.save();

        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);

        typeRef.addBound(typeRef2);
        typeRef.addTypeArg(typeRef3);
        typeRef.add(ref);

        a(TypeRef.count()).shouldBeEqual(3);
        a(Reference.count()).shouldBeEqual(1);

        typeRef.delete(true);

        a(TypeRef.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }
}
