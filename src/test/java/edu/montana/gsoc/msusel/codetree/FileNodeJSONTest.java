/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.codetree;

import edu.montana.gsoc.msusel.codetree.node.FieldNode;
import edu.montana.gsoc.msusel.codetree.node.FileNode;
import edu.montana.gsoc.msusel.codetree.node.MethodNode;
import edu.montana.gsoc.msusel.codetree.node.ProjectNode;
import edu.montana.gsoc.msusel.codetree.node.TypeNode;

/**
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class FileNodeJSONTest {

    public static void main(String args[])
    {
        ProjectNode pn = ProjectNode.builder("project").create();

        FileNode fn = FileNode.builder("/home/git/test")
                .metric("TEST", 1.0)
                .metric("TEST2", 2.0)
                .type(
                        TypeNode.builder()
                                .name("Class")
                                .identifier("Class")
                                .start(1)
                                .end(100)
                                .metric("TEST1", 1.0)
                                .method("Method",
                                        MethodNode.builder()
                                                .name("Method")
                                                .identifier("Method")
                                                .start(50)
                                                .end(100)
                                                .metric("Test1", 1.0)
                                                .create())
                                .field("TestField",
                                        FieldNode.builder()
                                                .name("TestField")
                                                .identifier("TestField")
                                                .start(25)
                                                .end(25)
                                                .metric("Test1", 1.0)
                                                .create())
                                .create())
                .create();

        System.out.println(fn.toJSON());

        FileNode f = (FileNode) FileNode.createFromJson(fn.toJSON());

        CodeTree tree = new CodeTree();
        tree.setProject(pn);
        pn.addFile(fn);

        System.out.println("\n" + tree.toJSON());

        CodeTree ct2 = CodeTree.createFromJson(tree.toJSON());

        ProjectNode pn2 = ProjectNode.createFromJson(pn.toJSON());
    }
}
