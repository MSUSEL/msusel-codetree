/**
 * 
 */
package com.sparqline.quamoco.codetree.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sparqline.quamoco.codetree.FileNode;
import com.sparqline.quamoco.codetree.ModuleNode;
import com.sparqline.quamoco.codetree.ProjectNode;

/**
 * @author fate
 *
 */
public class ProjectNodeDeserializer implements JsonDeserializer<ProjectNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public ProjectNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Not defined as a json object.");

        JsonObject obj = json.getAsJsonObject();

        if (!obj.has(("qIdentifier")))
            throw new JsonParseException("Missing qIdentifier field.");
        String key = obj.get("qIdentifier").getAsString();

        ProjectNode node = new ProjectNode(key);

        if (obj.has("metrics")) {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);

            for (String m : metrics.keySet()) {
                node.addMetric(m, metrics.get(m));
            }
        }

        if (!obj.has("start"))
            throw new JsonParseException("Missing start field.");

        int start = obj.get("start").getAsInt();
        int end = obj.get("end").getAsInt();
        node.updateLocation(start, end);

        if (obj.has("files")) {
            Type filesType = new TypeToken<Map<String, FileNode>>() {
            }.getType();
            Map<String, FileNode> files = context.deserialize(obj.get("files"), filesType);

            for (String t : files.keySet()) {
                node.addFile(files.get(t));
            }
        }

        if (obj.has("subprojects")) {
            Type subprojectsType = new TypeToken<Map<String, ProjectNode>>() {
            }.getType();
            Map<String, ProjectNode> subprojects = context.deserialize(obj.get("subprojects"), subprojectsType);

            for (String t : subprojects.keySet()) {
                node.addSubProject(subprojects.get(t));
            }
        }

        if (obj.has("modules")) {
            Type modulesType = new TypeToken<Map<String, ModuleNode>>() {
            }.getType();
            Map<String, ModuleNode> modules = context.deserialize(obj.get("modules"), modulesType);

            for (String t : modules.keySet()) {
                node.addModule(modules.get(t));
            }
        }

        return node;
    }
}
