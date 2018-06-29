package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface FileDAO extends GenericDAO<File, Long> {
    List<Type> findTypes(File file)
}
