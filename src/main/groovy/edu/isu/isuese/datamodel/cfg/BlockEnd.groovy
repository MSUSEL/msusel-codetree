package edu.isu.isuese.datamodel.cfg

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class BlockEnd extends AbstractCFGNode {

    @Builder(buildMethodName = "create")
    BlockEnd(StatementType type, int label) {
        super(type, label)
    }

    String toString() {
        "BLKEND:${super.toString()}"
    }
}
