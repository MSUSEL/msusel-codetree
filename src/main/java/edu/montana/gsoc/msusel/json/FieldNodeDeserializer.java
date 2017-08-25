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

/**
 * JSON deserializer for FieldNodes
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class FieldNodeDeserializer implements JsonDeserializer<FieldNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("FieldNode not defined as a json object.");
        JsonObject obj = json.getAsJsonObject();

        int start = 0;
        String qId = null;
        String name = null;

        if (obj.has("start"))
            start = obj.get("start").getAsInt();
        else
            throw new JsonParseException("start element missing from FieldNode json definition.");

        if (obj.has("qIdentifier"))
            qId = obj.get("qIdentifier").getAsString();
        else
            throw new JsonParseException("qIdentifier is missing from json definition");

        if (obj.has("name"))
            name = obj.get("name").getAsString();
        else
            throw new JsonParseException("name is missing from json definition");

        FieldNode.Builder builder = FieldNode.builder(name, qId).range(start, start);

        if (obj.has("metrics"))
        {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);
            metrics.forEach((metric, value) -> builder.metric(metric, value));
        }

        return builder.create();
    }

}
