/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
