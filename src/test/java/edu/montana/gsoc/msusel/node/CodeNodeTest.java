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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.montana.gsoc.msusel.node.CodeNode;
import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.FileNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * The class <code>CodeNodeTest</code> contains tests for the class
 * <code>{@link CodeNode}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class CodeNodeTest {

    private CodeNode fixture;

    /**
     * Run the int compareTo(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testCompareTo_1() throws Exception
    {
        final CodeNode other = TypeNode.builder("Type", "namespace.Type").range(1, 100).create();

        final int result = fixture.compareTo(other);

        // add additional test code here
        Assert.assertEquals(1, result);
    }

    /**
     * Run the int compareTo(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testCompareTo_2() throws Exception
    {
        final CodeNode other = TypeNode.builder("Type", "namespace.Type").range(100, 1000).create();

        final int result = fixture.compareTo(other);

        // add additional test code here
        Assert.assertEquals(0, result);
    }

    /**
     * Run the int compareTo(CodeNode) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testCompareTo_3() throws Exception
    {
        final CodeNode other = TypeNode.builder("Type", "namespace.Type").range(200, 1000).create();

        final int result = fixture.compareTo(other);

        // add additional test code here
        Assert.assertEquals(-1, result);
    }

    /**
     * Run the boolean containsLine(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testContainsLine_1() throws Exception
    {
        final int line = 100;

        final boolean result = fixture.containsLine(line);

        // add additional test code here
        Assert.assertEquals(true, result);
    }

    /**
     * Run the boolean containsLine(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testContainsLine_2() throws Exception
    {
        final int line = 99;

        final boolean result = fixture.containsLine(line);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the boolean containsLine(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testContainsLine_3() throws Exception
    {
        final int line = 1000;

        final boolean result = fixture.containsLine(line);

        // add additional test code here
        Assert.assertEquals(true, result);
    }

    /**
     * Run the boolean containsLine(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testContainsLine_4() throws Exception
    {
        final int line = 1001;

        final boolean result = fixture.containsLine(line);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_1() throws Exception
    {
        final CodeNode obj = TypeNode.builder("Type", "namespace.Type").range(100, 1000).create();

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertEquals(true, result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_2() throws Exception
    {
        final Object obj = null;

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_3() throws Exception
    {
        final Object obj = new Object();

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_4() throws Exception
    {
        final CodeNode obj = TypeNode.builder("Type", "namespace.Type").range(100, 1000).create();

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertTrue(result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_5() throws Exception
    {
        final CodeNode obj = TypeNode.builder("NewType", "namespace.NewType").range(100, 1000).create();

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertFalse(result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_6() throws Exception
    {
        final FileNode obj = new FileNode("path");

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertFalse(result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_7() throws Exception
    {
        final CodeNode obj = TypeNode.builder("Type", "namespace.Type").range(125, 1000).create();
        ;

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertFalse(result);
    }

    /**
     * Run the boolean equals(Object) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testEquals_8() throws Exception
    {
        final CodeNode obj = TypeNode.builder("Type", "namespace.Type").range(100, 900).create();

        final boolean result = fixture.equals(obj);

        // add additional test code here
        Assert.assertFalse(result);
    }

    /**
     * Run the int getEnd() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetEnd_1() throws Exception
    {
        final int result = fixture.getEnd();

        // add additional test code here
        Assert.assertEquals(1000, result);
    }

    /**
     * Run the String getQIdentifier() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetQIdentifier_1() throws Exception
    {
        final String result = fixture.getQIdentifier();

        // add additional test code here
        Assert.assertEquals("namespace.Type", result);
    }

    /**
     * Run the int getStart() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testGetStart_1() throws Exception
    {
        final int result = fixture.getStart();

        // add additional test code here
        Assert.assertEquals(100, result);
    }

    /**
     * Run the void setEnd(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetEnd_1() throws Exception
    {
        final int end = 150;

        try
        {
            fixture.setEnd(end);
            Assert.assertEquals(end, fixture.getEnd());
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the void setEnd(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetEnd_2() throws Exception
    {
        final int end = 1;

        try
        {
            fixture.setEnd(end);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

    /**
     * Run the void setEnd(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetEnd_3() throws Exception
    {
        fixture = FieldNode.builder("field", "field").range(150, 150).create();
        final int end = 1001;

        try
        {
            fixture.setEnd(end);

        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the void setStart(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetStart_1() throws Exception
    {
        final int start = 1001;

        try
        {
            fixture.setStart(start);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {
        }
    }

    /**
     * Run the void setStart(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetStart_2() throws Exception
    {
        final int start = -1;

        try
        {
            fixture.setStart(start);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {
        }
    }

    /**
     * Run the void setStart(int) method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testSetStart_3() throws Exception
    {
        final int start = 250;

        try
        {
            fixture.setStart(start);
            Assert.assertEquals(start, fixture.getStart());
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
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
        fixture = TypeNode.builder("Type", "namespace.Type").range(100, 1000).create();
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
        new org.junit.runner.JUnitCore().run(CodeNodeTest.class);
    }
}