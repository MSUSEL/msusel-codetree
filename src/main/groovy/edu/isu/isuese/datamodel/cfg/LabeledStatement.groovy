package edu.isu.isuese.datamodel.cfg

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class LabeledStatement extends AbstractCFGNode {

    String codeLabel

    @Builder(buildMethodName = "create")
    LabeledStatement(String codeLabel, int label) {
        super(StatementType.LABELED, label)
        this.codeLabel = codeLabel
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String toString() {
        "LABEL:${super.toString()}:$codeLabel"
    }
}
