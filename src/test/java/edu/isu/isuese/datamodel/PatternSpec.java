package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class PatternSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Pattern pattern = new Pattern();
        a(pattern).shouldBe("valid");
//        //a(pattern.errors().get("author")).shouldBeEqual("Author must be provided");
        pattern.set("patternKey", "pattern", "name", "pattern");
        a(pattern).shouldBe("valid");
        pattern.save();
        pattern = Pattern.findById(24);
        a(pattern.getId()).shouldNotBeNull();
        a(pattern.get("name")).shouldBeEqual("pattern");
        a(pattern.get("patternKey")).shouldBeEqual("pattern");
        a(Pattern.count()).shouldBeEqual(24);
    }

    @Test
    public void canAddPatternInstance() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(pattern.getInstances().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePatternInstance() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(pattern.getInstances().size()).shouldBeEqual(1);
        pattern = Pattern.findById(24);
        pattern.remove(inst);
        a(pattern.getInstances().size()).shouldBeEqual(0);
    }

    @Test
    public void canAddRole() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        pattern.save();

        a(pattern.getRoles().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRole() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        pattern.save();

        a(pattern.getRoles().size()).shouldBeEqual(1);
        pattern = Pattern.findById(24);
        pattern.removeRole(role);
        a(pattern.getRoles().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(Pattern.count()).shouldBeEqual(24);
        a(Role.count()).shouldBeEqual(1);
        a(PatternInstance.count()).shouldBeEqual(1);
        pattern.delete(true);
        a(Pattern.count()).shouldBeEqual(23);
        a(Role.count()).shouldBeEqual(0);
        a(PatternInstance.count()).shouldBeEqual(0);
    }
}
