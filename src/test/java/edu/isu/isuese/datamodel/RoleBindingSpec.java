package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RoleBindingSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        RoleBinding binding = new RoleBinding();
        a(binding).shouldBe("valid");
//        //a(binding.errors().get("author")).shouldBeEqual("Author must be provided");
        binding.set();
        a(binding).shouldBe("valid");
        binding.save();
        binding = RoleBinding.findById(1);
        a(binding.getId()).shouldNotBeNull();
        a(RoleBinding.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddRoleRefPair() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);

        Reference ref2 = Reference.createIt("refKey", "ref2");
        Role role2 = Role.createIt("roleKey", "role2", "name", "role");
        binding.setRoleRefPair(role2, ref2);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRoleRefPair() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);

        binding = RoleBinding.findById(1);
        binding.remove(ref);
        binding.remove(role);
        a(binding.getAll(Role.class).size()).shouldBeEqual(0);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(RoleBinding.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        a(Role.count()).shouldBeEqual(1);
        binding.delete(true);
        a(RoleBinding.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
        a(Role.count()).shouldBeEqual(0);
    }
}
