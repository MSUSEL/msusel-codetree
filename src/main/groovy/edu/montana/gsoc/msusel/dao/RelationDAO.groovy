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
package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.member.Field
import edu.montana.gsoc.msusel.datamodel.member.Member
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.relations.Relation
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface RelationDAO extends GenericDAO<Relation, Long> {

    List<Type> findFromRelations(Type t, RelationType type)

    List<Type> findToRelations(Type t, RelationType type)

    public <M extends Member> List<Type> findTypesUsingMember(M m)

    public <M extends Member> List<M> findMembersUsedInMethod(Method m)

    List<Method> findMethodsUsingMember(Member m)

    List<Type> findTypesUsingMethod(Method method)

    List<Method> findMethodsCalledFrom(Type type)

    List<Field> findFieldsUsedBy(Method method)

    List<Field> findFieldsUsedBy(Type type)

    List<Type> findAllParentClasses(Type type)

    List<Type> findAllDescendantClasses(Type type)

    boolean hasBidirectionalAssociation(Type from, Type to)

    boolean hasContainmentRelation(Type from, Type to)

    boolean hasUniDirectionalAssociation(Type from, Type to)

    boolean hasUseDependency(Type from, Type to)

    boolean hasGeneralization(Type type, Type gen)

    boolean hasRealization(Type type, Type real)

    List<Method> findMethodUseInSameClass(Method method, Type type)

    List<Field> findFieldUseInSameClass(Method method, Type type)
}
