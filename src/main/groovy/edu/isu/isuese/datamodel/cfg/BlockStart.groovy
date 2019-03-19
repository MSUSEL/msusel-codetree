package edu.isu.isuese.datamodel.cfg

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class BlockStart extends AbstractCFGNode {

    @Builder(buildMethodName = "create")
    BlockStart(StatementType type, int label) {
        super(type, label)
    }

    boolean isLoop() {
        false
    }

    String toString() {
        "BLKSTRT:${super.toString()}"
    }
}
