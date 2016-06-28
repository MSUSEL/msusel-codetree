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
import com.sparqline.quamoco.codetree.TypeNode;

/**
 * @author fate
 *
 */
public class FileNodeDeserializer implements JsonDeserializer<FileNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public FileNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Json does not define an object.");
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("qIdentifier"))
            throw new JsonParseException("Missing qIdentifier field.");
        String path = obj.get("qIdentifier").getAsString();

        FileNode node = new FileNode(path);

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
        if (!obj.has("end"))
            throw new JsonParseException("Missing end field.");
        int end = obj.get("end").getAsInt();
        node.updateLocation(start, end);

        if (obj.has("types")) {
            Type typesType = new TypeToken<Map<String, TypeNode>>() {
            }.getType();
            Map<String, TypeNode> types = context.deserialize(obj.get("types"), typesType);

            for (String t : types.keySet()) {
                node.addType(types.get(t));
            }
        }

        return node;
    }
}
