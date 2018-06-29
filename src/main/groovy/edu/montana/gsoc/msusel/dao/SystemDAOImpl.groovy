package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.System

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SystemDAOImpl extends GenericDAOImpl<System, Long> implements SystemDAO {

    SystemDAOImpl() {
        super(System.class)
    }

    @Transactional
    @Override
    System findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<System> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<System> s = c.from(entityClass)
            c.select(s).where(cb.equal(s.get("sysKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Override
    List<System> findAll(String ownerKey) {
        findAll()
    }
}
