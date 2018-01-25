package codetree.typeref

import codetree.AbstractTypeRef
import groovy.transform.builder.Builder

@Builder(buildMethodName = "create")
class TypeVarTypeRef extends AbstractTypeRef {

    String typeVar

    String toString() {
        typeVar
    }
}
