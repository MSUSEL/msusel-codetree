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
package edu.montana.gsoc.msusel.datamodel.type

import edu.montana.gsoc.msusel.datamodel.Accessibility
import edu.montana.gsoc.msusel.datamodel.Component
import edu.montana.gsoc.msusel.datamodel.Modifier
import edu.montana.gsoc.msusel.datamodel.TypeReference
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.member.*
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create")
abstract class Type extends Component {

    @OneToMany
    List<Type> children = []
    @OneToMany(mappedBy = "owner")
    List<Component> members = []
    @ManyToOne(fetch = FetchType.LAZY)
    File container
    @Embedded
    List<TypeReference> templateParams = []

    Type(String key, File file, Type parent, String name, Accessibility access, List<Modifier> modifiers, List<Type> children, List<Component> members, int start, int end) {
        super(key, parent, name, access, modifiers, start, end)
        this.container = file
        this.children = children
        this.members = members
    }

    Type() {
        super()
    }

    Type plus(Type t) {
        addChild(t)
        this
    }

    Type leftShift(Type t) {
        addChild(t)
        this
    }

    Type minus(Type t) {
        removeChild(t)
        this
    }

    void addChild(Type t) {
        if (t && !children.contains(t)) {
            children << t
            t.parent = this
        }
    }

    void removeChild(Type t) {
        if (t && children.contains(t)) {
            children -= t
            t.parent = null
        }
    }

    Type plus(Member t) {
        addMember(t)
        this
    }

    Type leftShift(Member t) {
        addMemberl(t)
        this
    }

    Type minus(Member t) {
        removeMember(t)
        this
    }

    void addMember(Member m) {
        if (l && !members.contains(l)) {
            mbmers << m
            m.parent = this
        }
    }

    void removeMember(Member m) {
        if (l && members.contains(m)) {
            members -= m
            m.parent = null
        }
    }

    Type plus(Modifier m) {
        addModifier(m)
        this
    }

    Type leftShift(Modifier m) {
        addModifier(m)
        this
    }

    Type minus(Modifier m) {
        removeModifier(m)
        this
    }

    void addModifier(Modifier m) {
        if (m && !modifiers.contains(m)) {
            modifiers << m
        }
    }

    void removeModifier(Modifier m) {
        if (m && modifiers.contains(m)) {
            modifiers -= m
        }
    }

    void addTypeParam(TypeReference node) {
        if (templateParams == null)
            templateParams = []

        if (node != null || !templateParams.contains(node))
            templateParams << node
    }

    List<Method> methods() {
        List<Method> m = []

        m += members.findAll {
            it instanceof Method
        }

        m
    }

    List<Constructor> constructors() {
        List<Constructor> c = []

        c += members.findAll {
            it instanceof Constructor
        }

        c
    }

    List<Field> fields() {
        List<Field> f = []

        f += members.findAll {
            it instanceof Field
        }

        f
    }

    def getField(String name) {
        fields().find { it.name() == name }
    }

    def properties() {
        children.findAll {
            it instanceof Property
        }
    }

    boolean isStatic() {
        modifiers.contains(Modifier.STATIC)
    }

    boolean isAbstract() {
        modifiers.contains(Modifier.ABSTRACT)
    }

    abstract boolean isInterface()

    Method findMethod(final int line) {
        if (line < getStart() || line > getEnd())
            return null

        methods().find { Method m -> m.containsLine(line) }
    }

    def getMethod(String name) {
        methods().find { it.name == name }
    }

    def getAllMethodsByName(String name) {
        methods().findAll { it.name == name }
    }

    TypeReference getTypeParamByName(String param) {
        templateParams.find { it.name() == param }
    }

    boolean hasTypeParam(String name) {
        templateParams.find { it.name() == name } != null
    }

    Method findMethodBySignature(String sig) {
        methods().find { Method m -> m.signature() == sig }
    }

    Initializer getStaticInitializer(int i) {
        (Initializer) members.find { it instanceof Initializer && it.modifiers.contains(Modifier.STATIC) && it.name == '<static_init$' + i + '>' }
    }

    Initializer getInstanceInitializer(int i) {
        (Initializer) members.find { it instanceof Initializer && !it.modifiers.contains(Modifier.STATIC) && it.name =~ /^<init\$${i}>$/ }
    }

    boolean hasMethod(Method node) {
        methods().find { it == node } != null
    }

    Field findField(int line) {
        fields().find { it.containsLine(line) }
    }

    // TODO Remove this
    abstract def generatePlantUML()

    boolean hasMethodWithMatchingSignature(Method method) {
        methods().any { Method m ->
            m.signature() == method.signature()
        }
    }
}