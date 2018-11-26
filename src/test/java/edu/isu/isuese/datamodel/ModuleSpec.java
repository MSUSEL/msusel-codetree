package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ModuleSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Module module = new Module();
        a(module).shouldBe("valid");
//        //a(module.errors().get("author")).shouldBeEqual("Author must be provided");
        module.set("moduleKey", "module", "name", "module");
        a(module).shouldBe("valid");
        module.save();
        module = Module.findById(1);
        a(module.getId()).shouldNotBeNull();
        a(module.get("name")).shouldBeEqual("module");
        a(module.get("moduleKey")).shouldBeEqual("module");
        a(Module.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddNamespace() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        a(module.getAll(Namespace.class).size()).shouldBeEqual(0);
        module.add(ns);
        a(module.getAll(Namespace.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveNamespace() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        module.add(ns);

        module = Module.findById(1);
        module.remove(ns);

        a(module.getAll(Namespace.class).isEmpty()).shouldBeTrue();
    }

    @Test
    public void deleteHandlesCorrectly() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        module.add(ns);
        module.delete(true);

        a(Module.count()).shouldBeEqual(0);
        a(Namespace.count()).shouldBeEqual(0);
    }
}
