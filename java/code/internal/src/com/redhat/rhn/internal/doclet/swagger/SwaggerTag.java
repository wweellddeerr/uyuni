package com.redhat.rhn.internal.doclet.swagger;

public class SwaggerTag {
    String name;
    String description;
    public SwaggerTag(String name, String description) {
        this.name = name;
        this.description = description.replaceAll("\n", "");
    }
}
