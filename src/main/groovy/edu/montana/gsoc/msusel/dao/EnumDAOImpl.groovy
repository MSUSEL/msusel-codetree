package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Class
import edu.montana.gsoc.msusel.datamodel.type.Enum

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class EnumDAOImpl extends TypeDAOImpl<Enum> implements EnumDAO {

    EnumDAOImpl() {
        super(Enum.class)
    }

    @Transactional
    File findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<File> f = em.getCriteriaBuilder().createQueury(File.class)
            Root<Class> r = f.from(entityClass)
            f.select(r.<File>get("container")).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Enum findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Enum> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Enum> e = c.from(entityClass)
            c.select(e).where(cb.equal(e.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
