package edu.isu.isuese.datamodel.cfg

import groovy.transform.EqualsAndHashCode

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@EqualsAndHashCode
abstract class AbstractCFGNode implements ControlFlowNode {

    StatementType type
    int label

    AbstractCFGNode(StatementType type, int label) {
        this.type = type
        this.label = label
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String toString() {
        "${type.toString()}_${label}"
    }
}
