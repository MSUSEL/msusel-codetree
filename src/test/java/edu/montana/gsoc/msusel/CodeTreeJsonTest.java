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
package edu.montana.gsoc.msusel;

import edu.montana.gsoc.msusel.CodeTree;
import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.FileNode;
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.ProjectNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class CodeTreeJsonTest {

    public static void main(String args[])
    {
        ProjectNode pn = ProjectNode.builder("project").metric("TEST", 4.0).metric("TEST2", 10.0).create();

        FileNode fn = FileNode.builder("/home/git/test")
                .metric("TEST", 1.0)
                .metric("TEST2", 2.0)
                .type(
                        TypeNode.builder("Class", "Class")
                                .range(1, 100)
                                .metric("TEST1", 1.0)
                                .method(
                                        MethodNode.builder("Method", "Method")
                                                .range(50, 100)
                                                .metric("Test1", 1.0)
                                                .create())
                                .field(
                                        FieldNode.builder("TestField", "TestField")
                                                .range(25, 25)
                                                .metric("Test1", 1.0)
                                                .create())
                                .create())
                .create();

        FileNode f = (FileNode) FileNode.createFromJson(fn.toJSON());

        CodeTree tree = new CodeTree();
        tree.setProject(pn);
        pn.addFile(fn);

        System.out.println("\n" + tree.toJSON());

        CodeTree ct2 = CodeTree.createFromJson(tree.toJSON());

        // ProjectNode pn2 = ProjectNode.createFromJson(pn.toJSON());
        //
        // tree = new CodeTree();
        // tree.setProject("Bob");
        //
        // System.out.println("\n" + tree.toJSON());
        //
        // ct2 = CodeTree.createFromJson(tree.toJSON());

        System.out.println("\n\n" + ct2.toJSON());
    }
}
