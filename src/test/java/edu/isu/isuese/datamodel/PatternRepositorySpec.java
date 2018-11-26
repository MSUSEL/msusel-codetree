package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class PatternRepositorySpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        PatternRepository repo = new PatternRepository();
        a(repo).shouldBe("valid");
//        //a(repo.errors().get("author")).shouldBeEqual("Author must be provided");
        repo.set("repoKey", "repo", "name", "repo");
        a(repo).shouldBe("valid");
        repo.save();
        repo = PatternRepository.findById(2);
        a(repo.getId()).shouldNotBeNull();
        a(repo.get("name")).shouldBeEqual("repo");
        a(repo.get("repoKey")).shouldBeEqual("repo");
        a(PatternRepository.count()).shouldBeEqual(2);
    }

    @Test
    public void canAddPattern() {
        PatternRepository repo = PatternRepository.createIt("repoKey", "repo", "name", "repo");
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        repo.addPattern(pattern);
        repo.save();

        a(repo.getPatterns().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePattern() {
        PatternRepository repo = PatternRepository.createIt("repoKey", "repo", "name", "repo");
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        repo.addPattern(pattern);
        repo.save();

        a(repo.getPatterns().size()).shouldBeEqual(1);
        repo = PatternRepository.findById(2);
        repo.remove(pattern);
        a(repo.getPatterns().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        PatternRepository repo = PatternRepository.createIt("repoKey", "repo", "name", "repo");
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        repo.addPattern(pattern);
        repo.save();

        a(PatternRepository.count()).shouldBeEqual(2);
        a(Pattern.count()).shouldBeEqual(24);
        repo.delete(true);
        a(PatternRepository.count()).shouldBeEqual(1);
        a(Pattern.count()).shouldBeEqual(23);
    }
}
