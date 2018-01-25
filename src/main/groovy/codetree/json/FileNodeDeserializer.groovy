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
package codetree.json

import codetree.node.structural.FileNode
import codetree.node.type.TypeNode
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

/**
 * JSON deserializer for FileNodes
 *
 * @author Isaac Griffith
 * @version 1.2.0-idg
 */
class FileNodeDeserializer implements JsonDeserializer<FileNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    FileNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("Json does not define an object.")
        JsonObject obj = json.getAsJsonObject()

        if (!obj.has("key"))
            throw new JsonParseException("Missing key field.")
        String path = obj.get("key").getAsString()

        def builder = FileNode.builder().key(path)

        if (obj.has("metrics"))
        {
            Type metricsType = new TypeToken<Map<String, Double>>() {}.getType()
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType)
            builder.metrics(metrics)
        }

        if (obj.has("children"))
        {
            Type typesType = new TypeToken<Map<String, TypeNode>>() {
            }.getType()
            Map<String, TypeNode> types = context.deserialize(obj.get("types"), typesType)
            builder.children(types)
        }

        return builder.create()
    }
}
