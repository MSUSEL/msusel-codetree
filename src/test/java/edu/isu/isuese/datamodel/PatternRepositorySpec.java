/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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