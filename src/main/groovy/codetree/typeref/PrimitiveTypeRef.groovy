package codetree.typeref

import codetree.AbstractTypeRef
import groovy.transform.builder.Builder

@Builder(buildMethodName = "create")
class PrimitiveTypeRef extends AbstractTypeRef {

    String key;

    static PrimitiveTypeRef getInstance(String key) {
        MultitonHolder.map[key]
    }

    static def getTypes() {
        MultitonHolder.map.values()
    }

    private static class MultitonHolder {
        private static def map = [:]

        static {
            // Shared Java and C# Primitive Types
            map["int"] = builder().key("int").create()
            map["long"] = builder().key("long").create()
            map["char"] = builder().key("char").create()
            map["byte"] = builder().key("byte").create()
            map["short"] = builder().key("short").create()
            map["double"] = builder().key("double").create()
            map["float"]  = builder().key("float").create()
            map["void"] = builder().key("void").create()

            // Java Primitive Types
            map["boolean"] = builder().key("boolean").create()

            // C# Primitive Types
            map["sbyte"] = builder().key("sbyte").create()
            map["ushort"] = builder().key("ushort").create()
            map["uint"] = builder().key("uint").create()
            map["ulong"] = builder().key("ulong").create()
            map["object"] = builder().key("object").create()
            map["string"] = builder().key("string").create()
            map["decimal"] = builder().key("decimal").create()
            map["bool"] = builder().key("bool").create()
            map["DataTime"] = builder().key("DateTime").create()
            map["DateSpan"] = builder().key("DateSpan").create()
        }
    }

    String toString() {
        key
    }
}
