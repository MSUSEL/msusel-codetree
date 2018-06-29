package edu.montana.gsoc.msusel.datamodel.inject

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.JpaDataModelMediator

class JpaDataModelModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(DataModelMediator.class).to(JpaDataModelMediator.class).in(Singleton.class)
        bind(DataModelUtils.class).to(JpaDataModelUtils.class)
    }
}
