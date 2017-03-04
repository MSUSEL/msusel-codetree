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
package com.sparqline.codetree.json;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sparqline.codetree.node.NamespaceNode;
import com.sparqline.codetree.node.TypeNode;

/**
 * @author Isaac Griffith
 * @version
 */
public class NamespaceNodeDeserializer implements JsonDeserializer<NamespaceNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("Not defined as a json object.");

        JsonObject obj = json.getAsJsonObject();

        if (!obj.has(("qIdentifier")))
            throw new JsonParseException("Missing qIdentifier field.");
        String key = obj.get("qIdentifier").getAsString();

        NamespaceNode.Builder builder = NamespaceNode.builder(key);

        if (obj.has("metrics"))
        {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);
            metrics.forEach((metric, value) -> builder.metric(metric, value));
        }

        if (obj.has("types"))
        {
            Type typesType = new TypeToken<Set<TypeNode>>() {
            }.getType();
            Set<TypeNode> types = context.deserialize(obj.get("types"), typesType);
            types.forEach((type) -> builder.type(type));
        }

        if (obj.has("namespaces"))
        {
            Type nsType = new TypeToken<Map<String, NamespaceNode>>() {
            }.getType();
            Map<String, NamespaceNode> namespaces = context.deserialize(obj.get("namespaces"), nsType);
            namespaces.forEach((id, namespace) -> builder.namespace(namespace));
        }

        return builder.create();
    }

}
