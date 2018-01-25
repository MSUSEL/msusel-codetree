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

import codetree.node.structural.ProjectNode

import java.lang.reflect.Type

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

/**
 * JSON Deserializer for ProjectNodes
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
class ProjectNodeDeserializer implements JsonDeserializer<ProjectNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    ProjectNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("Not defined as a json object.")

        JsonObject obj = json.getAsJsonObject()

        if (!obj.has(("qIdentifier")))
            throw new JsonParseException("Missing qIdentifier field.")
        String key = obj.get("qIdentifier").getAsString()

        def builder = ProjectNode.builder().key(key)

        if (obj.has("metrics"))
        {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType)
            builder.metrics(metrics)
        }

//        if (obj.has("files"))
//        {
//            Type filesType = new TypeToken<Map<String, FileNode>>() {
//            }.getType();
//            Map<String, FileNode> files = context.deserialize(obj.get("files"), filesType)
//            files.forEach((id, file) -> builder.file(file))
//        }
//
//        if (obj.has("subprojects"))
//        {
//            Type subprojectsType = new TypeToken<Map<String, ProjectNode>>() {
//            }.getType()
//            Map<String, ProjectNode> subprojects = context.deserialize(obj.get("subprojects"), subprojectsType)
//            subprojects.forEach((id, project) -> builder.project(project))
//        }
//
//        if (obj.has("modules"))
//        {
//            Type modulesType = new TypeToken<Map<String, ModuleNode>>() {
//            }.getType();
//            Map<String, ModuleNode> modules = context.deserialize(obj.get("modules"), modulesType)
//            modules.forEach((id, module) -> builder.module(module))
//        }
//
//        if (obj.has("namespaces"))
//        {
//            Type namespacesType = new TypeToken<Map<String, NamespaceNode>>() {
//            }.getType()
//            Map<String, NamespaceNode> namespaces = context.deserialize(obj.get("namespaces"), namespacesType)
//            namespaces.forEach((id, namespace) -> builder.namespace(namespace))
//        }

        return builder.create()
    }
}