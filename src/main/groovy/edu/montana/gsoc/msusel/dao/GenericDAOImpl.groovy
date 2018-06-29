package edu.montana.gsoc.msusel.dao

import com.google.inject.Inject
import com.google.inject.persist.Transactional

import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.criteria.CriteriaQuery

import static javax.persistence.LockModeType.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @Inject
    protected EntityManager em
    protected Class<T> entityClass

    protected GenericDAOImpl(Class<T> entityClass) {
        this.em = em
        this.entityClass = entityClass
    }

    @Override
    void joinTransaction() {
        if (!em.isJoinedToTransaction())
            em.joinTransaction()
    }

    @Override
    T findById(ID id) {
        return findById(id, NONE)
    }

    @Transactional
    @Override
    T findById(ID id, LockModeType lockModeType) {
        return em.find(entityClass, id, lockModeType)
    }

    @Transactional
    @Override
    T findReferenceById(ID id) {
        return em.getReference(entityClass, id)
    }

    @Transactional
    @Override
    List<T> findAll() {
        CriteriaQuery<T> c = em.getCriteriaBuilder().createQuery(entityClass)
        c.select(c.from(entityClass))
        return em.createQuery(c).getResultList()
    }

    @Transactional
    @Override
    Long getCount() {
        CriteriaQuery<Long> c =
                em.getCriteriaBuilder().createQuery(Long.class)
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)))
        return em.createQuery(c).getSingleResult()
    }

    @Transactional
    @Override
    void checkVersion(T entity, boolean forceUpdate) {
        em.lock(entity, forceUpdate ? OPTIMISTIC_FORCE_INCREMENT : OPTIMISTIC)
    }

    @Transactional
    @Override
    T makePersistent(T instance) {
        // merge() handles transient AND detached instances
        return em.merge(instance)
    }

    @Override
    T update(T entity) {
        makePersistent(entity)
    }

    @Transactional
    @Override
    void makeTransient(T instance) {
        em.remove(instance)
    }
}
