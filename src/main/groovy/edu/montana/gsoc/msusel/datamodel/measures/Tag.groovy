package edu.montana.gsoc.msusel.datamodel.measures

import groovy.transform.builder.Builder

import javax.persistence.Embeddable

@Embeddable
@Builder(buildMethodName = "create")
class Tag {

    String tag
}
