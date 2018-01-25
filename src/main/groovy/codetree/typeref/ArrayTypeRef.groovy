package codetree.typeref

import codetree.AbstractTypeRef
import groovy.transform.builder.Builder

@Builder(buildMethodName = "create")
class ArrayTypeRef extends AbstractTypeRef {

    AbstractTypeRef ref
    String dimensions

    String toString() {
        "${ref.toString()} + $dimensions"
    }
}
