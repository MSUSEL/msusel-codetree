package edu.isu.isuese.datamodel.cfg

import com.google.common.graph.Graph
import com.google.common.graph.MutableGraph
import edu.montana.gsoc.msusel.datamodel.utils.CFG2DOT

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ControlFlowGraph {

    MethodStart start
    MethodEnd end
    Graph<ControlFlowNode> graph

    ControlFlowGraph(graph, start, end) {
        this.start = start
        this.end = end
        this.graph = graph
    }

    String toDOT() {
        CFG2DOT.generateDot(graph)
    }

    def cfgToString() {
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

    static ControlFlowGraph fromString(String string) {
        MutableGraph<ControlFlowNode> graph
        ControlFlowNode start, end
        if (string.contains(':') && string.contains(',') && string.contains('|->|')) {
            def edges = string.split(',')
            edges.each { e ->
                nodes = e.split(/\|->\|/)
                def nodeU = processNode(nodes[0])
                def nodeV = processNode(nodes[1])

                graph.putEdge(nodeU, nodeV)
                if (nodeU instanceof MethodStart)
                    start = nodeU
                else if (nodeU instanceof MethodEnd)
                    end = nodeU
                else if (nodeV instanceof MethodStart)
                    start = nodeV
                else if (nodeV instanceof MethodEnd)
                    end = nodeV
            }
        }

        return new ControlFlowGraph(graph, start, end)
    }

    private def processNode(String desc) {
        ControlFlowNode cfn = ControlFlowNodeFactory.instance.createNode(desc)
        return cfn
    }
}
