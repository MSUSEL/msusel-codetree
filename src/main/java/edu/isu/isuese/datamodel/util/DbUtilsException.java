package edu.isu.isuese.datamodel.util;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class DbUtilsException extends Exception {
    public DbUtilsException() {
        super();
    }

    public DbUtilsException(String message) {
        super(message);
    }

    public DbUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbUtilsException(Throwable cause) {
        super(cause);
    }

    protected DbUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
