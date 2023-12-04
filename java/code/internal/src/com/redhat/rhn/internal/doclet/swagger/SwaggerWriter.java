/*
 * Copyright (c) 2020 SUSE LLC
 * Copyright (c) 2009--2010 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.internal.doclet.swagger;

import com.redhat.rhn.internal.doclet.ApiCall;
import com.redhat.rhn.internal.doclet.DocWriter;
import com.redhat.rhn.internal.doclet.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * Swagger Writer
 */
public class SwaggerWriter extends DocWriter {

    private static final Gson GSON = new GsonBuilder().create();

    public SwaggerWriter(String outputIn, String templatesIn, String productIn, String apiVersionIn, boolean debugIn) {
        super(outputIn, templatesIn, productIn, apiVersionIn, debugIn);
    }

    @Override
    public void write(List<Handler> handlers, Map<String, String> serializers) throws IOException {
        SwaggerSpec swagger = new SwaggerSpec(product);
        for (Handler handler : handlers) {
            swagger.getTags().add(new SwaggerTag(handler.getName(), handler.getDesc()));
            for (var call : handler.getCalls()) {
                String path = "/" + handler.getName().replaceAll("\\.", "/") + "/" + call.getName();
                String method = call.isReadOnly() ? "get" : "post";
                var parameters = call.getParams().stream()
                        .map(SwaggerParameter::parseParam)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                for (var param : parameters.stream()
                        .filter(p -> p.in.equals("body"))
                        .collect(Collectors.toList())) {
                    var body_properties = new HashMap<>();
                    body_properties.put("properties", param.properties);
                    swagger.definitions.put(param.name, body_properties);
                    param.properties = null;
                    param.name = null;
                }               
                var operation = new SwaggerOperation(call.getDoc(), handler.getName(), parameters);
                swagger.paths.put(path, Map.of(method, operation));
            }
        }
        writeFile(output + "swagger.json", GSON.toJson(swagger));
    }
}
