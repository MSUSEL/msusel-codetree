package edu.isu.isuese.datamodel.cfg

import com.google.common.collect.Sets
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import edu.isu.isuese.datamodel.Method
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class CFGBuilder {

    MutableGraph<ControlFlowNode> currentCFG
    ControlFlowNode prevNode
    Stack<Pair<BlockStart, BlockEnd>> blocks
    Stack<Pair<MethodStart, MethodEnd>> methodNodes
    int stmtCount = 1
    def labeledStatements = [:]
    ControlFlowNode loopNext

    CFGBuilder() {
        currentCFG = GraphBuilder.directed().build()
        prevNode = null
        blocks = new Stack<>()
        methodNodes = new Stack<>()
    }

    void inject(BlockStart start, BlockEnd end) {
        Set<ControlFlowNode> outs = Sets.newHashSet(currentCFG.successors(prevNode))
        if (loopNext != null && outs.contains(loopNext))
            outs.remove(loopNext)

        // unless special case of if
        //if (!blocks.empty() && blocks.peek().getLeft().getType() != StatementType.IF) {
        for (ControlFlowNode out : outs) {
            currentCFG.removeEdge(prevNode, out)
        }
        //}

        currentCFG.putEdge(start, end)
        currentCFG.putEdge(prevNode, start)

        // Unless special case of Break, Continue, or Return
        for (ControlFlowNode out : outs) {
            currentCFG.putEdge(end, out)
        }

        prevNode = start

        blocks.push(Pair.of(start, end))
    }

    void inject(ControlFlowNode node) {
        Set<ControlFlowNode> outs = Sets.newHashSet(currentCFG.successors(prevNode))

        ControlFlowNode endNode = !blocks.empty() ? blocks.peek().getRight() : null
        if (endNode != null) {
            currentCFG.removeEdge(prevNode, endNode)
            currentCFG.putEdge(prevNode, node)
            currentCFG.putEdge(node, endNode)
        } else {
            currentCFG.putEdge(prevNode, node)
            for (ControlFlowNode out : outs) {
                currentCFG.removeEdge(prevNode, out)
                currentCFG.putEdge(node, out)
            }
        }

        prevNode = node
    }

    ControlFlowNode createStatement(StatementType type, String label = null, boolean jump = false, JumpTo jumpto = null) {
        ControlFlowNode s
        if (label != null && type == null) {
            s = LabeledStatement.builder().label(stmtCount++).codeLabel(label).create()
            labeledStatements.put(label, s)
        } else {
            s = Statement.builder().type(type).label(stmtCount++).create()
        }

        if (jump) {
            ControlFlowNode oldPrev = prevNode
            inject(s)
            prevNode = oldPrev
            Pair<BlockStart, BlockEnd> loop = findLoop()
            Set<ControlFlowNode> succ = Sets.newHashSet(currentCFG.successors(s))
            switch (jumpto) {
                case JumpTo.LABEL:
                    succ.each { currentCFG.removeEdge(s, it)}
                    currentCFG.putEdge(s, (ControlFlowNode) labeledStatements.get(label))
                    break
                case JumpTo.LOOP_END:
                    if (loop != null) {
                        succ.each { currentCFG.removeEdge(s, it)}
                        if (loopNext != null)
                            currentCFG.putEdge(s, loopNext)
                        else
                            currentCFG.putEdge(s, loop.getRight())
                    }
                    else {
                        succ.each { currentCFG.removeEdge(s, it) }
                        currentCFG.putEdge(s, blocks.peek().getRight())
                    }
                    break
                case JumpTo.LOOP_START:
                    succ.each { currentCFG.removeEdge(s, it)}
                    if (loop != null)
                        currentCFG.putEdge(s, loop.getLeft())
                    else
                        currentCFG.putEdge(s, blocks.peek().getLeft())
                    break
                case JumpTo.METHOD_END:
                    succ.each { currentCFG.removeEdge(s, it)}
                    currentCFG.putEdge(s, methodNodes.peek().getRight())
                    break
            }
        } else {
            inject(s)
        }

        s
    }

    private Pair<BlockStart, BlockEnd> findLoop() {
        Stack<Pair<BlockStart, BlockEnd>> temp = new Stack<>()
        Pair<BlockStart, BlockEnd> loop = null

        while (!blocks.empty()) {
            if (blocks.peek().left.isLoop()) {
                loop = blocks.peek()
                break
            }

            temp.push(blocks.pop())
        }

        while (!temp.empty())
            blocks.push(temp.pop())

        loop
    }

    void startLoop(StatementType type, boolean atLeastOne = false) {
        BlockStart bs = LoopStart.builder().type(type).label(stmtCount++).create()
        BlockEnd be = BlockEnd.builder().type(StatementType.END).label(stmtCount++).create()
        BlockEnd next = BlockEnd.builder().type(StatementType.END).label(stmtCount++).create()

        if (atLeastOne)
            inject(bs, be)
        else {
            inject(bs, next)
            currentCFG.putEdge(bs, be)
            blocks.pop()
            blocks.push(Pair.of(bs, be))
//            Set<ControlFlowNode> set = Sets.newHashSet(currentCFG.successors(be))
//
//            for (ControlFlowNode node : set) {
//                currentCFG.putEdge(bs, node)
//                currentCFG.removeEdge(be, node)
//            }
            loopNext = next
        }
        currentCFG.putEdge(be, bs)

        prevNode = bs
    }

    void endLoop(atLeastOne = false) {
        Pair<BlockStart, BlockEnd> pair = blocks.peek()

        endBlock()

        if (!atLeastOne)
            prevNode = pair.getLeft()
    }

    void startDecision(StatementType type) {
        BlockStart bs = BlockStart.builder().type(type).label(stmtCount++).create()
        BlockEnd be = BlockEnd.builder().type(StatementType.END).label(stmtCount++).create()

        inject(bs, be)
    }

    void endDecision(boolean hasDefault = true) {
        if (hasDefault)
            currentCFG.putEdge(blocks.peek().getLeft(), blocks.peek().getRight())

        endBlock()
    }

    void startBlock(StatementType type, StatementType endType = StatementType.END) {
        BlockStart bs = BlockStart.builder().type(type).label(stmtCount++).create()
        BlockEnd be = BlockEnd.builder().type(endType).label(stmtCount++).create()

        inject(bs, be)
    }

    void endBlock() {
        Pair<BlockStart, BlockEnd> p = blocks.pop()
        loopNext = null

        if (!blocks.empty() && (blocks.peek().getLeft().getType() == StatementType.IF || blocks.peek().getLeft().getType() == StatementType.SWITCH)) {
            prevNode = blocks.peek().getLeft()
        }
        else
            prevNode = p.getRight()
    }

    void startMethod() {
        currentCFG = GraphBuilder.directed().build()
        MethodStart ms = new MethodStart()
        MethodEnd me = new MethodEnd()

        methodNodes.push(Pair.of(ms, me))

        currentCFG.addNode(ms)
        currentCFG.addNode(me)
        currentCFG.putEdge(ms, me)

        prevNode = ms
    }

    void endMethod(Method method) {
        Pair<MethodStart, MethodEnd> pair = methodNodes.pop()
        method.setCfg(new ControlFlowGraph(currentCFG, pair.getLeft(), pair.getRight()))

        //println(CFG2DOT.generateDot(currentCFG, (String)methods.peek().name()))

        currentCFG = null
        prevNode = null
        stmtCount = 1
    }


    void createReturnStatement() {
        ControlFlowNode s = createStatement(StatementType.RETURN, null, true, JumpTo.METHOD_END)
        def others = currentCFG.successors(s).findAll { it != methodNodes.peek().getRight() }
        others.each { currentCFG.removeEdge(s, it) }
    }

    void exitSwitchBlock() {
        def succ = currentCFG.successors(prevNode).find { it.type == StatementType.BREAK }

        if (succ != null) {
            prevNode = blocks.peek().getLeft()
        }
    }

    void startDecisionBlock() {
        prevNode = blocks.peek().getLeft()
    }
}
