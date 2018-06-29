package edu.montana.gsoc.msusel.dao

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Member
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Type

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

abstract class MemberDAOImpl<T extends Member> extends GenericDAOImpl<T, Long> implements MemberDAO<T> {

    protected MemberDAOImpl(Class<T> entityClass) {
        super(entityClass)
    }

    @Transactional
    @Override
    Type findParent(T t) {
        t = em.merge(t)
        t.getOwner()
    }

    @Transactional
    @Override
    File findParentFile(T t) {
        t = em.merge(t)
        Type type = t.getOwner()
        if (type) {
            type.getContainer()
        } else {
            null
        }
    }

    @Transactional
    @Override
    List<T> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<T> f = em.getCriteriaBuilder().createQueury(entityClass)
        Root<T> r = f.from(entityClass)
        f.select(r).where(cb.equal(r.<Type>get("owner").get("compKey"), ownerKey))
        return em.createQuery(f).getResultList()
    }
}
