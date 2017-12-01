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
 * The class <code>MethodNodeTest</code> contains tests for the class
 * <code>{@link MethodNode}</code>.
 *
 * @generatedBy CodePro at 1/26/16 6:38 PM
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class MethodNodeTest {

    private MethodNode fixture;

    /**
     * Run the MethodNode(CodeNode,String,String,boolean,int,int) constructor
     * test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testMethodNode_1() throws Exception
    {
        final String name = "method";
        final String qIdentifier = "path#" + name;
        final boolean constructor = true;
        final int start = 1;
        final int end = 1;

        final MethodNode result = MethodNode.builder()
                .name(name)
                .identifier(qIdentifier)
                .constructor(constructor)
                .start(start)
                .end(end)
                .create();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result.isConstructor());
        Assert.assertEquals("METHOD", result.getType());
        Assert.assertEquals("path#method", result.getQIdentifier());
        Assert.assertEquals(1, result.getEnd());
        Assert.assertEquals(1, result.getStart());
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
        Assert.assertEquals("METHOD", result);
    }

    /**
     * Run the boolean isConstructor() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testIsConstructor_1() throws Exception
    {
        final boolean result = fixture.isConstructor();

        // add additional test code here
        Assert.assertEquals(true, result);
    }

    /**
     * Run the boolean isConstructor() method test.
     *
     * @throws Exception
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Test
    public void testIsConstructor_2() throws Exception
    {
        fixture.setConstructor(false);
        final boolean result = fixture.isConstructor();

        // add additional test code here
        Assert.assertEquals(false, result);
    }

    @Test
    public void testSetConstructor_1() throws Exception
    {
        Assert.assertTrue(fixture.isConstructor());

        fixture.setConstructor(false);

        Assert.assertFalse(fixture.isConstructor());
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
        fixture = MethodNode.builder()
            .name("method")
            .identifier("path#method")
            .constructor(true)
            .start(1)
            .end(1)
            .create();
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
        new org.junit.runner.JUnitCore().run(MethodNodeTest.class);
    }
}