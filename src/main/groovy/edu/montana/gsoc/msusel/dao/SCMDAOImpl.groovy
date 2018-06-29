package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.SCM
import edu.montana.gsoc.msusel.datamodel.structural.Project

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class SCMDAOImpl extends GenericDAOImpl<SCM, Long> implements SCMDAO {

    protected SCMDAOImpl() {
        super(SCM.class)
    }

    @Transactional
    @Override
    SCM findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<SCM> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<SCM> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("scmKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<SCM> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<SCM> cq = cb.createQuery(entityClass)
        Root<SCM> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Project>get("project").<String>get("projKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
