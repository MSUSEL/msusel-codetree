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
package edu.montana.gsoc.msusel.codetree.node;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>FieldNodeTest</code> contains tests for the class
 * <code>{@link FieldNode}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class FieldNodeTest {

    private FieldNode fixture;

    /**
     * Run the FieldNode(CodeNode,String,String,int) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testFieldNode_1() throws Exception
    {
        final String qIdentifier = "path#field";
        final String name = "field";
        final int line = 1;

        try
        {
            final FieldNode result = FieldNode.builder().identifier(qIdentifier).name(name).start(line).end(line).create();

            // add additional test code here
            Assert.assertNotNull(result);
            Assert.assertEquals("FIELD", result.getType());
            Assert.assertEquals("path#field", result.getQIdentifier());
            Assert.assertEquals(1, result.getEnd());
            Assert.assertEquals(1, result.getStart());
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the FieldNode(CodeNode,String,String,int) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFieldNode_2() throws Exception
    {
        final String identifier = "";
        final int line = 1;

        FieldNode.builder().identifier(identifier).name(identifier).start(line).end(line).create();
    }

    /**
     * Run the FieldNode(CodeNode,String,String,int) constructor test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFieldNode_3() throws Exception
    {
        final String identifier = null;
        final int line = 1;

        FieldNode.builder().identifier(identifier).name(identifier).start(line).end(line).create();
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
        Assert.assertEquals("FIELD", result);
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
        fixture = FieldNode.builder().name("field").identifier("path#field").start(1).end(1).create();
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
        new org.junit.runner.JUnitCore().run(FieldNodeTest.class);
    }
}