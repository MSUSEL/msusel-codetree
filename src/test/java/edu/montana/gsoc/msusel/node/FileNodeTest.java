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
package edu.montana.gsoc.msusel.node;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.FileNode;
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * The class <code>FileNodeTest</code> contains tests for the class
 * <code>{@link FileNode}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class FileNodeTest {

    private FileNode fixture;

    /**
     * Run the FileNode(String) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testFileNode_1() throws Exception
    {
        final String fullPath = "path";

        // add additional test code here
        try
        {
            final FileNode result = new FileNode(fullPath);
            Assert.assertNotNull(result);
            Assert.assertEquals("FILE", result.getType());
            Assert.assertEquals("path", result.getQIdentifier());
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the FileNode(String) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testFileNode_2() throws Exception
    {
        final String fullPath = "";

        // add additional test code here
        try
        {
            new FileNode(fullPath);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the FileNode(String) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testFileNode_3() throws Exception
    {
        final String fullPath = null;

        // add additional test code here
        try
        {
            new FileNode(fullPath);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the boolean addType(TypeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddType_1() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();

        final boolean result = fixture.addType(node);

        // add additional test code here
        Assert.assertEquals(true, result);
    }

    /**
     * Run the boolean addType(TypeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddType_2() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();

        Assert.assertTrue(fixture.addType(node));
        final boolean result = fixture.addType(node);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the boolean addType(TypeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddType_3() throws Exception
    {
        final TypeNode node = null;

        final boolean result = fixture.addType(node);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the String getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_1() throws Exception
    {
        MethodNode method;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).method(
                method = MethodNode.builder("method", "namespace.Type#method").range(25, 100).create()).create();
        Assert.assertTrue(fixture.addType(node));
        final int line = 25;

        final String result = fixture.getMethod(line);

        // add additional test code here
        Assert.assertEquals(method.getQIdentifier(), result);
    }

    /**
     * Run the String getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_2() throws Exception
    {
        MethodNode method;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).method(
                method = MethodNode.builder("method", "namespace.Type#method").range(25, 100).create()).create();
        Assert.assertTrue(fixture.addType(node));

        int line = 25;
        String result = fixture.getMethod(line);

        Assert.assertEquals(method.getQIdentifier(), result);

        line = 100;
        result = fixture.getMethod(line);

        Assert.assertEquals(method.getQIdentifier(), result);
    }

    /**
     * Run the String getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_3() throws Exception
    {
        MethodNode method;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(20, 100).method(
                method = MethodNode.builder("method", "namespace.Type#method").range(25, 100).create()).create();
        Assert.assertTrue(fixture.addType(node));

        int line = 25;
        String result = fixture.getMethod(line);

        Assert.assertEquals("namespace.Type#method", result);

        line = 100;
        result = fixture.getMethod(line);

        Assert.assertEquals("namespace.Type#method", result);
    }

    /**
     * Run the String getField(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_1() throws Exception
    {
        FieldNode field;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000)
                .field(field = FieldNode.builder("field", "namespace.Type#field").range(25, 25).create())
                .create();

        Assert.assertTrue(fixture.addType(node));
        final int line = 25;

        final String result = fixture.getField(line);

        // add additional test code here
        Assert.assertEquals(field.getQIdentifier(), result);
    }

    /**
     * Run the String getFIeld(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_2() throws Exception
    {
        FieldNode field;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(27, 99)
                .field(field = FieldNode.builder("field", "namespace.Type#field").range(28, 28).create())
                .create();

        Assert.assertTrue(fixture.addType(node));

        final int line = 56;
        final String result = fixture.getField(line);

        Assert.assertEquals("", result);
    }

    /**
     * Run the String getField(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_3() throws Exception
    {
        FieldNode field;
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(27, 99)
                .field(field = FieldNode.builder("field", "namespace.Type#field").range(28, 28).create())
                .create();
        Assert.assertTrue(fixture.addType(node));

        final int line = 100;
        final String result = fixture.getField(line);

        Assert.assertEquals("", result);
    }

    /**
     * Run the String getType() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetType_1() throws Exception
    {
        final String result = fixture.getType();

        // add additional test code here
        Assert.assertEquals("FILE", result);
    }

    /**
     * Run the String getType(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetType_2() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        fixture.addType(node);
        final int line = 1;

        final String result = fixture.getType(line);

        // add additional test code here
        Assert.assertEquals("namespace.Type", result);
    }

    /**
     * Run the String getType(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetType_3() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        fixture.addType(node);
        final int line = 1000;

        final String result = fixture.getType(line);

        // add additional test code here
        Assert.assertEquals("namespace.Type", result);
    }

    /**
     * Run the String getType(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetType_4() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        fixture.addType(node);
        final int line = -1;

        final String result = fixture.getType(line);

        // add additional test code here
        Assert.assertEquals("", result);
    }

    /**
     * Run the String getType(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetType_5() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        fixture.addType(node);
        final int line = 1001;

        final String result = fixture.getType(line);

        // add additional test code here
        Assert.assertEquals("", result);
    }

    /**
     * Run the Set<CodeNode> getTypes() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetTypes_1() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        Assert.assertTrue(fixture.addType(node));
        final Set<TypeNode> result = fixture.getTypes();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }

    /**
     * Run the boolean removeType(TypeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveType_1() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();
        Assert.assertTrue(fixture.addType(node));
        final boolean result = fixture.removeType(node);

        // add additional test code here
        Assert.assertTrue(result);
    }

    /**
     * Run the boolean removeType(TypeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveType_2() throws Exception
    {
        final TypeNode node = TypeNode.builder("Type", "namespace.Type").range(1, 1000).create();

        final boolean result = fixture.removeType(node);

        // add additional test code here
        Assert.assertFalse(result);
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
        fixture = new FileNode("path");
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
        new org.junit.runner.JUnitCore().run(FileNodeTest.class);
    }
}