/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.isu.isuese.datamodel.cfg

import com.google.common.graph.Graph
import com.google.common.graph.MutableGraph

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