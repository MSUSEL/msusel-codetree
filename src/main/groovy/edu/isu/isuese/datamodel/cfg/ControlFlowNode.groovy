package edu.isu.isuese.datamodel.cfg

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface ControlFlowNode {

    int getLabel()

    StatementType getType()
}
