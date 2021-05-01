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
package edu.isu.isuese.datamodel.pattern

import edu.isu.isuese.datamodel.PatternChain
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleBinding
import edu.isu.isuese.datamodel.System
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ChainIdentifier {

    def findChains(System system) {
        if (!system)
            throw new IllegalArgumentException("findChains: System cannot be null.")

        int index = 0
        def chains = system.getPatternChains()
        def projects = system.getProjects()
        if (!chains) {
            if (projects) {
                chains = createChains(projects.first().getPatternInstances())
            }
        }

        for (int i = 1; i < projects.size(); i++) {
            for (PatternInstance p : projects[i].getPatternInstances()) {
                PatternChain chain
                for (PatternChain c : chains) {
                    if (matches(c, p)) {
                        chain = c
                        break
                    }
                }
                if (chain) chain.addInstance(p)
                else createChain(p)
            }
        }
    }

    List<PatternChain> createChains(List<PatternInstance> insts) {
        if (insts == null)
            throw new IllegalArgumentException("createChains: pattern instance list cannot be null")

        List<PatternChain> chains = []
        insts.each {
            chains << createChain(it)
        }
        chains
    }

    PatternChain createChain(PatternInstance inst) {
        if (!inst)
            throw new IllegalArgumentException("createChain: pattern instance cannot be null")

        PatternChain chain = PatternChain.builder().chainKey(inst.getParentPattern().getName() + "-chain").create()
        chain.add(inst)
        inst.getParentProject().getParentSystem().add(chain)
        chain.updateKey()
        chain.refresh()
        chain
    }

    boolean matches(PatternChain chain, PatternInstance inst) {
        if (!chain)
            throw new IllegalArgumentException("matches: pattern chain cannot be null")
        if (!inst)
            throw new IllegalArgumentException("matches: pattern instance cannot be null")

        PatternInstance last = chain.instances.last()
        if (last.getParentPattern() != inst.getParentPattern()) {
            log.warn "Not same parent pattern"
            return false
        }

        Map<Role, List<String>> bindingMap = [:]
        String lastProjKey = last.getParentProject().getProjectKey()
        String instProjKey = inst.getParentProject().getProjectKey()

        last.getParentPattern().mandatoryRoles().each { Role r ->
            bindingMap[r] = []
            last.bindingsFor(r).each { RoleBinding rb ->
                if (rb.reference)
                    bindingMap[r] << rb.reference.refKey.replace(lastProjKey, "")
            }
            inst.bindingsFor(r).each { RoleBinding rb ->
                if (rb.reference)
                    bindingMap[r].remove(rb.reference.refKey.replace(instProjKey, ""))
            }
        }

        boolean retVal = true
        bindingMap.each { key, values ->
            retVal = retVal && values.isEmpty()
        }
        retVal
    }
}
