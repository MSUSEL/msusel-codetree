package edu.isu.isuese.datamodel.util;

import org.javalite.activejdbc.Base;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class DbUtils {

    private Map<String, String> properties;
    private boolean open = false;

    public void loadProperties(Map<String, String> properties) throws DbUtilsException {
        if (verifyProperties(properties))
            this.properties = properties;
        else
            throw new DbUtilsException("Missing Database Property");
    }

    private boolean verifyProperties(Map<String, String> properties) {
        return properties.containsKey(Constants.DB_DRIVER_KEY) &&
                properties.containsKey(Constants.DB_URL_KEY) &&
                properties.containsKey(Constants.DB_USER_KEY) &&
                properties.containsKey(Constants.DB_PASS_KEY);
    }

    public void openDbConnection() throws DbUtilsException {
        if (properties != null) {
            Base.open(properties.get(Constants.DB_DRIVER_KEY),
                    properties.get(Constants.DB_URL_KEY),
                    properties.get(Constants.DB_USER_KEY),
                    properties.get(Constants.DB_PASS_KEY));
            open = true;
        } else {
            throw new DbUtilsException("Database cannot be opened until Database connection properties are loaded.");
        }
    }

    public void closeDbConnection() throws DbUtilsException {
        if (open)
            Base.close();
        else
            throw new DbUtilsException("Cannot close a database connection that was never opened.");
    }
}
