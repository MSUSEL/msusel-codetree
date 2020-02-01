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

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("scms")
public class SCM extends Model {

    public SCM() {}

    @Builder(buildMethodName = "create")
    public SCM(String name, String key, String tag, String branch, String url, SCMType type) {
        set("name", name, "scmKey", key);
        if (tag != null && !tag.isEmpty())
            set("tag", tag);
        if (branch != null && !branch.isEmpty())
            set("branch", branch);
        setType(type);
        setURL(url);
    }

    public String getSCMKey() {
        return getString("scmKey");
    }

    public String getTag() {
        return getString("tag");
    }

    public void setTag(String tag) {
        set("tag", tag);
        save();
    }

    public String getBranch() {
        return getString("branch");
    }

    public void setBranch(String branch) {
        set("branch", branch);
        save();
    }

    public String getURL() {
        return getString("url");
    }

    public void setURL(String url) {
        set("url", url);
        save();
    }

    public SCMType getType() {
        if (get("type") == null) return null;
        else return SCMType.fromValue(getInteger("type"));
    }

    public void setType(SCMType type) {
        if (type == null) set("type", null);
        else set("type", type.value());
        save();
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public List<System> getParentSystems() {
        return DbUtils.getParentSystem(this.getClass(), (Integer) getId());
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        projects.add(parent(Project.class));
        return projects;
    }
}
