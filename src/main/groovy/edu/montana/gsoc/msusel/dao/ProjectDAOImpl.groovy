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

import com.google.inject.persist.Transactional
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Module
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.structural.Project
import edu.montana.gsoc.msusel.datamodel.type.Type

import javax.persistence.NonUniqueResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ProjectDAOImpl extends GenericDAOImpl<Project, Long> implements ProjectDAO {

    ProjectDAOImpl() {
        super(Project.class)
    }

    @Transactional
    System findParent(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<System> f = em.getCriteriaBuilder().createQuery(System.class)
            Root<Project> r = f.from(entityClass)
            f.select(r.<System>get("system")).where(cb.equal(r.get("projKey"), key))
            return em.createQuery(f).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    Project findByKey(String key) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder()
            CriteriaQuery<Project> c = em.getCriteriaBuilder().createQuery(entityClass)
            Root<Project> p = c.from(entityClass)
            c.select(p).where(cb.equal(p.get("projKey"), key))
            return em.createQuery(c).getSingleResult()
        } catch (NonUniqueResultException ex) {
            return null
        }
    }

    @Transactional
    @Override
    List<Project> findAll(String ownerKey) {
        CriteriaBuilder cb = em.getCriteriaBuilder()
        CriteriaQuery<Project> cq = cb.createQuery(entityClass)
        Root<Project> c = cq.from(entityClass)
        cq.select(c).where(cb.equal(c.<System>get("system").<String>get("sysKey"), ownerKey))
        return em.createQuery(cq).getResultList()
    }

    @Transactional
    @Override
    List<Type> findTypes(Project project) {
        project = em.merge(project)
        List<Module> mods = project.getModules()
        List<Type> types = []
        mods.each { Module m ->
            m.getNamespaces().each {
                types += it.types()
            }
        }
        types
    }

    @Transactional
    @Override
    List<File> findFiles(Project project) {
        project = em.merge(project)
        List<Module> mods = project.getModules()
        List<File> files = []
        mods.each { Module m ->
            m.getNamespaces().each {
                files += it.files()
            }
        }
        files
    }

    @Transactional
    @Override
    List<Method> findMethods(Project project) {
        project = em.merge(project)
        List<Module> mods = project.getModules()
        List<Method> methods = []
        mods.each { Module m ->
            m.getNamespaces().each { Namespace n ->
                n.types().each { Type t ->
                    methods += t.methods()
                }
            }
        }
        methods
    }
}
