package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.Metric
import edu.montana.gsoc.msusel.datamodel.measures.MetricRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MetricDAOImpl extends GenericDAOImpl<Metric, Long> implements MetricDAO {

    MetricDAOImpl() {
        super(Metric.class)
    }

    @Transactional
    MetricRepository findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<MetricRepository> f = em.getCriteriaBuilder().createQueury(MetricRepository.class)
            Root<Metric> r = f.from(entityClass)
            f.select(r.<MetricRepository>get("repository")).where(cb.equal(r.get("metricKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Metric findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Metric> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Metric> m = c.from(entityClass)
            c.select(m).where(cb.equal(m.get("metricKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Metric> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Metric> cq = cb.createQuery(entityClass)
        Root<Metric> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<MetricRepository>get("repository").<String>get("repoKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
