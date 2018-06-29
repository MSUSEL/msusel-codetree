package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.structural.Module
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.type.Type

interface TypeDAO<T extends Type> extends ComponentDAO<T> {

    Module findModule(T aClass)

    Namespace findNamespace(T aClass)

    List<Type> findAllDescendents(T type)

    List<Type> findAllParents(T type)

    List<Type> findParentClasses(T type)
}