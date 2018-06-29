package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.Finding
import edu.montana.gsoc.msusel.datamodel.structural.Project

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FindingDAOImpl extends GenericDAOImpl<Finding, Long> implements FindingDAO {

    FindingDAOImpl() {
        super(Finding.class)
    }

    @Transactional
    Project findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Project> f = em.getCriteriaBuilder().createQueury(Project.class)
            Root<Finding> r = f.from(entityClass)
            f.select(r.<Project>get("project")).where(cb.equal(r.get("findingKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Finding findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Finding> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Finding> f = c.from(entityClass)
            c.select(f).where(cb.equal(f.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Finding> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Finding> cq = cb.createQuery(entityClass)
        Root<Finding> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Project>get("project").<String>get("projKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
