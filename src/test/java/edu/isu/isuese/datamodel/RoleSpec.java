package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RoleSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Role role = new Role();
        a(role).shouldBe("valid");
//        //a(role.errors().get("author")).shouldBeEqual("Author must be provided");
        role.set("roleKey", "role", "name", "role");
        a(role).shouldBe("valid");
        role.save();
        role = Role.findById(1);
        a(role.getId()).shouldNotBeNull();
        a(role.get("name")).shouldBeEqual("role");
        a(role.get("roleKey")).shouldBeEqual("role");
        a(Role.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Role role = Role.createIt("roleKey", "role", "name", "role");

        a(Role.count()).shouldBeEqual(1);
        role.delete();
        a(Role.count()).shouldBeEqual(0);
    }
}
