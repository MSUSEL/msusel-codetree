package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Initializer

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class InitializerDAOImpl extends MemberDAOImpl<Initializer> implements InitializerDAO {

    InitializerDAOImpl() {
        super(Initializer.class)
    }

    @Transactional
    @Override
    Initializer findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Initializer> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Initializer> i = c.from(entityClass)
            c.select(i).where(cb.equal(i.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
