package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.RefType
import edu.montana.gsoc.msusel.datamodel.pattern.PatternInstance
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
class PatternInstanceDAOImpl extends GenericDAOImpl<PatternInstance, Long> implements PatternInstanceDAO {

    PatternInstanceDAOImpl() {
        super(PatternInstance.class)
    }

    @Transactional
    Project findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Project> f = em.getCriteriaBuilder().createQueury(Project.class)
            Root<PatternInstance> r = f.from(entityClass)
            f.select(r.<Project>get("project")).where(cb.equal(r.get("instKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    PatternInstance findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<PatternInstance> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<PatternInstance> i = c.from(entityClass)
            c.select(i).where(cb.equal(i.get("instKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<PatternInstance> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<PatternInstance> cq = cb.createQuery(entityClass)
        Root<PatternInstance> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Project>get("project").<String>get("projKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }

    @Transactional
    @Override
    List<Type> findTypes(PatternInstance inst) {
        List<Type> types = []
        inst = em.merge(inst)
        def bindings = inst.getBindings().find {
            it.getRef().getRefType() == RefType.TYPE
        }
        bindings.each {
            key = it.getRef().getRefKey()
            def c = this.<Class>findType(key)
            def e = this.<Enum>findType(key)
            def i = this.<Interface>findType(key)
            def s = this.<Struct>findType(key)
            def v = this.<Event>findType(key)

            if (c) types << c
            else if (i) types << i
            else if (s) types << s
            else if (e) types << e
            else if (v) types << v
        }
        types
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
}
