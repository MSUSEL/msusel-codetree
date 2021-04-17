/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package edu.isu.isuese.datamodel.util

import com.google.common.flogger.FluentLogger
import groovy.sql.Sql
import org.javalite.activejdbc.Base
import org.javalite.activejdbc.DBException

import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class DBManager {

    public FluentLogger logger
    boolean open
    ReadWriteLock lock = new ReentrantReadWriteLock()

    List<String> tables = [
            'classes', 'classes_measures', 'classes_modifiers', 'classes_template_params',
            'constructors', 'constructors_measures', 'constructors_method_exceptions', 'constructors_modifiers', 'constructors_template_params', 'constructors_typerefs',
            'destructors', 'destructors_measures', 'destructors_method_exceptions', 'destructors_modifiers', 'destructors_template_params', 'destructors_typerefs',
            'enums', 'enums_measures', 'enums_modifiers', 'enums_template_params',
            'fields', 'fields_modifiers', 'fields_template_params', 'fields_typerefs',
            'files', 'files_imports', 'files_measures',
            'findings', 'finding_data', 'finding_data_points',
            'imports',
            'initializers', 'initializers_measures', 'initializers_modifiers',
            'interfaces', 'interfaces_measures', 'interfaces_modifiers', 'interfaces_template_params',
            'languages',
            'literals', 'literals_modifiers',
            'measures',
            'method_exceptions', 'methodexceptions_typerefs',
            'methods', 'methods_measures', 'methods_method_exceptions', 'methods_modifiers', 'methods_template_params', 'methods_typerefs',
            'metric_repositories', 'metrics', 'metrics_measures',
            'modifiers',
            'modules', 'modules_measures',
            'namespaces', 'namespaces_measures',
            'parameters', 'parameters_modifiers', 'parameters_typerefs',
            'pattern_chains', 'pattern_instances', 'pattern_instances_measures', 'pattern_repositories', 'patterns',
            'projects', 'projects_findings', 'projects_languages', 'projects_measures',
            'refs',
            'relations',
            'role_bindings', 'roles', 'roles_role_bindings',
            'rule_repositories', 'rules', 'rules_findings', 'rules_tags',
            'scms',
            'systems', 'systems_measures',
            'tags',
            'template_params', 'template_params_typerefs',
            'type_refs', 'typerefs_typerefs',
            'unknown_types'
    ]

    def open(DBCredentials creds) {
        lock.writeLock().lock()
//        if (open)
//            return
        if (logger) logger.atInfo().log("Opening connection to the database")
        try { Base.open(creds.driver, creds.url, creds.user, creds.pass) }
        catch (DBException ex) { return }
        if (logger) logger.atInfo().log("database connection open and ready.")
        open = true
    }

    def close() {
//        if (!open)
//            return
        if (logger) logger.atInfo().log("Closing connection to the database")
        Base.close()
        if (logger) logger.atInfo().log("Database connection closed.")
        open = false
        lock.writeLock().unlock()
    }

    void checkDatabaseAndCreateIfMissing(DBCredentials creds) {
        boolean missing = false
        println "dbType: ${creds.type}"
        println "driver: ${creds.driver}"
        println "url: ${creds.url}"
        println "user: ${creds.user}"
        println "pass: ${creds.pass}"
        Sql.withInstance(creds.url, creds.user, creds.pass, creds.driver) { Sql sql ->
            DatabaseMetaData metaData = sql.connection.metaData

            tables.each { table ->
                ResultSet rs = metaData.getTables(null, null, table, null)
                if (!rs.next())
                    missing = true
            }
        }

        if (missing)
            createDatabase(creds)
    }

    void createDatabase(DBCredentials cred) {
        resetDatabase(cred)
    }

    void resetDatabase(DBCredentials creds) {
        if (logger) logger.atInfo().log("Resetting the database to empty")
        Sql.withInstance(creds.url, creds.user, creds.pass, creds.driver) { sql ->

//            tables.each {
//                println("Table: $it")
//                ResultSet rs = sql.connection.metaData.getTables(null, null, it, null)
//                if (rs.next())
//                    sql.execute("drop table $it")
//            }
            sql.execute("drop all;")

            def text = DBManager.class.getResourceAsStream("/edu/isu/isuese/datamodel/util/reset_${creds.type.toLowerCase()}.sql").getText("UTF-8")
            String[] inst = text.split(";")

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (inst[i].trim() != "") {
                    sql.execute(inst[i])
                }
            }
        }
        if (logger) logger.atInfo().log("Database reset and ready for fresh data.")
    }
}
