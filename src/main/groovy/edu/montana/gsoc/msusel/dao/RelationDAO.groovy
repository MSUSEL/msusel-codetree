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
