package edu.isu.isuese.datamodel.util

import com.google.common.flogger.FluentLogger
import groovy.sql.Sql
import org.javalite.activejdbc.Base

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

    void createDatabase(String dbType, String driver, String url, String user, String pass) {
        resetDatabase(dbType, driver, url, user, pass)
    }

    void resetDatabase(String dbType, String driver, String url, String user, String pass) {
        if (logger) logger.atInfo().log("Resetting the database to empty")
        Sql.withInstance(driver, url, user, pass) { sql ->

            tables.each {
                sql.execute("drop table $it;")

                def text = DBManager.class.getResourceAsStream("reset_${dbType.toLowerCase()}.sql").text
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
        }
        if (logger) logger.atInfo().log("Database reset and ready for fresh data.")
    }
}
