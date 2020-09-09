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
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Pattern extends Model {

    public Pattern() {}

    @Builder(buildMethodName = "create")
    public Pattern(String name, String key, String family) {
        if (name != null && !name.isEmpty()) setName(name);
        if (key != null && !key.isEmpty()) setString("patternKey", key);
        if (family != null && !family.isEmpty()) setFamily(family);
        save();
    }

    public String getPatternKey() { return getString("patternKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addInstance(PatternInstance inst) { add(inst); save(); }

    public void removeInstance(PatternInstance inst) { remove(inst); save(); }

    public List<PatternInstance> getInstances() { return getAll(PatternInstance.class); }

    public void addRole(Role role) { add(role); save(); }

    public void removeRole(Role role) { remove(role); save(); }

    public List<Role> getRoles() { return getAll(Role.class); }

    public Role getRoleByName(String name) {
        try {
            return get(Role.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public PatternRepository getParentPatternRepository() {
        return parent(PatternRepository.class);
    }

    public String getFamily() { return getString("family"); }

    public void setFamily(String family) { setString("family", family); save(); }

    public List<Role> mandatoryRoles() {
        List<Role> roles = Lists.newArrayList();
        getRoles().forEach(role -> {
            if (role.isMandatory())
                roles.add(role);
        });
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pattern) {
            Pattern role = (Pattern) o;
            return role.getPatternKey().equals(this.getPatternKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPatternKey());
    }
}
