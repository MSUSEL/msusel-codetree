package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.Rule
import edu.montana.gsoc.msusel.datamodel.measures.RuleRepository

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RuleDAOImpl extends GenericDAOImpl<Rule, Long> implements RuleDAO {

    RuleDAOImpl() {
        super(Rule.class)
    }

    @Transactional
    RuleRepository findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<RuleRepository> f = em.getCriteriaBuilder().createQueury(RuleRepository.class)
            Root<Rule> r = f.from(entityClass)
            f.select(r.<RuleRepository>get("repository")).where(cb.equal(r.get("ruleKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Rule findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Rule> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Rule> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("ruleKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Rule> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Rule> cq = cb.createQuery(entityClass)
        Root<Rule> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<RuleRepository>get("repository").<String>get("repoKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
