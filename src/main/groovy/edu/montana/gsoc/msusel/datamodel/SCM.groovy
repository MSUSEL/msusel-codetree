package edu.montana.gsoc.msusel.datamodel

import edu.montana.gsoc.msusel.datamodel.structural.Project
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*

@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id"])
class SCM {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String scmKey
    String tag
    String branch
    String url
    @Embedded
    SCMType type
    @OneToOne(mappedBy = "scm")
    Project project
}
