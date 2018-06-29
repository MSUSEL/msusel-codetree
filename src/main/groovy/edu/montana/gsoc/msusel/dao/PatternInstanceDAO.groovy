package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.pattern.PatternInstance
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface PatternInstanceDAO extends GenericDAO<PatternInstance, Long> {
    List<Type> findTypes(PatternInstance patternInstance)
}
