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
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * The class <code>TypeNodeTest</code> contains tests for the class
 * <code>{@link TypeNode}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class TypeNodeTest {

    private TypeNode fixture;

    /**
     * Run the TypeNode(CodeNode,String,String,boolean,int,int) constructor
     * test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testTypeNode_1() throws Exception
    {
        final String identifier = "type";
        final String qIdentifier = "namespace.Type";
        final int start = 1;
        final int end = 1000;

        try
        {
            final TypeNode result = TypeNode.builder(identifier, qIdentifier).range(start, end).create();

            // add additional test code here
            Assert.assertNotNull(result);
            Assert.assertEquals("TYPE", result.getType());
            Assert.assertEquals("namespace.Type", result.getQIdentifier());
            Assert.assertEquals(1000, result.getEnd());
            Assert.assertEquals(1, result.getStart());
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the TypeNode(CodeNode,String,String,boolean,int,int) constructor
     * test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testTypeNode_2() throws Exception
    {
        final String identifier = "";
        final String qIdentifier = "";
        final int start = 1;
        final int end = 1000;

        try
        {
            TypeNode.builder(identifier, qIdentifier).range(start, end).create();
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the TypeNode(CodeNode,String,String,boolean,int,int) constructor
     * test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testTypeNode_3() throws Exception
    {
        final String identifier = null;
        final String qIdentifier = "";
        final int start = 1;
        final int end = 1000;

        try
        {
            TypeNode.builder(identifier, qIdentifier).range(start, end).create();
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the void addMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddMethod_1() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();

        fixture.addMethod(method);

        // add additional test code here
        Assert.assertEquals(1, fixture.getMethods().size());
    }

    /**
     * Run the void addMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddMethod_2() throws Exception
    {
        final MethodNode method = null;

        Assert.assertTrue(fixture.getMethods().isEmpty());
        fixture.addMethod(method);

        // add additional test code here
        Assert.assertTrue(fixture.getMethods().isEmpty());
    }

    /**
     * Run the void addMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddMethod_3() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();

        fixture.addMethod(method);

        // add additional test code here
        Assert.assertEquals(1, fixture.getMethods().size());
        fixture.addMethod(method);
        Assert.assertEquals(1, fixture.getMethods().size());
    }

    /**
     * Run the void addMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddMethod_4() throws Exception
    {
        try
        {
            final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 1001)
                    .create();
            fixture.addMethod(method);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the void addMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddMethod_5() throws Exception
    {
        fixture.setStart(100);
        try
        {
            final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 1009)
                    .create();
            fixture.addMethod(method);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the void addField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddField_1() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();

        fixture.addField(field);

        // add additional test code here
        Assert.assertEquals(1, fixture.getFields().size());
    }

    /**
     * Run the void addField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddField_2() throws Exception
    {
        final FieldNode field = null;

        Assert.assertTrue(fixture.getFields().isEmpty());
        fixture.addField(field);

        // add additional test code here
        Assert.assertTrue(fixture.getFields().isEmpty());
    }

    /**
     * Run the void addField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddField_3() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();

        fixture.addField(field);

        // add additional test code here
        Assert.assertEquals(1, fixture.getFields().size());
        fixture.addField(field);
        Assert.assertEquals(1, fixture.getFields().size());
    }

    /**
     * Run the void addField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddField_4() throws Exception
    {
        try
        {
            final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(1001, 1001).create();
            fixture.addField(field);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {
        }
    }

    /**
     * Run the void addField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testAddField_5() throws Exception
    {
        fixture.setStart(25);
        try
        {
            final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(24, 24).create();
            fixture.addField(field);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {
        }
    }

    /**
     * Run the MethodNode getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_1() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        fixture.addMethod(method);

        MethodNode result = fixture.getMethod(30);

        // add additional test code here
        Assert.assertEquals(method, result);

        result = fixture.getMethod(25);
        Assert.assertEquals(method, result);

        result = fixture.getMethod(100);
        Assert.assertEquals(method, result);

        result = fixture.getMethod(26);
        Assert.assertEquals(method, result);

        result = fixture.getMethod(99);
        Assert.assertEquals(method, result);
    }

    /**
     * Run the MethodNode getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_2() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        fixture.addMethod(method);

        MethodNode result = fixture.getMethod(30);

        // add additional test code here
        Assert.assertEquals(method, result);

        result = fixture.getMethod(24);
        Assert.assertNull(result);

        result = fixture.getMethod(101);
        Assert.assertNull(result);

        result = fixture.getMethod(-1);
        Assert.assertNull(result);

        result = fixture.getMethod(20000);
        Assert.assertNull(result);
    }

    /**
     * Run the MethodNode getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_3() throws Exception
    {
        MethodNode result = fixture.getMethod(30);

        // add additional test code here
        Assert.assertNull(result);

        result = fixture.getMethod(24);
        Assert.assertNull(result);

        result = fixture.getMethod(101);
        Assert.assertNull(result);

        result = fixture.getMethod(-1);
        Assert.assertNull(result);

        result = fixture.getMethod(20000);
        Assert.assertNull(result);
    }

    /**
     * Run the MethodNode getMethod(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethod_4() throws Exception
    {
        Assert.assertTrue(fixture.getMethods().isEmpty());

        MethodNode result = fixture.getMethod(30);

        // add additional test code here
        Assert.assertNull(result);

        result = fixture.getMethod(24);
        Assert.assertNull(result);

        result = fixture.getMethod(101);
        Assert.assertNull(result);

        result = fixture.getMethod(-1);
        Assert.assertNull(result);

        result = fixture.getMethod(20000);
        Assert.assertNull(result);
    }

    /**
     * Run the MethodNode getField(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_1() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        fixture.addField(field);

        final FieldNode result = fixture.getField(25);

        // add additional test code here
        Assert.assertEquals(field, result);
    }

    /**
     * Run the MethodNode getField(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_2() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        fixture.addField(field);

        FieldNode result = fixture.getField(26);

        // add additional test code here
        Assert.assertNull(result);

        result = fixture.getField(24);
        Assert.assertNull(result);
    }

    /**
     * Run the MethodNode getField(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetField_3() throws Exception
    {
        final FieldNode result = fixture.getField(25);

        // add additional test code here
        Assert.assertNull(result);
    }

    /**
     * Run the Set<CodeNode> getMethods() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetMethods_1() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        fixture.addMethod(method);
        final Set<MethodNode> result = fixture.getMethods();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }

    /**
     * Run the Set<CodeNode> getFields() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetFields_1() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        fixture.addField(field);
        final Set<FieldNode> result = fixture.getFields();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
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
        Assert.assertEquals("TYPE", result);
    }

    /**
     * Run the void removeMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveMethod_1() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        fixture.addMethod(method);

        Assert.assertEquals(1, fixture.getMethods().size());
        fixture.removeMethod(method);

        // add additional test code here
        Assert.assertTrue(fixture.getMethods().isEmpty());
    }

    /**
     * Run the void removeMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveMethod_2() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        fixture.addMethod(method);

        Assert.assertEquals(1, fixture.getMethods().size());
        fixture.removeMethod(null);

        // add additional test code here
        Assert.assertFalse(fixture.getMethods().isEmpty());
    }

    /**
     * Run the void removeMethod(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveMethod_3() throws Exception
    {
        final MethodNode method = MethodNode.builder("method", "namespace.type#method").range(25, 100).create();
        final MethodNode method2 = MethodNode.builder("method2", "namespace.type#method2").range(100, 120).create();
        fixture.addMethod(method);

        Assert.assertEquals(1, fixture.getMethods().size());
        fixture.removeMethod(method2);

        // add additional test code here
        Assert.assertFalse(fixture.getMethods().isEmpty());
    }

    /**
     * Run the void removeField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveField_1() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        fixture.addField(field);

        Assert.assertEquals(1, fixture.getFields().size());
        fixture.removeField(field);

        // add additional test code here
        Assert.assertTrue(fixture.getFields().isEmpty());
    }

    /**
     * Run the void removeField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveField_2() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        fixture.addField(field);

        Assert.assertEquals(1, fixture.getFields().size());
        fixture.removeField(null);

        // add additional test code here
        Assert.assertFalse(fixture.getFields().isEmpty());
    }

    /**
     * Run the void removeField(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testRemoveField_3() throws Exception
    {
        final FieldNode field = FieldNode.builder("field", "namespace.type#field").range(25, 25).create();
        final FieldNode field2 = FieldNode.builder("field2", "namespace.type#field2").range(24, 24).create();
        fixture.addField(field);

        Assert.assertEquals(1, fixture.getFields().size());
        fixture.removeField(field2);

        // add additional test code here
        Assert.assertFalse(fixture.getFields().isEmpty());
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
        fixture = new TypeNode("namespace.type", "type");
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
        new org.junit.runner.JUnitCore().run(TypeNodeTest.class);
    }
}