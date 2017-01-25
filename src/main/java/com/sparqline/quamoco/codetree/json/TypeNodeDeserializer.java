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
import com.sparqline.quamoco.codetree.MethodNode;
import com.sparqline.quamoco.codetree.TypeNode;

/**
 * @author fate
 *
 */
public class TypeNodeDeserializer implements JsonDeserializer<TypeNode> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
     * com.google.gson.JsonDeserializationContext)
     */
    @Override
    public TypeNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("Json not defined as an object.");
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("qIdentifier"))
            throw new JsonParseException("Missing qIdentifier field.");
        String key = obj.get("qIdentifier").getAsString();
        if (!obj.has("name"))
            throw new JsonParseException("Missing name field.");
        String shortKey = obj.get("name").getAsString();

        if (!obj.has("start"))
            throw new JsonParseException("Missing start field.");
        int start = obj.get("start").getAsInt();
        if (!obj.has("end"))
            throw new JsonParseException("Missing end field.");
        int end = obj.get("end").getAsInt();

        TypeNode node = new TypeNode(key, shortKey, start, end);

        if (obj.has("metrics")) {
            Type metricsType = new TypeToken<Map<String, Double>>() {
            }.getType();
            Map<String, Double> metrics = context.deserialize(obj.get("metrics"), metricsType);

            for (String m : metrics.keySet()) {
                node.addMetric(m, metrics.get(m));
            }
        }

        if (obj.has("fields")) {
            Type fieldsType = new TypeToken<Map<String, FieldNode>>() {
            }.getType();
            Map<String, FieldNode> fields = context.deserialize(obj.get("fields"), fieldsType);

            for (String f : fields.keySet()) {
                node.addField(fields.get(f));
            }
        }

        if (obj.has("methods")) {
            Type methodsType = new TypeToken<Map<String, MethodNode>>() {
            }.getType();
            Map<String, MethodNode> methods = context.deserialize(obj.get("methods"), methodsType);

            for (String m : methods.keySet()) {
                node.addMethod(methods.get(m));
            }
        }

        return node;
    }

}
