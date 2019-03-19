package edu.isu.isuese.datamodel.cfg

import com.google.common.collect.Queues
import com.google.common.graph.MutableGraph

/**
 * This class provides the utilities need to convert a CFG to a String and a String to a CFG for inclusion into a database
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CFG2StringUtils {

    def cfgToString(MutableGraph<ControlFlowNode> graph) {
        def build = new StringBuilder()
        graph.nodes().each { ControlFlowNode u ->
            graph.successors(u).each { ControlFlowNode v ->
                build << u.toString()
                build << '|->|'
                build << v.toString()
                build << ','
            }
        }
        return build.toString()
    }

    def stringToCFG(String string) {
        if (string.contains(',') && string.contains('|->|')) {

        }
    }
}
