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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.montana.gsoc.msusel.codetree.node.FieldNode;
import edu.montana.gsoc.msusel.codetree.node.FileNode;
import edu.montana.gsoc.msusel.codetree.node.MethodNode;
import edu.montana.gsoc.msusel.codetree.node.ProjectNode;
import edu.montana.gsoc.msusel.codetree.node.TypeNode;

/**
 * The class <code>CodeTreeTest</code> contains tests for the class
 * <code>{@link CodeTree}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class CodeTreeTest {

    CodeTree fixture;

    /**
     * Run the CodeTree() constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testCodeTree_1() throws Exception
    {

        final CodeTree result = new CodeTree();

        // add additional test code here
        Assert.assertNotNull(result);
    }

    @Test
    public void testJson() throws Exception
    {
        ProjectNode pn = ProjectNode.builder("project").metric("TEST", 4.0).metric("TEST2", 10.0).create();

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

        CodeTree tree = new CodeTree();
        tree.setProject(pn);
        pn.addFile(fn);

        CodeTree ct2 = CodeTree.createFromJson(tree.toJSON());
        assertEquals(tree, ct2);
    }

    @Test
    public void testExtractTree() throws Exception
    {
        FileNode file1;
        FileNode file2;

        fixture = new CodeTree();
        fixture.setProject("project");

        ProjectNode pn = ProjectNode.builder("project")
                .project(ProjectNode.builder("subproject1")
                        .file(
                                file1 = FileNode.builder("path1")
                                        .type(
                                                TypeNode.builder()
                                                        .name("Type1")
                                                        .identifier("path1.namespace.Type1")
                                                        .start(100)
                                                        .end(150)
                                                        .method("method1",
                                                                MethodNode.builder()
                                                                        .name("method1")
                                                                        .identifier("path1.namespace.Type1#method1")
                                                                        .start(110)
                                                                        .end(150)
                                                                        .create())
                                                        .create())
                                        .create())
                        .file(
                                file2 = FileNode.builder("path2")
                                        .type(
                                                TypeNode.builder()
                                                        .name("Type2")
                                                        .identifier("path2.namespace.Type2")
                                                        .start(100)
                                                        .end(150)
                                                        .method("method2",
                                                                MethodNode.builder()
                                                                        .name("method2")
                                                                        .identifier("path2.namespace.Type2#method2")
                                                                        .start(110)
                                                                        .end(150)
                                                                        .create())
                                                        .create())
                                        .create())
                        .create())
                .project(
                        ProjectNode.builder("subproject2")
                                .file(FileNode.builder("path3").create())
                                .file(FileNode.builder("path4").create())
                                .create())
                .create();

        FileNode file5 = fixture.getUtils().findFile("path1");
        assertNotNull(file5);
        Assert.assertEquals("subproject1", file1.getParentKey());
        CodeTree test = fixture.getUtils().extractTree(file1);

        System.out.println(test.toJSON());
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Before
    public void setUp() throws Exception
    {
        fixture = new CodeTree();

        ProjectNode pn = ProjectNode.builder("project")
                .file(
                        FileNode.builder("path1")
                                .type(
                                        TypeNode.builder()
                                                .name("Type1")
                                                .identifier("path1.namespace.Type1")
                                                .start(100)
                                                .end(150)
                                                .method("method1",
                                                        MethodNode.builder()
                                                                .name("method1")
                                                                .identifier("path1.namespace.Type1#method1")
                                                                .start(110)
                                                                .end(150)
                                                                .create())
                                                .create())
                                .create())
                .file(
                        FileNode.builder("path2")
                                .type(
                                        TypeNode.builder()
                                                .name("Type2")
                                                .identifier("path2.namespace.Type2")
                                                .start(100)
                                                .end(150)
                                                .method("method2",
                                                        MethodNode.builder()
                                                                .name("method2")
                                                                .identifier("path2.namespace.Type2#method2")
                                                                .start(110)
                                                                .end(150)
                                                                .create())
                                                .create())
                                .create())
                .create();
        fixture.setProject(pn);
    }

    /**
     * Perform post-test clean-up.
     *
     * @throws Exception
     *             if the clean-up fails for some reason
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @After
    public void tearDown() throws Exception
    {
        // Add additional tear down code here
    }

    /**
     * Launch the test.
     *
     * @param args
     *            the command line arguments
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    public static void main(final String[] args)
    {
        new org.junit.runner.JUnitCore().run(CodeTreeTest.class);
    }
}