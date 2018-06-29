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
import edu.montana.gsoc.msusel.datamodel.relations.RelationType
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Module
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.structural.Structure
import edu.montana.gsoc.msusel.datamodel.type.Type

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

abstract class TypeDAOImpl<T extends Type> extends GenericDAOImpl<T, Long> implements TypeDAO<T> {

    @Inject RelationDAO relDao

    protected TypeDAOImpl(Class<T> entityClass) {
        super(entityClass)
    }

    @Transactional
    @Override
    File findParentFile(T child) {
        child = em.merge(child)
        child.getContainer()
    }

    @Transactional
    @Override
    Module findModule(T aClass) {
        aClass = em.merge(aClass)
        File f = aClass.getContainer()
        Namespace n = f.getNamespace()
        Structure s = n.getContainer()
        if (s instanceof Module)
            s
        else
            null
    }

    @Transactional
    @Override
    Namespace findNamespace(T aClass) {
        aClass = em.merge(aClass)
        File f = aClass.getContainer()
        f.getNamespace()
    }

    @Transactional
    @Override
    List<Type> findAllDescendents(T type) {
        relDao.findAllDescendantClasses(type)
    }

    @Transactional
    @Override
    List<Type> findAllParents(T type) {
        relDao.findAllParentClasses(type)
    }

    @Transactional
    @Override
    List<Type> findParentClasses(T type) {
        Set<Type> parents = [] as Set
        parents += relDao.findFromRelations(type, RelationType.GENERALIZATION)
        parents += relDao.findFromRelations(type, RelationType.REALIZATION)
        parents as List
    }

    @Transactional
    @Override
    List<T> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<T> cq = cb.createQuery(entityClass)
        Root<T> c = cq.from(T.class)
        cq.select(c).where(cb.equal(c.get("container").<Namespace>get("namespace").<String>get("nsKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }
}
