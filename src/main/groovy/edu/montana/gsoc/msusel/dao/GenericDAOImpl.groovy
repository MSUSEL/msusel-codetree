/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
