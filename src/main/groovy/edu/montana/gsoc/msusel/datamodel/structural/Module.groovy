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
package edu.montana.gsoc.msusel.datamodel.structural

import edu.montana.gsoc.msusel.datamodel.System
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.builder.Builder

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
class Module extends Structure {

    @ManyToOne(fetch = FetchType.LAZY)
    Project project

    @Builder(buildMethodName = "create")
    Module(String key, String name, System system, List<Namespace> namespaces, Project project) {
        super(key, name, system, namespaces)
        this.project = project
    }

    Module() {
        super()
    }

    Module plus(Namespace ns) {
        addNamespace(ns)
        this
    }

    Module minus(Namespace ns) {
        removeNamespace(ns)
        this
    }

    Module leftShift(Namespace ns) {
        addNamespace(ns)
        this
    }

    void addNamespace(Namespace ns) {
        if (ns && !namespaces.contains(ns)) {
            namespaces << ns
            ns.module = this
        }
    }

    void removeNamespace(Namespace ns) {
        if (ns && namespaces.contains(ns)) {
            namespaces -= ns
            ns.proj = null
        }
    }

    /**
     * Checks whether this module contains a file with the given absolute path
     * name.
     *
     * @param path
     *            The absolute path of a file to find in the module.
     * @return true if a file exists in this module with the given absolute
     *         path, false otherwise.
     */
    boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false

        return files.containsKey(path)
    }
    
    /**
     * Searches this module for a namespace with the given qualified identifier.
     * If such a namespace is found it is returned, otherwise null is returned.
     *
     * @param nsID
     *            Qualified Identifier of the namespace to search for
     * @return The found Namespace, or null if the provided identifier is null
     *         or empty or no such namespace exists within this module.
     */
    Namespace getNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty() || !namespaces.containsKey(nsID))
            return null

        namespaces.get(nsID)
    }

    /**
     * Checks whether a namespace with the given identifier exists within this
     * module.
     *
     * @param nsID
     *            Qualified Identifier of the namespace to search for.
     * @return true if a namespace with the given identifier is contained in
     *         this module, false otherwise.
     */
    boolean hasNamespace(String nsID)
    {
        if (nsID == null || nsID.isEmpty())
            return false

        namespaces.containsKey(nsID)
    }

    List<Method> methods() {
        def methods = []
        types().each { Type type ->
            methods << type.methods()
        }
        methods
    }

    List<Type> types() {
        []
    }

    List<File> files() {
        []
    }
}
