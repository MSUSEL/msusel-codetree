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
import com.sparqline.quamoco.codetree.FieldNode;

/**
 * @author fate
 *
 */
public class FieldNodeDeserializer implements JsonDeserializer<FieldNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public FieldNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
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

        FieldNode node = new FieldNode(qId, name, start);

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
