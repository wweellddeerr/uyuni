package com.redhat.rhn.internal.doclet.swagger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwaggerOperation {
    String description;
    Map<Integer, OperationDetails> responses = new HashMap<>();

    List<String> tags = new ArrayList<>();
    List<SwaggerParameter> parameters;

    public SwaggerOperation(String description, String handler, List<SwaggerParameter> parameters) {
        this.description = description.replaceAll("\n", "");
        this.parameters = parameters;
        this.responses.put(200, new OperationDetails("successful operation"));
        this.responses.put(400, new OperationDetails("bad request"));
        this.responses.put(401, new OperationDetails("unauthorized"));
        this.responses.put(403, new OperationDetails("forbidden"));
        this.responses.put(404, new OperationDetails("not found"));
        this.responses.put(500, new OperationDetails("internal server error"));
        tags.add(handler);
    }

    static class OperationDetails {
        String description;
        OperationDetails(String description) {
            this.description = description;
        }
    }
}
