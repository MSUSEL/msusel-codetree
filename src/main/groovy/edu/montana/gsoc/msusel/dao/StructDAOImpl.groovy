package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Struct

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class StructDAOImpl extends TypeDAOImpl<Struct> implements StructDAO {

    StructDAOImpl() {
        super(Struct.class)
    }

    @Transactional
    File findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<File> f = em.getCriteriaBuilder().createQueury(File.class)
            Root<Struct> r = f.from(entityClass)
            f.select(r.<File>get("container")).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Struct findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Struct> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Struct> r = c.from(entityClass)
            c.select(r).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }
}
