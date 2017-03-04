package com.sparqline.codetree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sparqline.codetree.node.FieldNode;
import com.sparqline.codetree.node.FileNode;
import com.sparqline.codetree.node.MethodNode;
import com.sparqline.codetree.node.ProjectNode;
import com.sparqline.codetree.node.TypeNode;

/**
 * The class <code>CodeTreeTest</code> contains tests for the class
 * <code>{@link CodeTree}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author fate
 * @version $Revision: 1.0 $
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
                                                TypeNode.builder("Type1", "path1.namespace.Type1")
                                                        .range(100, 150)
                                                        .method(
                                                                MethodNode.builder(
                                                                        "method1", "path1.namespace.Type1#method1")
                                                                        .range(110, 150)
                                                                        .create())
                                                        .create())
                                        .create())
                        .file(
                                file2 = FileNode.builder("path2")
                                        .type(
                                                TypeNode.builder("Type2", "path2.namespace.Type2")
                                                        .range(100, 150)
                                                        .method(
                                                                MethodNode.builder(
                                                                        "method2", "path2.namespace.Type2#method2")
                                                                        .range(110, 150)
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
        assertEquals("subproject1", file1.getParentID());
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
                                        TypeNode.builder("Type1", "path1.namespace.Type1")
                                                .range(100, 150)
                                                .method(
                                                        MethodNode.builder("method1", "path1.namespace.Type1#method1")
                                                                .range(110, 150)
                                                                .create())
                                                .create())
                                .create())
                .file(
                        FileNode.builder("path2")
                                .type(
                                        TypeNode.builder("Type2", "path2.namespace.Type2")
                                                .range(100, 150)
                                                .method(
                                                        MethodNode.builder("method2", "path2.namespace.Type2#method2")
                                                                .range(110, 150)
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