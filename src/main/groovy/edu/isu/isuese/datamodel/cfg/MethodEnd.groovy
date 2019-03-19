package edu.isu.isuese.datamodel.cfg

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MethodEnd extends AbstractCFGNode {

    MethodEnd() {
        super(StatementType.METHOD_END, 0)
    }

    String toString() {
        "METHEND:${super.toString()}"
    }
}
