package edu.montana.gsoc.msusel.datamodel.inject

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.DefaultDataModelMediator
import edu.montana.gsoc.msusel.datamodel.JpaDataModelMediator

class DefaultDataModelModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(DataModelMediator.class).to(JpaDataModelMediator.class).in(Singleton.class)
        bind(DataModelMediator.class).to(DefaultDataModelMediator.class).in(Singleton.class)
        bind(DataModelUtils.class).to(JpaDataModelUtils.class)
        bind(DataModelUtils.class).to(DefaultDataModelUtils.class)
        bind()
    }
}
