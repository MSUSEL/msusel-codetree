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
package edu.montana.gsoc.msusel.datamodel

import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.EqualsAndHashCode

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(excludes = ["id", "modifiers"])
abstract class Component implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String compKey
    String name
    @ManyToOne(fetch = FetchType.LAZY)
    Type owner
    int start
    int end
    @Enumerated(EnumType.STRING)
    Accessibility access
    @Embedded
    @Enumerated(EnumType.STRING)
    List<Modifier> modifiers = []

    Component() {

    }

    Component(String key, Type parent, String name, Accessibility access, List<Modifier> modifiers, int start, int end) {
        this.compKey = key
        this.owner = parent
        this.name = name
        this.access = access
        this.modifiers = modifiers
        this.start = start
        this.end = end
    }

    /**
     * Checks whether this line range of this entity contains the provided
     * value.
     *
     * @param line
     *            value to check
     * @return true if the line range of this entity contains the provided line,
     *         false otherwise.
     */
    boolean containsLine(final int line)
    {
        (start..end).contains(line)
    }

    boolean hasModifier(Modifier mod) {
        modifiers.contains(mod)
    }

    void addModifier(Modifier m) {
        if (m && !modifiers.contains(m))
            modifiers << m
    }

    @Override
    String key() {
        compKey
    }

    @Override
    String name() {
        name
    }
}
