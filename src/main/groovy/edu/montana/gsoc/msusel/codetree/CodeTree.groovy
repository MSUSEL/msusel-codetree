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

import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.ProjectNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.relations.RelationshipType
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils

interface CodeTree {

    def addRelation(TypeNode from, TypeNode to, RelationshipType r)

    def addGeneralizes(TypeNode from, TypeNode to)

    def getGeneralizedFrom(TypeNode from)

    def getGeneralizedTo(TypeNode to)

    def addRealizes(TypeNode from, TypeNode to)

    def getRealizedFrom(TypeNode from)

    def getRealizedTo(TypeNode to)

    def addAssociation(TypeNode from, TypeNode to, boolean bidirectional)

    def getAssociatedFrom(TypeNode from)

    def getAssociatedTo(TypeNode to)

    def addAggregation(TypeNode from, TypeNode to, boolean bidirectional)

    def getAggregatedFrom(TypeNode from)

    def getAggregatedTo(TypeNode to)

    def addComposition(TypeNode from, TypeNode to, boolean bidirectional)

    def getComposedFrom(TypeNode from)

    def getComposedTo(TypeNode to)

    def addUse(TypeNode from, TypeNode to)

    def getUseFrom(TypeNode from)

    def getUseTo(TypeNode to)

    def addDependency(TypeNode from, TypeNode to)

    def addContainment(TypeNode contained, TypeNode container)

    def getDependencyFrom(TypeNode from)

    def getDependencyTo(TypeNode to)

    def getContainedIn(TypeNode container)

    def getContainedBy(TypeNode contained)

    def getTypesUsingMethod(MethodNode method)

    def getMethodsCalledFrom(TypeNode type)

    def getMethodsCalledFrom(MethodNode method)

    def getMethodsCallingMethod(MethodNode method)

    def getFieldsUsedBy(MethodNode method)

    def getAllParentClasses(TypeNode type)

    def getAllDescendentClasses(TypeNode type)

    /**
     * @return Serializes this code tree and its contents to a JSON string.
     */
    String toJSON()

    void addUnknownType(TypeNode node)

    def generatePlantUML()

    boolean hasBiDirectionalAssociation(TypeNode from, TypeNode to)

    boolean hasContainmentRelation(TypeNode from, TypeNode to)

    boolean hasUniDirectionalAssociation(TypeNode from, TypeNode to)

    boolean hasUseDependency(TypeNode from, TypeNode to)

    NamespaceNode languageNamespace(String s)

    def addLanguageNamespace(String s, NamespaceNode namespaceNode)

    boolean inheritsFrom(TypeNode type, TypeNode gen)

    boolean realizes(TypeNode type, TypeNode real)

    CodeTreeUtils getUtils()

    ProjectNode getProject()

    void setProject(ProjectNode pn)
}