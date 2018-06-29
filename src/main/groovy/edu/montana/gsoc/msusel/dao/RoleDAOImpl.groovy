package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.pattern.Pattern
import edu.montana.gsoc.msusel.datamodel.pattern.Role

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RoleDAOImpl extends GenericDAOImpl<Role, Long> implements RoleDAO {

    RoleDAOImpl() {
        super(Role.class)
    }

    @Transactional
    Pattern findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Pattern> f = em.getCriteriaBuilder().createQueury(Pattern.class)
            Root<Role> r = f.from(entityClass)
            f.select(r.<Pattern>get("pattern")).where(cb.equal(r.get("roleKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Role findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Role> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Role> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("roleKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Role> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Role> cq = cb.createQuery(entityClass)
        Root<Role> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Pattern>get("pattern").<String>get("patternKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
