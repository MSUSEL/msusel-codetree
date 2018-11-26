package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RuleSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Rule rule = new Rule();
        a(rule).shouldBe("valid");
        //a(rule.errors().get("author")).shouldBeEqual("Author must be provided");
        rule.set("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        a(rule).shouldBe("valid");
        rule.save();
        rule = Rule.findById(1);
        a(rule.getId()).shouldNotBeNull();
        a(rule.get("priority")).shouldBeEqual(4);
        a(rule.getPriority()).shouldBeEqual(Priority.HIGH);
        a(rule.get("ruleKey")).shouldBeEqual("fake key");
        a(rule.get("name")).shouldBeEqual("fake name");
        a(rule.get("description")).shouldBeEqual("fake content");
        a(rule.get("rule_repository_id")).shouldBeEqual(1);
        a(Rule.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddTag() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(rule.getAll(Tag.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveTag() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(rule.getAll(Tag.class).size()).shouldBeEqual(1);
        rule = Rule.findById(1);
        rule.remove(t);
        a(rule.getAll(Tag.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddFinding() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        rule.save();

        a(rule.getAll(Finding.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveFinding() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        rule.save();

        a(rule.getAll(Finding.class).size()).shouldBeEqual(1);
        rule = Rule.findById(1);
        rule.remove(finding);
        a(rule.getAll(Finding.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canSetPriority() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();

        a(rule.get("priority")).shouldBeEqual(Priority.HIGH.value());
        a(rule.getPriority()).shouldBeEqual(Priority.HIGH);

        rule.setPriority(Priority.LOW);
        rule.save();

        a(rule.get("priority")).shouldBeEqual(Priority.LOW.value());
        a(rule.getPriority()).shouldBeEqual(Priority.LOW);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(Rule.count()).shouldBeEqual(1);
        a(Finding.count()).shouldBeEqual(1);
        a(Tag.count()).shouldBeEqual(1);
        rule.delete(true);
        a(Rule.count()).shouldBeEqual(0);
        a(Finding.count()).shouldBeEqual(0);
        a(Tag.count()).shouldBeEqual(0);
    }
}