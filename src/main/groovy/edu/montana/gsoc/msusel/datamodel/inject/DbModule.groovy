package edu.montana.gsoc.msusel.datamodel.inject

import com.google.inject.AbstractModule
import com.google.inject.Provides
import edu.montana.gsoc.msusel.jpa.DatabaseProduct

import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
/**
 * Creates an EntityManagerFactory.
 * <p>
 * Configuration of the persistence units is taken from <code>META-INF/persistence.xml</code>
 * and other sources. Additional <code>hbm.xml</code> file names can be given to the
 * constructor.
 * </p>
 */
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DbModule extends AbstractModule {

    final String persistenceUnitName
    final String user
    final String pass
    final DatabaseProduct product
    final Map<String, String> properties = new HashMap<>()

    DbModule(DatabaseProduct databaseProduct, String persistenceUnitName) throws Exception {
        this.product = databaseProduct
        this.persistenceUnitName = persistenceUnitName
    }

    @Provides @Singleton
    EntityManagerFactory provideEntityManagerFactory() {
        // No automatic scanning by Hibernate, all persistence units list explicit classes/packages
        //properties["hibernate.archive.autodetection"] = "none"

        // Really the only way how we can get hbm.xml files into an explicit persistence
        // unit (where Hibernate scanning is disabled)
        //properties["hibernate.hbmxml.files"] = StringHelper.join(",", hbmResources != null ? hbmResources : new String[0])

        // We don't want to repeat these settings for all units in persistence.xml, so
        // they are set here programmatically
        properties["hibernate.use_sql_comments"] = "true"
        properties["hibernate.hbm2ddl.auto"] = "create"
        properties["hibernate.show_sql"] = "true"
        properties["hibernate.format_sql"] = "true"
        properties["hibernate.dialect"] = product.hibernateDialect

        Persistence.createEntityManagerFactory(persistenceUnitName, properties)
    }

    @Provides
    EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager()
    }

    void createSchema() {
        generateSchema("create")
    }

    void dropSchema() {
        generateSchema("drop")
    }

    void generateSchema(String action) {
        // Take exiting EMF properties, override the schema generation setting on a copy
        Map<String, String> createSchemaProperties = new HashMap<>(properties)
        createSchemaProperties["javax.persistence.schema-generation.database.action"] = action
        Persistence.generateSchema(persistenceUnitName, createSchemaProperties)
    }
}
