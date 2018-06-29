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
            CriteriaQuery<Project> f = em.getCriteriaBuilder().createQueury(Project.class)
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

    @Override
    List<Type> findTypesUsingMethod(Method method) {
        return null
    }

    @Override
    List<Method> findMethodsCalledFrom(Type type) {
        return null
    }

    @Override
    List<Field> findFieldsUsedBy(Method method) {
        return null
    }

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

    @Override
    boolean hasBidirectionalAssociation(Type from, Type to) {
        return false
    }

    @Override
    boolean hasContainmentRelation(Type from, Type to) {
        return false
    }

    @Override
    boolean hasUniDirectionalAssociation(Type from, Type to) {
        return false
    }

    @Override
    boolean hasUseDependency(Type from, Type to) {
        return false
    }

    @Override
    boolean hasGeneralization(Type type, Type gen) {
        return false
    }

    @Override
    boolean hasRealization(Type type, Type real) {
        return false
    }

    @Override
    List<Method> findMethodUseInSameClass(Method method, Type type) {
        return null
    }

    @Override
    List<Field> findFieldUseInSameClass(Method method, Type type) {
        return null
    }
}
