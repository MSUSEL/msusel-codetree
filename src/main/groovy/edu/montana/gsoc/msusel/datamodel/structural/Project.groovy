/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
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
package edu.montana.gsoc.msusel.datamodel.structural

import com.google.common.collect.Sets
import edu.montana.gsoc.msusel.datamodel.SCM
import edu.montana.gsoc.msusel.datamodel.System
import edu.montana.gsoc.msusel.datamodel.measures.Finding
import edu.montana.gsoc.msusel.datamodel.measures.Measure
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.pattern.PatternInstance
import edu.montana.gsoc.msusel.datamodel.relations.Relation
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.builder.Builder

import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Table(name = "Project")
class Project extends Structure {

    @OneToMany(mappedBy = "project")
    List<Module> modules = []
    String version
    List<String> languages
    @OneToMany(mappedBy = "project")
    List<Measure> measures = []
    @OneToMany(mappedBy = "project")
    List<Finding> findings = []
    @OneToMany(mappedBy = "project")
    List<PatternInstance> patterns = []
    @OneToMany(mappedBy = "project")
    List<Relation> relations = []
    @OneToOne(mappedBy = "project")
    SCM scm

    @Builder(buildMethodName = "create")
    Project(String key, String name, String version, System system, List<Module> modules, List<String> languages, SCM scm) {
        super(key, name, system, [])
        this.version = version
        this.modules = modules
        this.languages = languages
        measures = []
        findings = []
        patterns = []
        relations = []
        this.scm = scm
    }

    Project leftShift(Relation relation) {
        addRelation(relation)
        this
    }

    Project plus(Relation relation) {
        addRelation(relation)
        this
    }

    Project minus(Relation relation) {
        removeRelation(relation)
    }

    void addRelation(Relation relation) {
        if (relation && !relations.contains(relation)) {
            relations << relation
            relation.project = this
        }
    }

    void removeRelation(Relation relation) {
        if (relation && relations.contains(relation)) {
            relations -= relation
            relation.project = null
        }
    }

    Project leftShift(Structure structure) {
        addStructure(structure)
        this
    }

    Project plus(Structure structure) {
        addStructure(structure)
        this
    }

    Project minus(Structure structure) {
        removeStructure(structure)
    }

    void addStructure(Structure structure) {
        if (structure && !structures.contains(structure)) {
            structures << structure
            structure.project = this
        }
    }

    void removeStructure(Structure structure) {
        if (structure && structures.contains(structure)) {
            structures -= structure
            structure.project = null
        }
    }

    Project leftShift(PatternInstance instance) {
        addPatternInstance(instance)
        this
    }

    Project plus(PatternInstance instance) {
        addPatternInstance(instance)
        this
    }

    Project minus(PatternInstance instance) {
        removePatternInstance(instance)
    }

    void addPatternInstance(PatternInstance instance) {
        if (instance && !instances.contains(instance)) {
            instances << instance
            instance.project = this
        }
    }

    void removePatternInstance(PatternInstance instance) {
        if (instance && instances.contains(instance)) {
            instances -= instance
            instance.project = null
        }
    }

    Project leftShift(Finding finding) {
        addFinding(finding)
        this
    }

    Project plus(Finding finding) {
        addFinding(finding)
        this
    }

    Project minus(Finding finding) {
        removeFinding(finding)
    }

    void addFinding(Finding finding) {
        if (finding && !findings.contains(finding)) {
            findings << finding
            finding.project = this
        }
    }

    void removeFinding(Finding finding) {
        if (finding && findings.contains(finding)) {
            findings -= finding
            finding.project = null
        }
    }

    Project leftShift(Measure measure) {
        addMeasure(measure)
        this
    }

    Project plus(Measure measure) {
        addMeasure(measure)
        this
    }

    Project minus(Measure measure) {
        removeMeasure(measure)
    }

    void addMeasure(Measure measure) {
        if (measure && !measures.contains(measure)) {
            measures << measure
            measure.project = this
        }
    }

    void removeMeasure(Measure measure) {
        if (measure && measures.contains(measure)) {
            measures -= measure
            measure.project = null
        }
    }

    Project() {
        super()
        modules = []
    }

    Project plus(Project p) {
        addModule(p)
        this
    }

    Project minus(Project p) {
        removeModule(p)
        this
    }

    Project leftShift(Project p) {
        addModule(p)
        this
    }

    Project plus(Namespace ns) {
        addNamespace(ns)
        this
    }

    Project minus(Namespace ns) {
        removeNamespace(ns)
        this
    }

    Project leftShift(Namespace ns) {
        addNamespace(ns)
        this
    }

    void addModule(Project p) {
        if (p && !modules.contains(p)) {
            modules << p
            p.parent = this
        }
    }

    void removeModule(Project p) {
        if (p && !modules.contains(p)) {
            modules -= p
            p.parent = null
        }
    }

    void addNamespace(Namespace ns) {
        if (ns && !namespaces.contains(ns)) {
            namespaces << ns
            ns.proj = this
        }
    }

    void removeNamespace(Namespace ns) {
        if (ns && namespaces.contains(ns)) {
            namespaces -= ns
            ns.proj = null
        }
    }

    /**
     * {@inheritDoc}
     */
    List<File> files() {
        List<File> files = []
        namespaces().each {
            files += it.files
        }

        files
    }
    
    List<Namespace> namespaces() {
        List<Namespace> namespaces = []
        modules.each {
            namespaces += it.namespaces
        }

        namespaces
    }

    /**
     * {@inheritDoc}
     */
    List<Type> types()
    {
        List<Type> types = []

        files().each { File file ->
            types += file.getTypes()
        }

        types
    }
    
    List<Method> methods() {
        List<Method> methods = []
        types().each { Type type ->
            methods += type.methods()
        }
        methods
    }

    /**
     * @return Set of file paths for the files contained in this project.
     */
    Set<String> getFileKeys()
    {
        def set = Sets.newHashSet()

        files().each { java.io.File fn -> set << fn.key}

        set
    }

    /**
     * @return true if this project has any subprojects, modules, or files
     */
    boolean hasChildren()
    {
        return !children.isEmpty()
    }

    /**
     * Searches this project for a subproject with the provided qualified
     * identifier.
     *
     * @param qIdentifier
     *            Identifier to search for modules with
     * @return true if this project contains a module with the provided
     *         qIdentifier, false otherwise.
     */
    boolean hasSubProject(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return false

        return subprojects().find { it instanceof Project && it.getKey() == qIdentifier } != null
    }
    
    /**
     * Searches this project for a module with the provided qualified
     * identifier.
     *
     * @param qIdentifier
     *            Identifier to search for modules with
     * @return true if this project contains a module with the provided
     *         qIdentifier, false otherwise.
     */
    boolean hasModule(String qIdentifier)
    {
        if (qIdentifier == null || qIdentifier.isEmpty())
            return false

        return modules().find { it instanceof Module && it.getKey() == qIdentifier } != null
    }

    /**
     * Searches this project for a file with the provided path and returns true
     * if such a file exists in this project.
     *
     * @param path
     *            Absolute path to search for.
     * @return true if a file exists, within this project, with the given path,
     *         false otherwise.
     */
    boolean hasFile(String path)
    {
        if (path == null || path.isEmpty())
            return false

        return files().find {it instanceof java.io.File && it.key == path} != null
    }
    
    /**
     * Checks whether this project contains a namespace matching the provided
     * qualified identifier.
     *
     * @param ns
     *            Qualified identifier
     * @return true if this project contains a namespace matching the provided
     *         qualified identifier, false otherwise.
     */
    boolean hasNamespace(String ns)
    {
        if (ns == null || ns.isEmpty())
            return false

        return namespaces().find {it instanceof Namespace && it.key == ns} != null
    }

    Namespace getNamespace(String ns) {
        if (ns == null || ns.isEmpty())
            return null

        return (Namespace) namespaces().find {it instanceof Namespace && it.key == ns}
    }
}
