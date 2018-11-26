package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RelationSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Relation rel = new Relation();
        a(rel).shouldBe("valid");
//        //a(rel.errors().get("author")).shouldBeEqual("Author must be provided");
        rel.set("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        a(rel).shouldBe("valid");
        rel.save();
        rel = Relation.findById(1);
        a(rel.getId()).shouldNotBeNull();
        a(rel.get("relKey")).shouldBeEqual("rel");
        a(rel.get("type")).shouldBeEqual(RelationType.ASSOCIATION.value());
        a(rel.getType()).shouldBeEqual(RelationType.ASSOCIATION);
        a(Relation.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddReferencePair() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(rel.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveReferencePair() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(rel.getAll(Reference.class).size()).shouldBeEqual(1);
        rel = Relation.findById(1);
        rel.remove(ref);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(0);
    }

    @Test
    public void addingToAndFromRefsWorks() {
        Relation rel = Relation.createIt("relKey", "rel");
        Reference to = Reference.createIt("refKey", "to");
        to.setType(RefType.TYPE);
        Reference from = Reference.createIt("refKey", "from");
        from.setType(RefType.TYPE);

        rel.setToAndFromRefs(to, from);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(2);

        Reference to2 = Reference.createIt("refKey", "to2");
        to.setType(RefType.TYPE);
        Reference from2 = Reference.createIt("refKey", "from2");
        from.setType(RefType.TYPE);
        rel.setToAndFromRefs(to2, from2);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(2);
        a(Reference.count()).shouldBeEqual(2);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(Relation.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        rel.delete(true);
        a(Relation.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }
}
