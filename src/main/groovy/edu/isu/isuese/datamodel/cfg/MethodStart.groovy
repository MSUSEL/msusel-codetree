package edu.isu.isuese.datamodel.cfg

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MethodStart extends AbstractCFGNode {

    MethodStart() {
        super(StatementType.METHOD_START, 0)
    }

    String toString() {
        "METHSTRT:${super.toString()}"
    }
}
