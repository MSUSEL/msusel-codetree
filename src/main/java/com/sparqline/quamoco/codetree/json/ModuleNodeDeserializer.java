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

/**
 * @author fate
 *
 */
public class ModuleNodeDeserializer implements JsonDeserializer<ModuleNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public ModuleNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String key = obj.get("qIdentifier").getAsString();

        ModuleNode node = new ModuleNode(key);

        if (obj.has("metrics")) {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);

            for (String m : metrics.keySet()) {
                node.addMetric(m, metrics.get(m));
            }
        }

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

        return node;
    }

}
