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
package edu.montana.gsoc.msusel.codetree.json;

import static org.junit.Assert.assertNotNull;

import edu.montana.gsoc.msusel.codetree.node.MethodNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class MethodNodeDeserializerTest {

    private MethodNodeDeserializer fixture;

    /**
     * @throws Exception
     */
    @Test
    public void testDeserialize_1() throws Exception {
        String json = "{\"constructor\": false,\"start\": 50,\"end\": 100,\"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#Method\",\"name\": \"Method\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        MethodNode fn = gson.fromJson(json, MethodNode.class);
        assertNotNull(fn);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_2() throws Exception {
        String json = "{\"constructor\": false,\"end\": 100,\"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#Method\",\"name\": \"Method\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, MethodNode.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_3() throws Exception {

        String json = "{\"constructor\": false,\"start\": 50,\"end\": 100,\"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"name\": \"Method\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, MethodNode.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_4() throws Exception {
        String json = "{\"constructor\": false,\"start\": 50,\"end\": 100,\"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#Method\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, MethodNode.class);
    }
    
    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_5() throws Exception {
        String json = "{\"constructor\": false,\"start\": 50,\"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#Method\",\"name\": \"Method\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, MethodNode.class);
    }
    
    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_6() throws Exception {
        String json = "Test";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MethodNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, MethodNode.class);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @Before
    public void setUp() throws Exception {
        fixture = new MethodNodeDeserializer();
    }

    /**
     * Perform post-test clean-up.
     *
     * @throws Exception
     *             if the clean-up fails for some reason
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    @After
    public void tearDown() throws Exception {
        // Add additional tear down code here
    }

    /**
     * Launch the test.
     *
     * @param args
     *            the command line arguments
     * @generatedBy CodePro at 1/26/16 6:38 PM
     */
    public static void main(final String[] args) {
        new org.junit.runner.JUnitCore().run(MethodNodeDeserializerTest.class);
    }
}
