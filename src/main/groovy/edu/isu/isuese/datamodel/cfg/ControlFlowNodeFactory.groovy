package edu.isu.isuese.datamodel.cfg

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class ControlFlowNodeFactory {

    def createNode(String desc) {
        def contents = desc.split(/_/)

        def type = contents[0]
        def label = contents[1]

        def nodetype = type.split(":")[0]
        def stmttype = type.split(":")[1]

        switch (nodetype) {
            case NodeType.BLOCKEND:
                return BlockEnd.builder().type(StatementType.valueOf(stmttype)).label(Integer.parseInt(label)).create()
            case NodeType.BLOCKSTART:
                return BlockStart.builder().type(StatementType.valueOf(stmttype)).label(Integer.parseInt(label)).create()
            case NodeType.LABELED:
                int intLabel = Integer.parseInt(label.split(":")[0])
                String codeLabel = label.split(":")[1]
                return LabeledStatement.builder().label(intLabel).codeLabel(codeLabel).create()
            case NodeType.LOOPSTART:
                return LoopStart.builder().type(StatementType.valueOf(stmttype)).label(Integer.parseInt(label)).create()
            case NodeType.METHODEND:
                return new MethodEnd()
            case NodeType.METHODSTART:
                return new MethodStart()
            case NodeType.STATEMENT:
                return Statement.builder().type(StatementType.valueOf(stmttype)).label(Integer.parseInt(label)).create()
        }
    }
}
