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
package edu.montana.gsoc.msusel.codetree.node.type

import com.google.gson.annotations.Expose
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.EnumLiteralNode
import edu.montana.gsoc.msusel.codetree.typeref.TypeVarTypeRef
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import groovy.transform.builder.Builder

import javax.persistence.Entity

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Entity
class EnumNode extends ClassNode {

    @Expose
    List<EnumLiteralNode> literals

    @Builder(buildMethodName = "create")
    EnumNode(String key, String parentKey, Accessibility accessibility = Accessibility.DEFAULT, specifiers = [],
             int start, int end, List<TypeVarTypeRef> templateParams, NamespaceNode namespace, literals = []) {
        super(key, parentKey, accessibility, specifiers, start, end, templateParams, namespace)
        this.literals = literals
    }

    def getLiterals() {
        children.findAll {
            it instanceof EnumLiteralNode
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def generatePlantUML() {
        StringBuilder builder = new StringBuilder()

        builder.append("enum ${this.name()} {\n")
        literals.each { builder.append(it.name()) }
        fields().each { FieldNode f -> builder.append("${f.accessibility.plantUML}${f.name()} ${f.getType()}\n") }
        methods().each { MethodNode m -> builder.append("${m.accessibility.plantUML}${m.name()}() : ${m.getType()}\n") }
        builder.append("}\n")
        builder.append("\n")

        builder.toString()
    }
}
