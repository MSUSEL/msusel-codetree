package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.structural.File

interface ComponentDAO<T> extends GenericDAO<T, Long> {

    File findParentFile(T child)
}
