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
package edu.montana.gsoc.msusel.datamodel.structural

import edu.montana.gsoc.msusel.datamodel.System
import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import groovy.transform.EqualsAndHashCode

import javax.persistence.*
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(excludes = ["id", "namespaces", "system"])
abstract class Structure implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String structKey
    String name
    @ManyToOne(fetch = FetchType.LAZY)
    System system
    @OneToMany(mappedBy = "container")
    List<Namespace> namespaces = []

    Structure(String key, String name, System system, List<Namespace> namespaces) {
        this.structKey = key
        this.name = name
        this.system = system
        this.namespaces = namespaces
    }

    Structure() {
        namespaces = []
    }

    File findFile(String key) {
        (File) files().find { it.key == key }
    }

    void removeFile(String s) {
        children.remove(findFile(s))
    }

    @Override
    String key() {
        structKey
    }

    @Override
    String name() {
        name
    }
}
