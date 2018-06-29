/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
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
package edu.montana.gsoc.msusel.datamodel.pattern

import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import edu.montana.gsoc.msusel.datamodel.structural.Project
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "project", "bindings"])
class PatternInstance implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String instKey
    @ManyToOne(fetch = FetchType.LAZY)
    Project project
    @ManyToOne(fetch = FetchType.LAZY)
    Pattern pattern
    @OneToMany(mappedBy = "instance")
    List<RoleBinding> bindings = []

    PatternInstance leftShift(RoleBinding binding) {
        addRoleBinding(binding)
        this
    }

    PatternInstance plus(RoleBinding binding) {
        addRoleBinding(binding)
        this
    }

    PatternInstance minus(RoleBinding binding) {
        removeRoleBinding(binding)
    }

    void addRoleBinding(RoleBinding binding) {
        if (binding && !bindings.contains(binding)) {
            bindings << binding
            binding.instance = this
        }
    }

    void removeRoleBinding(RoleBinding binding) {
        if (binding && bindings.contains(binding)) {
            bindings -= binding
            binding.instance = null
        }
    }

    @Override
    String key() {
        instKey
    }

    @Override
    String name() {
        instKey
    }
}
