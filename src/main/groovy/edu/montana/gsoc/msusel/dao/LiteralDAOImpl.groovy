package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Literal

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class LiteralDAOImpl extends MemberDAOImpl<Literal> implements LiteralDAO {

    LiteralDAOImpl() {
        super(Literal.class)
    }

    @Transactional
    @Override
    Literal findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Literal> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Literal> l = c.from(entityClass)
            c.select(l).where(cb.equal(l.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
