package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.MetricRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MetricRepositoryDAOImpl extends GenericDAOImpl<MetricRepository, Long> implements MetricRepositoryDAO {

    MetricRepositoryDAOImpl() {
        super(MetricRepository.class)
    }

    @Transactional
    @Override
    MetricRepository findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<MetricRepository> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<MetricRepository> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("repoKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Override
    List<MetricRepository> findAll(String ownerKey) {
        findAll()
    }
}
