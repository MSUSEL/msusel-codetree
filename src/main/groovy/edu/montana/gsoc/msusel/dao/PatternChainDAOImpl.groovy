package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.pattern.PatternChain

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternChainDAOImpl extends GenericDAOImpl<PatternChain, Long> implements PatternChainDAO {

    PatternChainDAOImpl() {
        super(PatternChain.class)
    }

    @Transactional
    @Override
    PatternChain findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<PatternChain> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<PatternChain> p = c.from(entityClass)
            c.select(p).where(cb.equal(p.get("chainKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<PatternChain> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<PatternChain> cq = cb.createQuery(entityClass)
        Root<PatternChain> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<System>get("system").<String>get("sysKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
