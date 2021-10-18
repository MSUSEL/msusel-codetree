/*
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
package edu.isu.isuese.datamodel.pattern

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternChain
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ChainIdentifier {

    MutableGraph<PatternInstance> graph

    ChainIdentifier() {
        graph = GraphBuilder.directed().build()
    }

    def findChains(System system) {
        if (!system)
            throw new IllegalArgumentException("findChains: System cannot be null.")

        def projects = system.getProjects()
        projects.sort {a, b -> a.version <=> b.version }

        for (int i = 0; i < projects.size() - 1; i++) {
            Project current = projects[i]
            Project next = projects[i + 1]

            Pattern.findAll().each { pattern ->
                List<PatternInstance> currInsts = PatternInstance.find("project_id = ? AND pattern_id = ?", current.getId(), pattern.getId())
                List<PatternInstance> nextInsts = PatternInstance.find("project_id = ? AND pattern_id = ?", next.getId(), pattern.getId())

                for (int currNdx = 0; currNdx < currInsts.size(); currNdx++) {
                    for (int nextNdx = 0; nextNdx < nextInsts.size(); nextNdx++) {
                        if (checkMatchingInstances(currInsts[currNdx], nextInsts[nextNdx])) {
                            createGraphEntry(currInsts[currNdx], nextInsts[nextNdx])
                            break
                        }
                    }
                }
            }
        }
    }

    def constructChains(System sys) {
        List<PatternInstance> chainStarts = graph.nodes().findAll { graph.inDegree(it) == 0 }.toList()

        chainStarts.each { start ->
            PatternChain chain = PatternChain.builder()
                    .chainKey(start.getInstKey() + "-chain")
                    .create()

            Stack<PatternInstance> stack = new Stack<>()
            stack.push(start)
            while (!stack.isEmpty()) {
                PatternInstance inst = stack.pop()
                chain.addInstance(inst)
                for (PatternInstance succ : graph.successors(inst))
                    stack.push(succ)
            }
            sys.addPatternChain(chain)
        }
    }

    boolean checkMatchingInstances(PatternInstance first, PatternInstance second) {
        List<String> firstNames = extractRoleNames(first)
        List<String> secondNames = extractRoleNames(second)

        return secondNames.containsAll(firstNames)
    }

    List<String> extractRoleNames(PatternInstance inst) {
        List<String> names = []

        String parentProjKey = inst.getParentProject().getProjectKey()
        inst.getRoleBindings().each {binding ->
            String refKey = binding.getReference().getRefKey()
            names << refKey.replace(parentProjKey + ":", "")
        }

        return names
    }

    void createGraphEntry(PatternInstance first, PatternInstance second) {
        if (!first || !second)
            return

        graph.addNode(first)
        graph.addNode(second)
        graph.putEdge(first, second)
    }
}
