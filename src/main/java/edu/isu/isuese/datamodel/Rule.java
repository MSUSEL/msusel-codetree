/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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

import edu.isu.isuese.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Rule extends Model {

    public Rule() {}

    @Builder(buildMethodName = "create")
    public Rule(String key, String name, Priority priority, String description) {
        setKey(key);
        setName(name);
        setPriority(priority);
        setDescription(description);
    }

    public void setPriority(Priority p) {
        set("priority", p.value());
        save();
    }

    public Priority getPriority() {
        int p = (Integer) get("priority");
        return Priority.fromValue(p);
    }

    public RuleRepository getParentRuleRepository() {
        return parent(RuleRepository.class);
    }

    public void setKey(String key)
    {
        set("ruleKey", key);
        save();
    }

    public String getKey() {
        return getString("ruleKey");
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public void setDescription(String desc) {
        set("description", desc);
        save();
    }

    public String getDescription() {
        return getString("description");
    }

    public void addTag(Tag tag) {
        if (tag != null)
            add(tag);
        save();
    }

    public void removeTag(Tag tag) {
        if (tag != null)
            remove(tag);
        save();
    }

    public List<Tag> getTags() {
        return getAll(Tag.class);
    }

    public void addFinding(Finding finding) {
        if (finding != null)
            add(finding);
        save();
    }

    public void removeFinding(Finding finding) {
        if (finding != null)
            remove(finding);
        save();
    }

    public List<Finding> getFindings() {
        return getAll(Finding.class);
    }

    public boolean hasFindingOn(Component comp) {
        return getFindings().stream().anyMatch(finding -> {
            List<Reference> refs = finding.getReferences();
            if (refs.isEmpty() || comp == null) {
                return false;
            } else {
                if (refs.get(0).getRefKey() == null) {
                    return false;
                }
                return refs.get(0).getRefKey().equals(comp.getRefKey());
            }
        });
    }
}
