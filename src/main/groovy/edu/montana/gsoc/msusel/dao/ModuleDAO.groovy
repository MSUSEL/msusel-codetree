package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Module
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface ModuleDAO extends GenericDAO<Module, Long> {
    List<Type> findTypes(Module module)

    List<File> findFiles(Module module)

    List<Method> findMethods(Module module)
}
