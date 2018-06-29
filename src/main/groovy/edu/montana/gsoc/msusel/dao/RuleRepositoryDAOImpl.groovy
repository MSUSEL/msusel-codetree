package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.RuleRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RuleRepositoryDAOImpl extends GenericDAOImpl<RuleRepository, Long> implements RuleRepositoryDAO {

    RuleRepositoryDAOImpl() {
        super(RuleRepository.class)
    }

    @Transactional
    @Override
    RuleRepository findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<RuleRepository> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<RuleRepository> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("repoKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Override
    List<RuleRepository> findAll(String ownerKey) {
        findAll()
    }
}
