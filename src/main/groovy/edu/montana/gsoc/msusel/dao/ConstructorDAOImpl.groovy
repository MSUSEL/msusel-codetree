package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Constructor

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ConstructorDAOImpl extends MemberDAOImpl<Constructor> implements ConstructorDAO {

    ConstructorDAOImpl() {
        super(Constructor.class)
    }

    @Transactional
    @Override
    Constructor findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Constructor> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Constructor> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
