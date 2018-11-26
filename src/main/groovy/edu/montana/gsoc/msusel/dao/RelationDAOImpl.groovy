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

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.RefType
import edu.montana.gsoc.msusel.datamodel.member.*
import edu.montana.gsoc.msusel.datamodel.relations.Relation
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.structural.Project
import edu.montana.gsoc.msusel.datamodel.type.*

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RelationDAOImpl extends GenericDAOImpl<Relation, Long> implements RelationDAO {



    RelationDAOImpl() {
        super(Relation.class)
    }

    Project findParent() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Project> f = em.getCriteriaBuilder().createQuery(Project.class)
            Root<Relation> r = f.from(entityClass)
            f.select(r.<Project>get("project")).where(cb.equal(r.get("relKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    List<Type> findFromRelations(Type t, RelationType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(Relation.class)
        Root<Relation> c = cq.from(Relation.class)
        cq.select(c).where(cb.and(cb.equal(c.<Reference>get("src").<String>get("refKey"), t.compKey),
                cb.equal(c.<Reference>get("src").<RefType>get("refType"), RefType.TYPE)),
                cb.equal(c.<RelationType>get("type"), type))
        List<Relation> rels = em.createQuery(cq).getResultList()

        extractTypes(rels)
    }

    private List<Type> extractTypes(List<Relation> rels) {
        List<Type> types = []
        rels.each {
            Type x = this.<Class>findType(it.src.refKey)
            Type i = this.<Interface>findType(it.src.refKey)
            Type e = this.<Enum>findType(it.src.refKey)
            Type v = this.<Event>findType(it.src.refKey)
            Type s = this.<Struct>findType(it.src.refKey)
            if (x && !types.contains(x))
                types << x
            else if (i && !types.contains(i))
                types << i
            else if (e && !types.contains(e))
                types << e
            else if (v && !types.contains(v))
                types << v
            else if (s && !types.contains(s))
                types << s
        }

        types
    }

    // TODO Fix this
    private List<Member> extractMembers(List<Relation> rels) {
        List<Member> members = []
        rels.each {
            Member l = this.<Literal>findMember(it.src.refKey)
            Member i = this.<Initializer>findMember(it.src.refKey)
            Member f = this.<Field>findMember(it.src.refKey)
            Member m = this.<Method>findMember(it.src.refKey)
            Member c = this.<Constructor>findMember(it.src.refKey)
            Member d = this.<Destructor>findMember(it.src.refKey)
            Member p = this.<Property>findMember(it.src.refKey)
            if (l && !members.contains(x))
                members << l
            else if (i && !members.contains(i))
                members << i
            else if (f && !members.contains(e))
                members << f
            else if (m && !members.contains(v))
                members << m
            else if (c && !members.contains(s))
                members << c
            else if (d && !members.contains(s))
                members << d
            else if (p && !members.contains(s))
                members << p
        }

        members
    }

    private <T extends Type> T findType(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<T> c = em.getCriteriaBuilder().createQuery(T.class)
            Root<T> r = c.from(T.class)
            c.select(r).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    List<Type> findToRelations(Type t, RelationType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(Relation.class)
        Root<Relation> c = cq.from(Relation.class)
        cq.select(c).where(cb.and(cb.equal(c.<Reference>get("dest").<String>get("refKey"), t.compKey),
                cb.equal(c.<Reference>get("src").<RefType>get("refType"), RefType.TYPE)),
                cb.equal(c.<RelationType>get("type"), type))
        List<Relation> rels = em.createQuery(cq).getResultList()

        extractTypes(rels)
    }

     public <M extends Member> List<Type> findTypesUsingMember(M m) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(Relation.class)
        Root<Relation> c = cq.from(Relation.class)
        cq.select(c).where(cb.and(cb.equal(c.<Reference>get("dest").<String>get("refKey"), m.compKey),
                cb.equal(c.<Reference>get("src").<RefType>get("refType"), RefType.MEMBER)),
                cb.equal(c.<RelationType>get("type"), RelationType.USE))
        List<Relation> rels = em.createQuery(cq).getResultList()

        extractTypes(rels)
    }

    public List<Member> findMembersUsedInMethod(Method m) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(Relation.class)
        Root<Relation> c = cq.from(Relation.class)
        cq.select(c).where(cb.and(cb.equal(c.<Reference>get("src").<String>get("refKey"), m.compKey),
                cb.equal(c.<Reference>get("src").<RefType>get("refType"), RefType.MEMBER)),
                cb.equal(c.<RelationType>get("type"), RelationType.USE))
        List<Relation> rels = em.createQuery(cq).getResultList()

        extractMembers(rels)
    }

    List<Method> findMethodsUsingMember(Member m) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(Relation.class)
        Root<Relation> c = cq.from(Relation.class)
        cq.select(c).where(cb.and(cb.equal(c.<Reference>get("dest").<String>get("refKey"), m.compKey),
                cb.equal(c.<Reference>get("src").<RefType>get("refType"), RefType.MEMBER)),
                cb.equal(c.<RelationType>get("type"), RelationType.USE))
        List<Relation> rels = em.createQuery(cq).getResultList()

        extractMembers(rels)
    }

    @Override
    Relation findByKey(String key) {
        return null
    }

    @Transactional
    @Override
    List<Relation> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Relation> cq = cb.createQuery(entityClass)
        Root<Relation> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Project>get("project").<String>get("projKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }

    // TODO Finish this
    @Override
    List<Type> findTypesUsingMethod(Method method) {
        return null
    }

    // TODO Finish this
    @Override
    List<Method> findMethodsCalledFrom(Type type) {
        return null
    }

    // TODO Finish this
    @Override
    List<Field> findFieldsUsedBy(Method method) {
        return null
    }

    // TODO Finish this
    @Override
    List<Field> findFieldsUsedBy(Type type) {
        return null
    }

    @Override
    List<Type> findAllParentClasses(Type type) {
        Set<Type> parents = [] as Set

        Queue<Type> q = [] as Queue
        q << type
        while (!q.empty) {
            current = q.poll()
            if (!parents.contains(current)) {
                parents << current
                q += findFromRelations(current, RelationType.GENERALIZATION)
                q += findFromRelations(current, RelationType.REALIZATION)
            }
        }

        parents -= type
        parents as List
    }

    @Override
    List<Type> findAllDescendantClasses(Type type) {
        Set<Type> desc = [] as Set

        Queue<Type> q = [] as Queue
        q << type
        while (!q.empty) {
            current = q.poll()
            if (!desc.contains(current)) {
                desc << current
                q += findToRelations(current, RelationType.GENERALIZATION)
                q += findToRelations(current, RelationType.REALIZATION)
            }
        }

        desc -= type
        desc as List
    }

    // TODO Finish this
    @Override
    boolean hasBidirectionalAssociation(Type from, Type to) {
        return false
    }

    // TODO Finish this
    @Override
    boolean hasContainmentRelation(Type from, Type to) {
        return false
    }

    // TODO Finish this
    @Override
    boolean hasUniDirectionalAssociation(Type from, Type to) {
        return false
    }

    // TODO Finish this
    @Override
    boolean hasUseDependency(Type from, Type to) {
        return false
    }

    // TODO Finish this
    @Override
    boolean hasGeneralization(Type type, Type gen) {
        return false
    }

    // TODO Finish this
    @Override
    boolean hasRealization(Type type, Type real) {
        return false
    }

    // TODO Finish this
    @Override
    List<Method> findMethodUseInSameClass(Method method, Type type) {
        return null
    }

    // TODO Finish this
    @Override
    List<Field> findFieldUseInSameClass(Method method, Type type) {
        return null
    }
}
