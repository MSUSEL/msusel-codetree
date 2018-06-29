package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.measures.Metric
import edu.montana.gsoc.msusel.datamodel.structural.*
import edu.montana.gsoc.msusel.datamodel.type.Type

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class NamespaceDAOImpl extends GenericDAOImpl<Namespace, Long> implements NamespaceDAO {

    NamespaceDAOImpl() {
        super(Namespace.class)
    }

    @Transactional
    Structure findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Structure> f = em.getCriteriaBuilder().createQueury(Structure.class)
            Root<Metric> r = f.from(entityClass)
            f.select(r.<Module>get("container")).where(cb.equal(r.get("nsKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Namespace findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Namespace> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Namespace> n = c.from(entityClass)
            c.select(n).where(cb.equal(n.get("nsKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Namespace> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Namespace> cq = cb.createQuery(entityClass)
        Root<Namespace> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Module>get("container").<String>get("modKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }

    @Transactional
    @Override
    List<File> findFiles(Namespace namespace) {
        namespace = em.merge(namespace)
        namespace.files()
    }

    @Transactional
    @Override
    List<Type> findTypes(Namespace namespace) {
        namespace = em.merge(namespace)
        List<Type> types = []
        namespace.files().each {
            types += it.getTypes()
        }
        types
    }
}
