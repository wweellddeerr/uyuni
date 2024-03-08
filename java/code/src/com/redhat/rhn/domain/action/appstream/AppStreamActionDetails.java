package com.redhat.rhn.domain.action.appstream;

import com.redhat.rhn.domain.action.ActionChild;

public class AppStreamActionDetails extends ActionChild {

    public static final String DISABLE_TYPE = "DISABLE";
    public static final String ENABLE_TYPE = "ENABLE";
    private Long id;
    private String moduleName;
    private String stream;
    private String type;

    public AppStreamActionDetails() {
        // Default constructor
    }

    public AppStreamActionDetails(String moduleNameIn, String streamIn, String typeIn) {
        moduleName = moduleNameIn;
        stream = streamIn;
        type = typeIn;
    }

    public static AppStreamActionDetails disableAction(String moduleName) {
        return new AppStreamActionDetails(moduleName.split(":")[0], null, DISABLE_TYPE);
    }

    public static AppStreamActionDetails enableAction(String moduleName, String stream) {
        return new AppStreamActionDetails(moduleName, stream, ENABLE_TYPE);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idIn) {
        id = idIn;
    }

    public void setModuleName(String moduleNameIn) {
        moduleName = moduleNameIn;
    }

    public void setStream(String streamIn) {
        stream = streamIn;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getStream() {
        return stream;
    }

    public String getType() {
        return type;
    }

    public void setType(String typeIn) {
        type = typeIn;
    }

    public boolean isEnable() {
        return ENABLE_TYPE.equals(type);
    }
}
