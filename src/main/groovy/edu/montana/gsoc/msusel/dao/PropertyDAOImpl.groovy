package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Property

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PropertyDAOImpl extends MemberDAOImpl<Property> implements PropertyDAO {

    PropertyDAOImpl() {
        super(Property.class)
    }

    @Transactional
    @Override
    Property findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Property> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Property> f = c.from(entityClass)
            c.select(f).where(cb.equal(f.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
