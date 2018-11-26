package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class FileSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        File file = new File();
        a(file).shouldBe("valid");
//        //a(file.errors().get("author")).shouldBeEqual("Author must be provided");
        file.set("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        a(file).shouldBe("valid");
        file.save();
        file = File.findById(1);
        a(file.getId()).shouldNotBeNull();
        a(file.get("type")).shouldBeEqual(FileType.SOURCE.value());
        a(file.getType()).shouldBeEqual(FileType.SOURCE);
        a(file.get("name")).shouldBeEqual("file");
        a(file.get("fileKey")).shouldBeEqual("fileKey");
        a(File.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddImport() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        file.addImport(imp);
        a(file.getAll(Import.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveImport() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        file.addImport(imp);
        file = File.findById(1);
        file.removeImport(imp);

        a(file.getAll(Import.class).isEmpty()).shouldBeTrue();
    }

    @Test
    public void canAddType() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        a(file.getType()).shouldNotBeNull();
    }

    @Test
    public void canRemoveType() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        file.setType(null);

        a(file.getType()).shouldBeNull();
    }

    @Test
    public void deleteHandlesCorrectly() {
        File file = File.createIt("fileKey", "fileKey", "name", "file");
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        Type type = Class.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        file.add(imp);
        file.add(type);

        file.delete(true);

        a(File.count()).shouldBeEqual(0);
        a(Class.count()).shouldBeEqual(0);
        a(Import.count()).shouldBeEqual(0);
    }
}
