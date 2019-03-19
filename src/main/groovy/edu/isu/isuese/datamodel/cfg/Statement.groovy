package edu.isu.isuese.datamodel.cfg

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Statement extends AbstractCFGNode {

    @Builder(buildMethodName = "create")
    Statement(StatementType type, int label) {
        super(type, label)
    }

    String toString() {
        "STMT:${super.toString()}"
    }
}
