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
package edu.montana.gsoc.msusel.datamodel.member

import edu.montana.gsoc.msusel.datamodel.TypeReference
import edu.montana.gsoc.msusel.datamodel.Accessibility
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.Modifier
import edu.montana.gsoc.msusel.datamodel.cfg.ControlFlowGraph
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Transient

/**
 * An abstraction of a type method. A method's unique qualified name is similar
 * to a fields but contains a parenthesized listing of the parameter types
 * associated with the method. Each method contains the following: a set of
 * parameters, and a set of statements. A method also has a return type
 * associated with it. Each parameter is simply a pair consisting of a type and
 * a name.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@EqualsAndHashCode(excludes = ["modifiers", "access"])
class Method extends TypedMember {

    @Embedded
    List<Parameter> params = []
    @Embedded
    List<TypeReference> typeParams = []
    @Transient
    ControlFlowGraph cfg
    @Embedded
    List<TypeReference> exceptions = []

    @Builder(buildMethodName = "create")
    protected Method(String key, Type parent, String name, Accessibility access, List<Modifier> modifiers, TypeReference type, List<Parameter> params, int start, int end) {
        super(key, parent, name, access, modifiers, type, start, end)
        this.params = params
    }

    Method() {
        super()
    }

    Method plus(Parameter p) {
        addParameter(p)
        this
    }

    Method minus(Parameter p) {
        removeParameter(p)
        this
    }

    Method leftShift(Parameter p) {
        addParameter(p)
        this
    }

    void addParameter(Parameter p) {
        if (p && !params.contains(p))
            params << p
    }

    void removeParameter(Parameter p) {
        if (p && params.contains(p))
            params -= p
    }

    Method plus(Modifier m) {
        addModifier(m)
        this
    }

    Method leftShift(Modifier m) {
        addModifier(m)
        this
    }

    Method minus(Modifier m) {
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

    def addException(TypeReference ref) {
        if (ref != null && !exceptions.contains(ref)) {
            exceptions << ref
        }
    }

    def hasModifier(String mod) {
        modifiers.contains(Modifier.valueOf(mod.toUpperCase()))
    }

    boolean isAbstract() {
        return hasModifier("abstract")
    }

    def isOverriding(Type owner, DataModelMediator tree) {
        Type current = owner
        def parents = []
        while (!tree.getGeneralizedFrom(current).isEmpty()) {
            current = (Type) tree.getGeneralizedFrom(current).first()
            parents << current
        }

        parents.each { Type p ->
            if (p.hasMethodWithMatchingSignature(this))
                return true
        }

        return false
    }

    String signature() {
        String sig = "${getType() != null ? getType().name() + ' ' + name() : name()}("
        params.each { Parameter param ->
            sig += param.getType().name() + ", "
        }
        if (sig.endsWith(", "))
            sig = sig.trim()[0..-2]
        sig += ")"

        sig
    }

    def getParameterByName(String param) { params.find { it.name() == param } }

    TypeReference getTypeParamByName(String paramName) {
        typeParams.find { it.name() == paramName }
    }

    TypeReference getExceptionByName(String exception) {
        exceptions.find { it.name() == exception }
    }

    def isAccessor() {
        return (name.startsWith("get") && params.size() == 0 && (getType() != null && getType().name() != "void"))
    }

    def isMutator() {
        return (name.startsWith("set") && params.size() == 1 && (getType() != null && getType().name() == "void"))
    }
}
