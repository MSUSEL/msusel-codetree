/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/**
 * 
 */
package codetree

import codetree.node.FileNode
import codetree.node.NamespaceNode
import codetree.node.TypeNode
import com.sparqline.codetree.mmap.Tag

import java.security.SecureRandom

/**
 * @author Isaac Griffith
 *
 */
class CodeTreeTester {

    static def classNames = []
    static def fieldNames = []
    static def methodNames = []
    static def nsNames = []
    
    /**
     * 
     */
    public CodeTreeTester()
    {
        // TODO Auto-generated constructor stub
    }

    static main(args) {
        CodeTree tree = new CodeTree()
        MemberMMap.init()
        
        readNamespaceNames()
        readClassFileNames()
        readFieldNames()
        readMethodNames()
        
        println "Total Memory in Use (bytes): ${Runtime.getRuntime().totalMemory() / 1024 / 1024} MB"
        println "Free memory (bytes): ${Runtime.getRuntime().freeMemory() / 1024 / 1024} MB"
        println "Maximum memory (bytes): ${Runtime.getRuntime().maxMemory() / 1024 / 1024} MB"
        
        ProjectNode pn = NodeFactory.getProjectNode(randProjKey())
        tree.project = pn
        for (int i in 0..1500) { // namespaces
            NamespaceNode ns = NodeFactory.getNamespaceNode(randNSKey())
            pn.children << ns
            for (int j in 0..15) { // files
                FileNode fn = NodeFactory.getFileNode(randFileKey(ns.key))
                pn.children << fn
                for (int k in 0..3) { // types
                    TypeNode tn = null
                    Random rand = new Random()
                    int val = rand.nextInt(3) + 1
                    if (val == 1)
                        tn = NodeFactory.getClassNode(randTypeKey(ns.key))
                    else if (val == 2)
                        tn = NodeFactory.getInterfaceNode(randTypeKey(ns.key))
                    else
                        tn = NodeFactory.getEnumNode(randTypeKey(ns.key))
                        
                    fn.children << tn
                    ns.children << tn
                    
                    tree.addGeneralizes(tn, tn)
                    
                    for (int l in 0..20) { // members
                        val = rand.nextInt(2) + 1
                        Tag mn = null
                        if (val == 1)
                            mn = NodeFactory.getMethodNode(randMethodKey(tn.key))
                        else
                            mn = NodeFactory.getFieldNode(randFieldKey(tn.key))
                    }
                }
            }
        }
        
        println "Total Memory in Use (bytes): ${Runtime.getRuntime().totalMemory() / 1024 / 1024} MB"
        println "Free memory (bytes): ${Runtime.getRuntime().freeMemory() / 1024 / 1024} MB"
        println "Maximum memory (bytes): ${Runtime.getRuntime().maxMemory() / 1024 / 1024} MB"
        println "Total Nodes: ${NodeFactory.nodes.size()}"
        println "Nodes added to Member Map ${MemberMMap.getCount()}"
        
        MemberMMap.close();
    }

    
    static def readClassFileNames() {
        classNames = []
        def isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/classnames1.txt"))
        classNames = isr.readLines()
        isr.close()

        isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/classnames2.txt"))
        classNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/classnames3.txt"))
        classNames += isr.readLines()
        isr.close()
    }

    static def readFieldNames() {
        fieldNames = []
        def isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/fieldnames.txt"))
        fieldNames = isr.readLines()
        isr.close()
    }

    static def readMethodNames() {
        methodNames = []
        def isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/methodnames1.txt"))
        methodNames += isr.readLines()
        isr.close()

        methodNames = []
        isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/methodnames2.txt"))
        methodNames += isr.readLines()
        isr.close()
    }

    static def readNamespaceNames() {
        nsNames = []
        def isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/regionnames1.txt"))
        nsNames = isr.readLines()
        isr.close()

        isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/regionnames2.txt"))
        nsNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(CodeTreeTester.class.getResourceAsStream("/com/sparqline/pattern/gen/regionnames3.txt"))
        nsNames += isr.readLines()
        isr.close()
    }
    
    static def randProjKey() {
        return "Test"
    }
    
    static def randNSKey() {
        SecureRandom rand = new SecureRandom()
        
        int k = rand.nextInt(4) + 3
        Collections.shuffle(nsNames)
        def name = ''
        for (int i in 0..k) {
            name += nsNames[i]
            name += "."
        }
        
        name = name[0..-2]
        return name
    }
    
    static def randFileKey(nsName) {
        SecureRandom rand = new SecureRandom()
        
        int k = rand.nextInt(3) + 1
        Collections.shuffle(classNames)
        def name = nsName.replaceAll("\\.", "/") + "/"
        for (int i in 0..k)
            name += classNames[i].capitalize()
            
        return name + ".java"
    }
    
    static def randTypeKey(nsName) {
        SecureRandom rand = new SecureRandom()
        
        int k = rand.nextInt(3) + 1
        Collections.shuffle(classNames)
        def name = nsName + "."
        for (int i in 0..k)
            name += classNames[i].capitalize()
            
        return name
    }
    
    static def randFieldKey(typeName) {
        SecureRandom rand = new SecureRandom()
        
        int k = rand.nextInt(3) + 1
        Collections.shuffle(fieldNames)
        def name = typeName + "#"
        for (int i in 0..k)
            name += fieldNames[i].capitalize()
            
        return name
    }
    
    static def randMethodKey(typeName) {
        SecureRandom rand = new SecureRandom()
        
        int k = rand.nextInt(3) + 1
        Collections.shuffle(methodNames)
        def name = typeName + "#"
        for (int i in 0..k)
            name += methodNames[i].capitalize()
            
        return name
    }
}
