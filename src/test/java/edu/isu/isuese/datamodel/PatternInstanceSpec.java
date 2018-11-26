package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class PatternInstanceSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        PatternInstance inst = new PatternInstance();
        a(inst).shouldBe("valid");
//        //a(inst.errors().get("author")).shouldBeEqual("Author must be provided");
        inst.set("instKey", "inst");
        a(inst).shouldBe("valid");
        inst.save();
        inst = PatternInstance.findById(1);
        a(inst.getId()).shouldNotBeNull();
        a(inst.get("instKey")).shouldBeEqual("inst");
        a(PatternInstance.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddRoleBinding() {
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        RoleBinding binding = RoleBinding.createIt();
        inst.addRoleBinding(binding);

        a(inst.getRoleBindings().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRoleBinding() {
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        RoleBinding binding = RoleBinding.createIt();
        inst.addRoleBinding(binding);

        a(inst.getRoleBindings().size()).shouldBeEqual(1);
        inst = PatternInstance.findById(1);
        inst.removeRoleBinding(binding);
        a(inst.getRoleBindings().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        RoleBinding binding = RoleBinding.createIt();
        inst.addRoleBinding(binding);

        a(PatternInstance.count()).shouldBeEqual(1);
        a(RoleBinding.count()).shouldBeEqual(1);
        inst.delete(true);
        a(PatternInstance.count()).shouldBeEqual(0);
        a(RoleBinding.count()).shouldBeEqual(0);
    }
}
