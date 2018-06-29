package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface NamespaceDAO extends GenericDAO<Namespace, Long> {
    List<File> findFiles(Namespace namespace)

    List<Type> findTypes(Namespace namespace)
}
