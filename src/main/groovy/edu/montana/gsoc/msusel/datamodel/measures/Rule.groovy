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
package edu.montana.gsoc.msusel.datamodel.measures

import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "findings"])
class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String ruleKey
    @OneToMany(mappedBy = "rule")
    List<Finding> findings
    @ManyToOne(fetch = FetchType.LAZY)
    RuleRepository repository
    String name
    String description
    @Enumerated(EnumType.STRING)
    Priority priority
    @Embedded
    List<Tag> tags

    Rule plus(Finding f) {
        addFinding(f)
        this
    }

    Rule minus(Finding f) {
        removeFinding(f)
        this
    }

    Rule leftShift(Finding f) {
        addFinding(f)
        this
    }

    void addFinding(Finding f) {
        if (f && !findings.contains(f)) {
            findings << f
            f.rule = this
        }
    }

    void removeFinding(Finding f) {
        if (f && findings.contains(f)) {
            findings -= f
            f.rule = null
        }
    }

    Rule plus(Tag t) {
        addTag(t)
        this
    }

    Rule minus(Tag t) {
        removeTag(t)
        this
    }

    Rule leftShift(Tag t) {
        addTag(t)
        this
    }

    void addTag(Tag t) {
        if (f && !tags.contains(t)) {
            tags << t
        }
    }

    void removeTag(Tag t) {
        if (f && tags.contains(t)) {
            tags -= t
        }
    }
}
