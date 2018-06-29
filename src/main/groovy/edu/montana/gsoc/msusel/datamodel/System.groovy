/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.datamodel

import edu.montana.gsoc.msusel.datamodel.measures.Measurable
import edu.montana.gsoc.msusel.datamodel.pattern.PatternChain
import edu.montana.gsoc.msusel.datamodel.structural.Project
import groovy.transform.builder.Builder

import javax.persistence.*

@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
class System implements Measurable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String sysKey
    String name
    @OneToMany(mappedBy = "system")
    List<Project> projects = []
    @OneToMany(mappedBy = "system")
    List<PatternChain> patternChains

    System leftShift(PatternChain chain) {
        addPatternChain(chain)
        this
    }

    System plus(PatternChain chain) {
        addPatternChain(chain)
        this
    }

    System minus(PatternChain chain) {
        removePatternChain(chain)
    }

    void addPatternChain(PatternChain chain) {
        if (chain && !patternChains.contains(chain)) {
            patternChains << chain
        }
    }

    void removePatternChain(PatternChain chain) {
        if (chain && patternChains.contains(chain)) {
            patternChains -= chain
        }
    }

    System leftShift(Project project) {
        addProject(project)
        this
    }

    System plus(Project project) {
        addProject(project)
        this
    }

    System minus(Project project) {
        removeProject(project)
    }

    void addProject(Project project) {
        if (project && !projects.contains(project)) {
            projects << project
        }
    }

    void removeProject(Project project) {
        if (project && projects.contains(project)) {
            projects -= project
        }
    }
    
    @Override
    String key() {
        sysKey
    }

    @Override
    String name() {
        name
    }
}
