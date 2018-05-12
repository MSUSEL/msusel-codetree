package edu.montana.gsoc.msusel.codetree.node.structural

import edu.montana.gsoc.msusel.codetree.INode
import edu.montana.gsoc.msusel.codetree.utils.CodeTreeUtils

class PatternNode extends StructuralNode {

    PatternNode(String key, String parentKey) {
        super(key, parentKey)
    }

    @Override
    def type() {
        return null
    }

    @Override
    def name() {
        return null
    }

    @Override
    void update(INode other) {

    }

    @Override
    INode cloneNoChildren() {
        return null
    }

    @Override
    def extractTree(Object tree) {
        return null
    }

    @Override
    def findParent(CodeTreeUtils utils) {
        return null
    }

    @Override
    def types() {
        return null
    }

    @Override
    def files() {
        return null
    }

    List modelBlocks() {

    }
}
