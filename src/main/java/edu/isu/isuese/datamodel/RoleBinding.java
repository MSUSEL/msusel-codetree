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
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RoleBinding extends Model {

    public static RoleBinding of(Role role, Reference ref) {
        RoleBinding binding = new RoleBinding();
        binding.saveIt();
        binding.setRoleRefPair(role, ref);
        return binding;
    }

    public Role getRole() { return getRoles().get(0); }

    public List<Role> getRoles() { return getAll(Role.class); }

    private List<Reference> getRefs() { return Lists.newLinkedList(getAll(Reference.class)); }

    public void setRoleRefPair(Role role, Reference ref) {
        List<Role> roles = getRoles();
        if (!roles.isEmpty()) {
            for (Role r : roles) {
                remove(r);
            }
        }

        List<Reference> refs = getRefs();
        if (!refs.isEmpty()) {
            for (Reference r : refs) {
                remove(r);
            }

        }

        save();
        add(role);
        add(ref);
        save();
    }

    public Reference getReference() {
        return getAll(Reference.class).get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoleBinding) {
            RoleBinding role = (RoleBinding) o;
            return role.getId().equals(this.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    public RoleBinding copy(String oldPrefix, String newPrefix) {
        return RoleBinding.of(this.getRole(), this.getReference().copy(oldPrefix, newPrefix));
    }
}
