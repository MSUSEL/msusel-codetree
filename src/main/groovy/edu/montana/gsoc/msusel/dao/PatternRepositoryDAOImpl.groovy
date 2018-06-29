package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.pattern.PatternRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternRepositoryDAOImpl extends GenericDAOImpl<PatternRepository, Long> implements PatternRepositoryDAO {

    PatternRepositoryDAOImpl() {
        super(PatternRepository.class)
    }

    @Transactional
    @Override
    PatternRepository findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<PatternRepository> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<PatternRepository> p = c.from(entityClass)
            c.select(p).where(cb.equal(p.get("repoKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<PatternRepository> findAll(String ownerKey) {
        findAll()
    }
}
