package edu.isu.isuese.datamodel.cfg

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class LoopStart extends BlockStart {

    @Builder(buildMethodName = "create")
    LoopStart(StatementType type, int label) {
        super(type, label)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isLoop() {
        true
    }

    String toString() {
        "LOOPSTRT:${super.toString()}"
    }
}
