package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RuleRepositorySpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        RuleRepository repo = new RuleRepository();
        a(repo).shouldBe("valid");
        //a(repo.errors().get("author")).shouldBeEqual("Author must be provided");
        repo.set("repoKey", "key", "name", "name");
        a(repo).shouldBe("valid");
        repo.save();
        repo = RuleRepository.findById(1);
        a(repo.getId()).shouldNotBeNull();
        a(repo.get("repoKey")).shouldBeEqual("key");
        a(repo.get("name")).shouldBeEqual("name");
        a(RuleRepository.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddRule() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(repo.getAll(Rule.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRule() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(repo.getAll(Rule.class).size()).shouldBeEqual(1);
        repo = RuleRepository.findById(1);
        repo.remove(rule);
        a(repo.getAll(Rule.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(RuleRepository.count()).shouldBeEqual(1);
        a(Rule.count()).shouldBeEqual(1);
        repo.delete(true);
        a(RuleRepository.count()).shouldBeEqual(0);
        a(Rule.count()).shouldBeEqual(0);
    }
}
