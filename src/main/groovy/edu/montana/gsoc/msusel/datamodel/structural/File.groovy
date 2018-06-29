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

import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "imports", "types"])
class File implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String fileKey
    String name
    String language
    @Embedded
    List<Import> imports = []
    @OneToMany
    List<Type> types = []
    @ManyToOne(fetch = FetchType.LAZY)
    Namespace namespace
    @Enumerated(EnumType.STRING)
    FileType type

    File leftShift(Type type) {
        addType(type)
        this
    }

    File plus(Type type) {
        addType(type)
        this
    }

    File minus(Type type) {
        removeType(type)
    }

    void addType(Type type) {
        if (type && !types.contains(type)) {
            types << type
            type.file = this
        }
    }

    void removeType(Type type) {
        if (type && types.contains(type)) {
            types -= type
            type.file = null
        }
    }

    File leftShift(Import imp) {
        addImport(imp)
        this
    }

    File plus(Import imp) {
        addImport(imp)
        this
    }

    File minus(Import imp) {
        removeImport(imp)
        this
    }

    void addImport(Import imp) {
        if (imp && !imports.contains(imp)) {
            imports << imp
        }
    }

    void removeImport(Import imp) {
        if (imp && imports.contains(imp)) {
            imports -= imp
        }
    }

    /**
     * Retrieves the method containing the given line from a type contained in
     * this file.
     *
     * @param line
     *            Line number for which a method is requested.
     * @return Qualified identifier of the method at the given line, or the
     *         empty string if no method exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    Method findMethod(final int line) {
        Type parent = findType(line)

        parent?.findMethod(line)
    }

    /**
     * Retrieves the qualified identifier of the type in this file which
     * contains the given line.
     *
     * @param line
     *            Line number for which a type is requested
     * @return Qualified identifier of the type at the given line, or the
     *         empty string if no type exists at the line or if the line is
     *         outside the range of any type in this file.
     */
    Type findType(final int line) {
        types().find { Type t -> t.containsLine(line) }
    }

    /**
     * Retrieves the type with the given qualified identifier.
     *
     * @param key
     *            Qualified identifier of the type to find in this file.
     * @return The type with the given identifier, if one exists, null
     *         otherwise or if the given identifier is null or empty.
     */
    Type findType(final String key) {
        if (key == null || key.isEmpty())
            return null

        types().find { Type t -> t.key == key }
    }

    /**
     * Retrieves the qualified identifier of the field at the given line, if one
     * exists.
     *
     * @param line
     *            Line to find a field at.
     * @return The qualified identifier if the given line contains a field
     *         definition, empty string otherwise.
     */
    String findField(final int line) {
        final String type = getType(line)

        if (type != null && types.containsKey(type)) {
            final Type node = types.get(type)
            if (node.getField(line) != null) {
                return node.getField(line).getQIdentifier()
            }
        }

        return ""
    }

    /**
     * Checks whether the provided type is contained within this file.
     *
     * @param key
     *            The unique identifier of the type in question
     * @return true if contained in this file, false if provided type is null or
     *         not contained in this file.
     */
    boolean hasType(String key) {
        findType(key) != null
    }

    /**
     * @return The set of methods of all types contained in this file.
     */
    List<Method> methods() {
        def methods = []
        types().each { Type t ->
            methods.addAll(t.methods())
        }

        return methods
    }

    /**
     * Checks if the given import is contained in this File
     *
     * @param imp
     *            Import to check
     * @return true if the given import is contained in the File, false if
     *         the provided value is null, empty, or not contained in the
     *         File.
     */
    boolean hasImport(String imp) {
        if (imp == null || imp.isEmpty())
            return false

        return imports.contains(imp)
    }

    /**
     * @return The set of Imports contained in the File
     */
    List<String> getImports() {
        []
    }

    def following(int line) {

    }

    @Override
    String key() {
        fileKey
    }

    @Override
    String name() {
        name
    }
}
