/**
 * The MIT License (MIT)
 *
 * SparQLine Code Tree
 * Copyright (c) 2015-2017 Isaac Griffith, SparQLine Analytics, LLC
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
package edu.montana.gsoc.msusel.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * JSON Deserializer for TypeNodes
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class TypeNodeDeserializer implements JsonDeserializer<TypeNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("Json not defined as an object.");
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("qIdentifier"))
            throw new JsonParseException("Missing qIdentifier field.");
        String key = obj.get("qIdentifier").getAsString();
        if (!obj.has("name"))
            throw new JsonParseException("Missing name field.");
        String shortKey = obj.get("name").getAsString();

        if (!obj.has("start"))
            throw new JsonParseException("Missing start field.");
        int start = obj.get("start").getAsInt();
        if (!obj.has("end"))
            throw new JsonParseException("Missing end field.");
        int end = obj.get("end").getAsInt();

        TypeNode.Builder builder = TypeNode.builder(shortKey, key).range(start, end);

        if (obj.has("metrics"))
        {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);
            metrics.forEach((metric, value) -> builder.metric(metric, value));
        }

        if (obj.has("fields"))
        {
            Type fieldsType = new TypeToken<Map<String, FieldNode>>() {
            }.getType();
            Map<String, FieldNode> fields = context.deserialize(obj.get("fields"), fieldsType);
            fields.forEach((id, field) -> builder.field(field));
        }

        if (obj.has("methods"))
        {
            Type methodsType = new TypeToken<Map<String, MethodNode>>() {
            }.getType();
            Map<String, MethodNode> methods = context.deserialize(obj.get("methods"), methodsType);
            methods.forEach((id, method) -> builder.method(method));
        }

        return builder.create();
    }

}
