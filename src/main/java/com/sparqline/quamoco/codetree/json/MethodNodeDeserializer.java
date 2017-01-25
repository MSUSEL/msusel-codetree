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
import com.sparqline.quamoco.codetree.MethodNode;

/**
 * @author fate
 *
 */
public class MethodNodeDeserializer implements JsonDeserializer<MethodNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public MethodNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Json data does not describe an object.");

        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("start"))
            throw new JsonParseException("Missing start field from json object.");

        int start = obj.get("start").getAsInt();

        if (!obj.has("end"))
            throw new JsonParseException("Missing end field from json object.");

        int end = obj.get("end").getAsInt();

        if (!obj.has("qIdentifier"))
            throw new JsonParseException("Missing qIdentifier field from json object.");

        String qId = obj.get("qIdentifier").getAsString();

        if (!obj.has("name"))
            throw new JsonParseException("Missing name field from json object.");

        String name = obj.get("name").getAsString();

        boolean constructor = obj.get("constructor").getAsBoolean();

        MethodNode node = new MethodNode(qId, name, constructor, start, end);

        if (obj.has("metrics")) {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);

            for (String m : metrics.keySet()) {
                node.addMetric(m, metrics.get(m));
            }
        }

        return node;
    }

}
