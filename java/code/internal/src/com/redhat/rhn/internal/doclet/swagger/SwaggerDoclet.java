package com.redhat.rhn.internal.doclet.swagger;

import com.redhat.rhn.internal.doclet.ApiDoclet;
import com.redhat.rhn.internal.doclet.DocWriter;

import jdk.javadoc.doclet.DocletEnvironment;

public class SwaggerDoclet extends ApiDoclet {

    @Override
    public DocWriter getWriter(String outputIn, String templateIn, String productNameIn, String apiVersionIn, boolean debugIn) {
        return new SwaggerWriter(outputIn, templateIn, productNameIn, apiVersionIn, debugIn);
    }

    @Override
    public String getName() {
        return "Swagger Doclet";
    }

    @Override
    public boolean run(DocletEnvironment docletEnvironmentIn) {
        return run(docletEnvironmentIn, "json");
    }
}
