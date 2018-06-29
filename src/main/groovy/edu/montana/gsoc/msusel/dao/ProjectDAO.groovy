package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Project
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface ProjectDAO extends GenericDAO<Project, Long> {
    List<File> findFiles(Project project)

    List<Method> findMethods(Project project)

    List<Type> findTypes(Project project)
}
