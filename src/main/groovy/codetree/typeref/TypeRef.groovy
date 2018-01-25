package codetree.typeref

import codetree.AbstractTypeRef
import codetree.node.type.TypeNode
import groovy.transform.builder.Builder

@Builder(buildMethodName = "create")
class TypeRef extends AbstractTypeRef {

    TypeNode type
    List<AbstractTypeRef> typeArgs = []

    String toString() {
        String typeargs = ""
        if (!typeArgs.empty) {
            typeargs = "<"
            typeArgs.each {arg -> typeargs + "${arg.toString()}, "}
            typeargs = typeargs.substring(0, typeargs.lastIndexOf(","))
            typeargs += ">"
        }
        "${type.name()} ${typeargs}"
    }
}
