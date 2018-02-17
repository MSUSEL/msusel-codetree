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
package edu.montana.gsoc.msusel.codetree

import edu.montana.gsoc.msusel.codetree.environment.EnvironmentLoader
import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.ProjectNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.relations.RelationshipType
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class CodeTree {

    ProjectNode project
    Table<TypeNode, TypeNode, List<RelationshipType>> table = HashBasedTable.create()
    CodeTreeUtils utils
    List<TypeNode> unknownTypes = []
    Map<String, NamespaceNode> langNs = [:]
    EnvironmentLoader loader

    /**
     *
     */
    CodeTree() {
        utils = new CodeTreeUtils(tree: this)
    }

    def addRelation(TypeNode from, TypeNode to, RelationshipType r) {
        List<RelationshipType> list = table.get(from, to) != null ? table.get(from, to) : []
        if (r != null)
            list << r
        table.put(from, to, list)
    }

    def addGeneralizes(TypeNode from, TypeNode to) {
        addRelation(from, to, RelationshipType.GENERALIZATION)
    }

    def getGeneralizedFrom(TypeNode from) {
        def map = table.row(from).findAll { it.getValue().contains(RelationshipType.GENERALIZATION) }
        def list = []
        map.each {
            list << it.getKey()
        }
        list
    }

    def getGeneralizedTo(TypeNode to) {
        def map = table.column(to).findAll { it.getValue().contains(RelationshipType.GENERALIZATION) }
        def list = []
        map.each {
            list << it.getKey()
        }
        list
    }

    def addRealizes(TypeNode from, TypeNode to) {
        addRelation(from, to, RelationshipType.REALIZATION)
    }

    def getRealizedFrom(TypeNode from) {
        def map = table.row(from).findAll { it.getValue().contains(RelationshipType.REALIZATION) }
        def list = []
        map.each {
            list << it.getKey()
        }
        list
    }

    def getRealizedTo(TypeNode to) {
        table.column(to).findAll { it.getValue().contains(RelationshipType.REALIZATION) }
    }

    def addAssociation(TypeNode from, TypeNode to, boolean bidirectional) {
        addRelation(from, to, RelationshipType.ASSOCIATION)
        if (bidirectional)
            addRelation(to, from, RelationshipType.ASSOCIATION)
    }

    def getAssociatedFrom(TypeNode from) {
        table.row(from).findAll { it.getValue().contains(RelationshipType.ASSOCIATION) }
    }

    def getAssociatedTo(TypeNode to) {
        table.column(to).findAll { it.getValue().contains(RelationshipType.ASSOCIATION) }
    }

    def addAggregation(TypeNode from, TypeNode to, boolean bidirectional) {
        addRelation(from, to, RelationshipType.AGGREGATION)
        if (bidirectional)
            addRelation(to, from, RelationshipType.AGGREGATION)
    }

    def getAggregatedFrom(TypeNode from) {
        table.row(from).findAll { it.getValue() == RelationshipType.ASSOCIATION }
    }

    def getAggregatedTo(TypeNode to) {
        table.column(to).findAll { it.getValue() == RelationshipType.ASSOCIATION }
    }

    def addComposition(TypeNode from, TypeNode to, boolean bidirectional) {
        addRelation(from, to, RelationshipType.COMPOSITION)
        if (bidirectional)
            addRelation(to, from, RelationshipType.COMPOSITION)
    }

    def getComposedFrom(TypeNode from) {
        table.row(from).findAll { it.getValue().contains(RelationshipType.COMPOSITION) }
    }

    def getComposedTo(TypeNode to) {
        table.column(to).findAll { it.getValue().contains(RelationshipType.COMPOSITION) }
    }

    def addUse(TypeNode from, TypeNode to) {
        addRelation(from, to, RelationshipType.USE)
    }

    def getUseFrom(TypeNode from) {
        table.row(from).findAll { it.getValue() == RelationshipType.USE }
    }

    def getUseTo(TypeNode to) {
        table.column(to).findAll { it.getValue().contains(RelationshipType.USE) }
    }

    def addDependency(TypeNode from, TypeNode to) {
        addRelation(from, to, RelationshipType.DEPENDENCY)
    }

    def addContainment(TypeNode contained, TypeNode container) {
        addRelation(contained, container, RelationshipType.CONTAINMENT)
    }

    def getDependencyFrom(TypeNode from) {
        table.row(from).values().findAll { it.contains(RelationshipType.DEPENDENCY) }
    }

    def getDependencyTo(TypeNode to) {
        table.column(to).findAll { it.getValue().contains(RelationshipType.DEPENDENCY) }
    }

    def getContainedIn(TypeNode container) {
        table.column(container).findAll { it.getValue().contains(RelationshipType.CONTAINMENT) }
    }

    def getContainedBy(TypeNode contained) {
        table.row(contained).findAll { it.getValue().contains(RelationshipType.CONTAINMENT) }
    }

    def getTypesUsingMethod(MethodNode method) {

    }

    def getMethodsCalledFrom(TypeNode type) {

    }

    def getMethodsCalledFrom(MethodNode method) {

    }

    def getMethodsCallingMethod(MethodNode method) {

    }

    def getFieldsUsedBy(MethodNode method) {

    }

    def getAllParentClasses(TypeNode type) {

    }

    def getAllDescendentClasses(TypeNode type) {

    }

    /**
     * Deserializes a CodeTree object from a given JSON string.
     *
     * @param json
     *            JSON encoded codetree object.
     * @return A newly instantiated CodeTree deserialized from the given string,
     *         or null if the provided string is null or empty.
     */
    static CodeTree createFromJson(String json) {
        def jsonSlurper = new JsonSlurper()
        def object = jsonSlurper.parseText(json)
    }

    /**
     * @return Serializes this code tree and its contents to a JSON string.
     */
    String toJSON() {
        def json = JsonOutput.toJson(this)
        JsonOutput.prettyPrint(json)
    }

    void addUnknownType(TypeNode node) {
        if (node != null && !unknownTypes.contains(node))
            unknownTypes.add(node)
    }

    def generatePlantUML() {
        StringBuilder builder = new StringBuilder()
        builder.append("@startuml\n")
        builder.append("\n")
        getUtils().getTypes().each { TypeNode t -> builder.append(t.generatePlantUML()) }
        getUtils().getTypes().each { TypeNode from ->
            table.row(from).each { TypeNode to, List<RelationshipType> list ->
                list.each {
                    builder.append("${to.name()} ${it.getPlantUML()} ${from.name()}\n")
                }
            }
        }
        builder.append("@enduml")

        builder.toString()
    }

    boolean hasBiDirectionalAssociation(TypeNode from, TypeNode to) {
        hasUniDirectionalAssociation(from, to) && hasUniDirectionalAssociation(to, from)
    }

    boolean hasContainmentRelation(TypeNode from, TypeNode to) {
        table.get(from, to)?.contains(RelationshipType.CONTAINMENT)
    }

    boolean hasUniDirectionalAssociation(TypeNode from, TypeNode to) {
        table.get(from, to)?.contains(RelationshipType.ASSOCIATION)
    }

    boolean hasUseDependency(TypeNode from, TypeNode to) {
        table.get(from, to)?.contains(RelationshipType.DEPENDENCY)
    }

    NamespaceNode languageNamespace(String s) {
        langNs[s]
    }

    def addLanguageNamespace(String s, NamespaceNode namespaceNode) {
        println "NS: ${s}@${namespaceNode}"
        if (s != null && !s.empty && namespaceNode != null)
            langNs[s] = namespaceNode
    }

    boolean inheritsFrom(TypeNode type, TypeNode gen) {
        table.get(type, gen)?.contains(RelationshipType.GENERALIZATION)
    }

    boolean realizes(TypeNode type, TypeNode real) {
        table.get(type, real)?.contains(RelationshipType.REALIZATION)
    }
}