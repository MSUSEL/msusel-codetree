package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Modifier extends Model {

    public enum Values {
       STATIC,
       FINAL,
       ABSTRACT,
       NATIVE,
       STRICTFP,
       SYNCHRONIZED,
       TRANSIENT,
       VOLATILE,
       DEFAULT,
       ASYNC,
       CONST,
       EXTERN,
       READONLY,
       SEALED,
       UNSAFE,
       VIRTUAL,
       OUT,
       REF,
       PARAMS,
       OVERRIDE,
       NEW,
       PARTIAL,
       EXPLICIT,
       IMPLICIT,
       YIELD,
       THIS
    }
}
