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

import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "files", "children"])
class Namespace implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String nsKey
    String name
    @OneToMany(mappedBy = "namespace")
    List<File> files = []
    @OneToMany(mappedBy = "parent")
    List<Namespace> children = []
    @ManyToOne(fetch = FetchType.LAZY)
    Structure container
    @ManyToOne(fetch = FetchType.LAZY)
    Namespace parent

    Namespace plus(File file) {
        addFile(file)
        this
    }

    Namespace leftShift(File file) {
        addFile(file)
        this
    }

    Namespace minus(File file) {
        removeFile(file)
        this
    }

    Namespace plus(Namespace ns) {
        addChild(ns)
        this
    }

    Namespace leftShift(Namespace ns) {
        addChild(ns)
        this
    }

    Namespace minus(Namespace ns) {
        removeChild(ns)
        this
    }

    void addFile(File file) {
        if (file && !files.contains(file)) {
            files << file
            file.namespace = this
        }
    }

    void removeFile(File file) {
        if (file && files.contains(file)) {
            files -= file
            file.namespace = null
        }
    }

    void addChild(Namespace ns) {
        if (ns && !children.contains(ns)) {
            children << ns
            ns.parent = this
        }
    }

    void removeChild(Namespace ns) {
        if (ns && children.contains(ns)) {
            children -= ns
            ns.parent = null
        }
    }

    List<Type> types() {
        List<Type> types = []

        files.each {
            types += it.getTypes()
        }
        children.each {
            types += it.types()
        }

        types
    }

    def namespaces() {
        children
    }

    List<File> files() {
        List<File> f = []
        f += files
        children.each {
            f += it.files()
        }

        f
    }

    def getPlantUML() {
        StringBuilder builder = new StringBuilder()
        builder.append("package ${this.getSimpleName()} {")

        builder.append("}")
    }

    boolean hasFile(String key) {
        files.find { it.fileKey == key } != null
    }

    boolean containsNamespace(Namespace current) {
        children.find { it == current } != null
    }

    Type findTypeByName(String name) {
        types().find { it.name == name }
    }

    @Override
    String key() {
        nsKey
    }

    @Override
    String name() {
        name
    }
}
