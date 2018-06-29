package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Destructor

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class DestructorDAOImpl extends MemberDAOImpl<Destructor> implements DestructorDAO {

    DestructorDAOImpl() {
        super(Destructor.class)
    }

    @Transactional
    @Override
    Destructor findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Destructor> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Destructor> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
