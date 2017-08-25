/**
 * 
 */
package edu.montana.gsoc.msusel.json;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import edu.montana.gsoc.msusel.json.FieldNodeDeserializer;
import edu.montana.gsoc.msusel.node.CodeNode;
import edu.montana.gsoc.msusel.node.FieldNode;

/**
 * The class <code>FieldNodeDeserializer</code> contains tests for the class
 * <code>{@link CodeNode}</code>.
 *
 * @author fate
 * @version $Revision: 1.0 $
 */
public class FieldNodeDeserializerTest {

    private FieldNodeDeserializer fixture;

    /**
     * @throws Exception
     */
    @Test
    public void testDeserialize_1() throws Exception {
        String json = "{\"start\": 25, \"end\": 25, \"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#TestField\",\"name\": \"TestField\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldNode.class, fixture);
        Gson gson = builder.create();

        FieldNode fn = gson.fromJson(json, FieldNode.class);
        assertNotNull(fn);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_2() throws Exception {
        String json = "{\"end\": 25, \"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#TestField\",\"name\": \"TestField\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, FieldNode.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_3() throws Exception {
        String json = "{\"start\": 25, \"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"name\": \"TestField\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, FieldNode.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_4() throws Exception {
        String json = "{\"start\": 25, \"end\": 25, \"range\": {\"lowerBound\": {\"endpoint\": 1},\"upperBound\": {\"endpoint\": 1}},\"metrics\": {\"Test1\": 1.0},\"qIdentifier\": \"Class#TestField\"}";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, FieldNode.class);
    }

    /**
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testDeserialize_5() throws Exception {
        String json = "Test";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldNode.class, fixture);
        Gson gson = builder.create();

        gson.fromJson(json, FieldNode.class);
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
        fixture = new FieldNodeDeserializer();
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
        new org.junit.runner.JUnitCore().run(FieldNodeDeserializerTest.class);
    }
}
