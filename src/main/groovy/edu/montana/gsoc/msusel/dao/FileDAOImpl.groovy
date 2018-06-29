package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.type.Class
import edu.montana.gsoc.msusel.datamodel.type.Type

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FileDAOImpl extends GenericDAOImpl<File, Long> implements FileDAO {

    FileDAOImpl() {
        super(File.class)
    }

    @Transactional
    Namespace findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Namespace> f = em.getCriteriaBuilder().createQueury(File.class)
            Root<Class> r = f.from(entityClass)
            f.select(r.<Namespace>get("container")).where(cb.equal(r.get("compKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    File findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<File> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<File> f = c.from(entityClass)
            c.select(f).where(cb.equal(f.get("fileKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<File> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<File> cq = cb.createQuery(entityClass)
        Root<File> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<Namespace>get("namespace").<String>get("nsKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }

    @Transactional
    @Override
    List<Type> findTypes(File file) {
        file = em.merge(file)

        file.getTypes()
    }
}
