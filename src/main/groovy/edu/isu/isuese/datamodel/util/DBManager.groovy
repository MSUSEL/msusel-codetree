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

import java.sql.DatabaseMetaData
import java.sql.ResultSet

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class DBManager {

    public FluentLogger logger
    boolean open;

    List<String> tables = [
            'classes', 'classes_modifiers', 'constructors',
            'constructors_method_exceptions', 'constructors_modifiers',
            'destructors', 'destructors_method_exceptions',
            'destructors_modifiers', 'enums', 'enums_modifiers',
            'fields', 'fields_modifiers', 'files', 'findings',
            'imports', 'initializers', 'initializers_modifiers',
            'interfaces', 'interfaces_modifiers',
            'languages', 'literals', 'literals_modifiers',
            'measures', 'method_exceptions', 'methods',
            'methods_method_exceptions', 'methods_modifiers',
            'metric_repositories', 'metrics', 'modifiers',
            'modules', 'namespaces', 'parameters', 'parameters_modifiers',
            'pattern_chains', 'pattern_instances', 'pattern_repositories',
            'patterns', 'projects', 'projects_languages', 'refs',
            'relations', 'role_bindings', 'roles', 'rule_repositories',
            'rules', 'rules_tags', 'scms', 'systems', 'tags',
            'type_refs', 'unknown_types'
    ]

    def open(String driver, String url, String user, String pass) {
        if (open)
            return
        if (logger) logger.atInfo().log("Opening connection to the database")
        Base.open(driver, url, user, pass)
        if (logger) logger.atInfo().log("database connection open and ready.")
        open = true
    }

    def close() {
        if (!open)
            return
        if (logger) logger.atInfo().log("Closing connection to the database")
        Base.close()
        if (logger) logger.atInfo().log("Database connection closed.")
        open = false
    }

    void checkDatabaseAndCreateIfMissing(String dbType, String driver, String url, String user, String pass) {
        boolean missing = false
        println "dbType: $dbType"
        println "driver: $driver"
        println "url: $url"
        println "user: $user"
        println "pass: $pass"
        Sql.withInstance(url, user, pass, driver) { Sql sql ->
            DatabaseMetaData metaData = sql.connection.metaData

            tables.each { table ->
                ResultSet rs = metaData.getTables(null, null, table, null)
                if (!rs.next())
                    missing = true
            }
        }

        if (missing)
            createDatabase(dbType, driver, url, user, pass)
    }

    void createDatabase(String dbType, String driver, String url, String user, String pass) {
        resetDatabase(dbType, driver, url, user, pass)
    }

    void resetDatabase(String dbType, String driver, String url, String user, String pass) {
        if (logger) logger.atInfo().log("Resetting the database to empty")
        Sql.withInstance(url, user, pass, driver) { sql ->

            tables.each {
                ResultSet rs = sql.connection.metaData.getTables(null, null, it, null)
                if (rs.next())
                    sql.execute("drop table $it;")
            }

            def text = DBManager.class.getResourceAsStream("/edu/isu/isuese/datamodel/util/reset_${dbType.toLowerCase()}.sql").getText("UTF-8")
            String[] inst = text.split(";")

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (inst[i].trim() != "") {
                    sql.execute(inst[i])
                    System.out.println(">>" + inst[i])
                }
            }
        }
        if (logger) logger.atInfo().log("Database reset and ready for fresh data.")
    }
}