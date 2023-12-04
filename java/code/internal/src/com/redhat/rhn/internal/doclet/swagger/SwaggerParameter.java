package com.redhat.rhn.internal.doclet.swagger;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwaggerParameter {
    String type;
    String name;
    String in;
    String description;
    boolean required;
    boolean explode;
    String format;
    Map<String, Object> schema;
    @SerializedName("default")
    String defaultValue;

    SwaggerParamSchema param_schema;

    Map<String, Object> items;

    Map<String, Map<String, String>> properties;

    public static void main(String[] args) {
        String desc = "#struct_begin(\"errataInfo\")\n" +
                "     *          #prop(\"string\", \"synopsis\")\n" +
                "     *          #prop(\"string\", \"advisory_name\")\n" +
                "     *          #prop(\"int\", \"advisory_release\")\n" +
                "     *          #prop_desc(\"string\", \"advisory_type\", \"Type of advisory (one of the\n" +
                "     *                  following: 'Security Advisory', 'Product Enhancement Advisory',\n" +
                "     *                  or 'Bug Fix Advisory'\")\n" +
                "     *          #prop_desc(\"string\", \"advisory_status\", \"Status of advisory (one of the\n" +
                "     *                  following: 'final', 'testing', 'stable' or 'retracted'\")\n" +
                "     *          #prop(\"string\", \"product\")\n" +
                "     *          #prop(\"string\", \"errataFrom\")\n" +
                "     *          #prop(\"string\", \"topic\")\n" +
                "     *          #prop(\"string\", \"description\")\n" +
                "     *          #prop(\"string\", \"references\")\n" +
                "     *          #prop(\"string\", \"notes\")\n" +
                "     *          #prop(\"string\", \"solution\")\n" +
                "     *          #prop_desc(\"string\", \"severity\", \"Severity of advisory (one of the\n" +
                "     *                  following: 'Low', 'Moderate', 'Important', 'Critical'\n" +
                "     *                  or 'Unspecified'\")\n" +
                "     *       #struct_end()";

        parseStruct(desc);
    }

    public SwaggerParameter(
            String name,
            String description,
            String in,
            String typeIn,
            Map<String, Object> items,
            Map<String, Map<String, String>> properties
    ) {
        this.name = name;
        this.description = description;
        this.type = typeIn;
        this.in = in;
        this.required = true;
        this.explode = false;
        this.items = items;
        this.properties = properties;

        if (this.description.endsWith(")")) {
            this.description = this.description.substring(0, this.description.length() - 1);
        }

        switch (typeIn) {
            case "$date":
                this.type = "string";
                this.format = "date-time";
                this.defaultValue = "2023-01-01T00:00:00Z";
                break;
            case "int":
                this.type = "integer";
                this.format = "int32";
                break;
            case "long":
                this.type = "integer";
                this.format = "int64";
                break;
        }

        if (this.in.equals("body")) {
            Map<String, Object> body_schema = new HashMap<>();
            body_schema.put("$ref", "#/definitions/" + this.name);
            this.schema = body_schema;
        }

    }

    public static Optional<SwaggerParameter> parseParam(String param) {
        String paramValue = param.replaceAll("\n", "");
        if (paramValue.equals("#session_key()")) {
            return Optional.empty();
        }

        Optional<SwaggerParameter> result = parsePrimitiveParam(paramValue);

        if (result.isEmpty()) {
            result = parseArrayStruct(paramValue);
        }
        if (result.isEmpty()) {
            result = parseStructParam(paramValue);
        }

        if (result.isEmpty()) {
            result = parseStruct(paramValue);
        }

        return result;
    }

    private static Optional<SwaggerParameter> parseStruct(String paramValue) {
        if (!paramValue.startsWith("#struct_begin")) {
            return Optional.empty();
        }

        String structName = paramValue.split("#struct_begin\\(\"")[1].split("\"\\)")[0];

        String propsStr = paramValue.split("#struct_begin")[1].split("#struct_end")[0].trim();
        String[] propsRaw = propsStr.split("(#prop_desc|#prop)");

        Map<String, Map<String, String>> properties = new HashMap<>();
        for (String propRaw : propsRaw) {
            if (!propRaw.contains(",")) {
                continue;
            }

            String[] prop = propRaw.split(",");
            String type = prop[0].trim().replaceAll("\"", "").replaceAll("\\(", "");
            type = type.equals("int") || type.equals("long") ? "integer" : type;
            String name = prop[1].trim().replaceAll("\"", "").replaceAll("\\)", "");
            properties.put(name, Map.of("type", type));
        }

        SwaggerParameter result = new SwaggerParameter(structName, "", "body", "object", null, properties);
        return Optional.of(result);
    }

    private static boolean isStructParam(String paramValue) {
        return paramValue.startsWith("#struct_desc")
                || paramValue.startsWith("#param_desc(\"struct\",")
                || paramValue.startsWith("#param(\"struct\",");
    }

    // TODO
    private static Optional<SwaggerParameter> parseArrayStruct(String paramValue) {
        if (!paramValue.startsWith("#array_begin")) {
            return Optional.empty();
        }
        String arrayName = paramValue.split("#array_begin\\(\"")[1].split("\"\\)")[0];
        SwaggerParameter result = new SwaggerParameter(
            arrayName, "", "query", "array", null, null
        );
        return Optional.of(result);
    }

    private static Optional<SwaggerParameter> parseStructParam(String paramValue) {
        if (!isStructParam(paramValue)) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private static Optional<SwaggerParameter> parsePrimitiveParam(String paramValue) {
        String regex = "#(param_desc|param|array_single_desc|array_single)\\((\"int\"|\"long\"|\"boolean\"|\"string\"|\\$date|\"\\$date\"),.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(paramValue);
        if (!matcher.find()) {
            return Optional.empty();
        }
        String structureType = matcher.group(1);
        String type = matcher.group(2).replaceAll("\"", "");
        int idx = matcher.end(2);
        String desc = "";
        String options = "";
        String nameDesc = paramValue.substring(idx + 2).trim();
        if (nameDesc.contains("#options")) {
            String[] nameDescOptions = nameDesc.split("#options");
            nameDesc = nameDescOptions[0].trim();
            options = nameDescOptions[1].trim();
        }
        String[] nameDescArr = nameDesc.split(",");
        String name = nameDescArr[0].replaceAll("\"", "").trim();

        if (nameDescArr.length > 1) {
            desc = nameDescArr[1].replaceAll("\"", "").trim();
            // remove trailing parenthesis
            desc = desc.substring(0, desc.length() - 1);
        }
        else {
            // remove trailing parenthesis
            name = name.substring(0, name.length() - 1);
        }

        Map<String, Object> items = buildItems(structureType, type, options);
        type = processArrayType(structureType, type);
        desc = processArrayDesc(desc);

        SwaggerParameter result = new SwaggerParameter(name, desc, "query", type, items, null);
        return Optional.of(result);
    }

    private static String processArrayType(String structureType, String type) {
        return isArray(structureType) ? "array" : type;
    }

    private static String processArrayDesc(String description) {
        if (description.contains("#options")) {
            return description.split("#options")[0].trim();
        }
        return description;
    }

    private static Map<String, Object> buildItems(String structureType, String type, String options) {
        if (!isArray(structureType)) {
            return null;
        }

        Map<String, Object> items = new HashMap<>();
        items.put("type", type.equals("int") || type.equals("long") ? "integer" : type);
        if (!options.isBlank()) {
            List<String> enumeration = new ArrayList<>();
            String itemsStr = options.split("#options_end")[0].trim();
            String itemKey = itemsStr.contains("#item_desc") ? "#item_desc" : "#item";

            String[] itemsRaw = itemsStr.split(itemKey);

            for (String itemRaw : itemsRaw) {
                String item = itemRaw.split("\\(")[1].trim();
                if (item.contains(",")) {
                    item = item.split(",")[0].trim();
                }
                item = item.endsWith(")") ? item.substring(0, item.length() - 1) : item;
                if (!item.equals("(") && !item.isBlank()) {
                    enumeration.add(item.replaceAll("\"", ""));
                }
            }
            items.put("enum", enumeration);
        }
        return items;
    }

    private static boolean isArray(String structureType) {
        return List.of("array_single", "array_single_desc").contains(structureType);
    }

    static class SwaggerParamSchema {
        String type;
        Map<String, Object> items;

        SwaggerParamSchema(String type, Map<String, Object> items) {
            this.type = type;
            this.items = items;
        }
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + param_schema.type +
                ", format='" + format + '\'';
    }
}
