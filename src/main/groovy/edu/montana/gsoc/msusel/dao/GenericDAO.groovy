package edu.montana.gsoc.msusel.dao

import javax.persistence.LockModeType
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface GenericDAO<T, ID extends Serializable> extends Serializable {

    void joinTransaction()

    T findById(ID id)

    T findById(ID id, LockModeType lockModeType)

    T findReferenceById(ID id)

    List<T> findAll()

    Long getCount()

    T makePersistent(T entity)

    T update(T entity)

    void makeTransient(T entity)

    void checkVersion(T entity, boolean forceUpdate)

    T findByKey(String key)

    List<T> findAll(String ownerKey)
}