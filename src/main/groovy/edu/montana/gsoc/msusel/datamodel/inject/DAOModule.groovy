package edu.montana.gsoc.msusel.datamodel.inject

import com.google.inject.AbstractModule
import edu.montana.gsoc.msusel.dao.*
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DAOModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClassDAO.class).to(ClassDAOImpl.class)
        bind(EnumDAO.class).to(EnumDAOImpl.class)
        bind(EventDAO.class).to(EventDAOImpl.class)
        bind(StructDAO.class).to(StructDAOImpl.class)
        bind(ConstructorDAO.class).to(ConstructorDAOImpl.class)
        bind(DestructorDAO).to(DestructorDAOImpl.class)
        bind(FieldDAO.class).to(FieldDAOImpl.class)
        bind(PropertyDAO.class).to(PropertyDAOImpl.class)
        bind(FileDAO.class).to(FileDAOImpl.class)
        bind(FindingDAO.class).to(FindingDAOImpl.class)
        bind(InitializerDAO.class).to(InitializerDAOImpl.class)
        bind(InterfaceDAO.class).to(InterfaceDAOImpl.class)
        bind(LiteralDAO.class).to(LiteralDAOImpl.class)
        bind(MeasureDAO.class).to(MeasureDAOImpl.class)
        bind(MethodDAO.class).to(MethodDAOImpl.class)
        bind(MetricDAO.class).to(MetricDAOImpl.class)
        bind(MetricRepositoryDAO.class).to(MetricRepositoryDAOImpl.class)
        bind(ModuleDAO.class).to(ModuleDAOImpl.class)
        bind(NamespaceDAO.class).to(NamespaceDAOImpl.class)
        bind(PatternChainDAO.class).to(PatternChainDAOImpl.class)
        bind(PatternDAO.class).to(PatternDAOImpl.class)
        bind(PatternInstanceDAO.class).to(PatternInstanceDAOImpl.class)
        bind(PatternRepositoryDAO.class).to(PatternRepositoryDAOImpl.class)
        bind(ProjectDAO.class).to(ProjectDAOImpl.class)
        bind(RelationDAO.class).to(RelationDAOImpl.class)
        bind(RoleDAO.class).to(RoleDAOImpl.class)
        bind(RuleDAO.class).to(RuleDAOImpl.class)
        bind(RuleRepositoryDAO.class).to(RuleRepositoryDAOImpl.class)
        bind(SystemDAO.class).to(SystemDAOImpl.class)
        bind(SCMDAO.class).to(SCMDAOImpl.class)
    }
}
