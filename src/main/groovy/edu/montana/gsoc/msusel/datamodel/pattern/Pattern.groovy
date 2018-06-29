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
package edu.montana.gsoc.msusel.datamodel.pattern

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Table(name = "Patterns")
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "instances", "roles"])
@ToString(ignoreNulls = true, includePackage = false, excludes = ["id", "roles", "instances"])
class Pattern {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id
    String patternKey
    String name
    @OneToMany(mappedBy = "pattern")
    List<Role> roles = []
    @OneToMany(mappedBy = "pattern")
    List<PatternInstance> instances = []
    @ManyToOne(fetch = FetchType.LAZY)
    PatternRepository repository

    Pattern plus(Role r) {
        addRole(r)
        this
    }

    Pattern leftShift(Role r) {
        addRole(r)
        this
    }

    Pattern minus(Role r) {
        removeRole(r)
        this
    }

    void addRole(Role r) {
        if (!roles)
            roles = []
        if (r && !roles.contains(r)) {
            roles << r
            r.pattern = this
        }
    }

    void removeRole(Role r) {
        if (!roles)
            roles = []

        if (r && roles.contains(r)) {
            roles -= r
            r.pattern = null
        }
    }

    Pattern plus(PatternInstance i) {
        addInstance(i)
        this
    }

    Pattern leftShift(PatternInstance i) {
        addInstance(i)
        this
    }

    Pattern minus(PatternInstance i) {
        removeInstance(i)
        this
    }

    void addInstance(PatternInstance i) {
        if (!instances)
            instances = []
        if (i && !instances.contains(i)) {
            instances << i
            i.pattern = this
        }
    }

    void removeInstance(PatternInstance i) {
        if (!instances)
            instances = []

        if (i && instances.contains(i)) {
            instances -= i
            i.pattern = null
        }
    }
}
