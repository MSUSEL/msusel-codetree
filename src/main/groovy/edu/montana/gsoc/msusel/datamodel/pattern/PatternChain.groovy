package edu.montana.gsoc.msusel.datamodel.pattern

import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id", "system", "bindings"])
class PatternChain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String chainKey
    @ManyToOne(fetch = FetchType.LAZY)
    System system
    @OneToMany
    List<PatternInstance> patterns

    PatternChain leftShift(PatternInstance instance) {
        addPatternInstance(instance)
        this
    }

    PatternChain plus(PatternInstance instance) {
        addPatternInstance(instance)
        this
    }

    PatternChain minus(PatternInstance instance) {
        removePatternInstance(instance)
    }

    void addPatternInstance(PatternInstance instance) {
        if (instance && !patterns.contains(instance)) {
            patterns << instance
        }
    }

    void removePatternInstance(PatternInstance instance) {
        if (instance && patterns.contains(instance)) {
            patterns -= instance
        }
    }
}
