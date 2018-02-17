/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.codetree.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import edu.montana.gsoc.msusel.codetree.node.member.DestructorNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.StatementNode

import java.lang.reflect.Type

class DestructorNodeDeserializer implements JsonDeserializer<MethodNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    DestructorNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Json data does not describe an object.")

        JsonObject obj = json.getAsJsonObject()

        if (!obj.has("start"))
            throw new JsonParseException("Missing start field from json object.")

        int start = obj.get("start").getAsInt()

        if (!obj.has("end"))
            throw new JsonParseException("Missing end field from json object.")

        int end = obj.get("end").getAsInt()

        if (!obj.has("key"))
            throw new JsonParseException("Missing qIdentifier field from json object.")

        String key = obj.get("key").getAsString()

        def builder = DestructorNode.builder().key(key).start(start).end(end)

        if (obj.has("metrics")) {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType()
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType)
            builder.metrics(metrics)
        }

        if (obj.has("statements")) {
            Type stmtType = new TypeToken<List<StatementNode>>() {
            }.getType()
            List<StatementNode> stmts = context.deserialize(obj.get("statements"), stmtType)
            builder.statements(stmts)
        }

        return builder.create()
    }

}
