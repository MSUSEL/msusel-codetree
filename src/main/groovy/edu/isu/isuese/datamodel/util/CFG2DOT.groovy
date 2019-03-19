package edu.isu.isuese.datamodel.util

import com.google.common.graph.EndpointPair
import com.google.common.graph.Graph
import edu.isu.isuese.datamodel.cfg.ControlFlowNode
import lombok.extern.slf4j.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Slf4j
class CFG2DOT {

    static String generateDot(Graph<ControlFlowNode> graph, String name = null) {
        StringBuilder builder = new StringBuilder()
        if (name)
            builder.append("digraph \"" + name + "\" {\n")
        else
            builder.append("digraph {\n")

        for (EndpointPair<ControlFlowNode> pair : graph.edges()) {
            builder.append("  " + pair.source().getType() + "_" + pair.source().getLabel() + " -> " + pair.target().getType() + "_" + pair.target().getLabel() + "\n")
        }

        builder.append("}")

        builder.toString()
    }

    static void saveDOTFile(String filename, Graph<ControlFlowNode> graph, String name = "") {
        String dot = generateDot(graph, name)

        Path p = Paths.get(filename)
        PrintWriter pw = new PrintWriter(Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
        try {
            pw.print(dot)
        } catch (IOException e) {
            //log.error(e.getMessage())
        } finally {
            pw.close()
        }
    }
}