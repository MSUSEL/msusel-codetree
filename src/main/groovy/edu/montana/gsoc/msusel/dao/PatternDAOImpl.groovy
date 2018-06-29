package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.pattern.Pattern
import edu.montana.gsoc.msusel.datamodel.pattern.PatternRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternDAOImpl extends GenericDAOImpl<Pattern, Long> implements PatternDAO {

    PatternDAOImpl() {
        super(Pattern.class)
    }

    @Transactional
    PatternRepository findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<PatternRepository> f = em.getCriteriaBuilder().createQueury(PatternRepository.class)
            Root<Pattern> r = f.from(entityClass)
            f.select(r.<PatternRepository>get("repository")).where(cb.equal(r.get("patternKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Pattern findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Pattern> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Pattern> p = c.from(entityClass)
            c.select(p).where(cb.equal(p.get("patternKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Pattern> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Pattern> cq = cb.createQuery(entityClass)
        Root<Pattern> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<PatternRepository>get("repository").<String>get("repoKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
