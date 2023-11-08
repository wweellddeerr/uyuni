package com.redhat.rhn.internal.doclet.swagger;

import com.redhat.rhn.common.conf.Config;
import com.redhat.rhn.common.conf.ConfigDefaults;
import com.redhat.rhn.frontend.xmlrpc.api.ApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwaggerSpec {

    private String swagger;
    private String productName;
    private String basePath;
    private Map<String, String> info;
    private List<SwaggerTag> tags = new ArrayList<>();
    Map<String, Map<String, SwaggerOperation>> paths = new HashMap<>();

    public SwaggerSpec(String productName) {
        this.productName = productName;
        this.swagger = "2.0";
        this.basePath = "/rhn/manager/api";
        this.info = Map.of(
            "title", productName + " Swagger",
            "description", "This is a Proof of Concept of using Swagger inside " + productName + "'s WebUI."
        );
    }

    public String getSwagger() {
        return swagger;
    }

    public String getBasePath() {
        return basePath;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public List<SwaggerTag> getTags() {
        return tags;
    }

    public Map<String, Map<String, SwaggerOperation>> getPaths() {
        return paths;
    }
}
