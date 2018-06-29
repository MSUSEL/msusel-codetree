package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Field

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FieldDAOImpl extends MemberDAOImpl<Field> implements FieldDAO {

    FieldDAOImpl() {
        super(Field.class)
    }

    @Transactional
    @Override
    Field findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Field> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Field> f = c.from(entityClass)
            c.select(f).where(cb.equal(f.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
