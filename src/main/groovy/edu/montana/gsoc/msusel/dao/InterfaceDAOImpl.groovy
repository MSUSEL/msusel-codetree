package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Interface

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class InterfaceDAOImpl extends TypeDAOImpl<Interface> implements InterfaceDAO {

    InterfaceDAOImpl() {
        super(Interface.class)
    }

    @Transactional
    File findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<File> f = em.getCriteriaBuilder().createQueury(File.class)
            Root<Interface> r = f.from(entityClass)
            f.select(r.<File>get("container")).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Interface findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Interface> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Interface> i = c.from(entityClass)
            c.select(i).where(cb.equal(i.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
