package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Method

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MethodDAOImpl extends MemberDAOImpl<Method> implements MethodDAO {

    MethodDAOImpl() {
        super(Method.class)
    }

    @Transactional
    @Override
    Method findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Method> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Method> m = c.from(entityClass)
            c.select(m).where(cb.equal(m.get("metricKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
