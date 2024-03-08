package com.redhat.rhn.domain.appstreams;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AppStream {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String stream;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private String arch;

    public AppStream() { }

    /**
     * Constructs an AppStream instance
     * @param nsvca a Map containing NSVCA keys: "name", "stream", "version", "context", "architecture"
     * @return AppStream instance
     */
    public static AppStream fromNVSCAMap(Map<String, String> nsvca) {
        return new AppStream(
            nsvca.get("name"),
            nsvca.get("stream"),
            nsvca.get("version"),
            nsvca.get("context"),
            nsvca.get("architecture")
        );
    }

    /**
     * Constructs an AppStream instance
     * @param name the name
     * @param stream the stream
     * @param version the version
     * @param context the context
     * @param arch the architecture
     */
    public AppStream(String name, String stream, String version, String context, String arch) {
        this.name = name;
        this.stream = stream;
        this.version = version;
        this.context = context;
        this.arch = arch;
    }

    /**
     * Constructs an AppStream instance based only in name stream and arch, ignoring context and version.
     * @param name the name
     * @param stream the stream
     * @param arch the architecture
     */
    public AppStream(String name, String stream, String arch) {
        this.name = name;
        this.stream = stream;
        this.arch = arch;
    }

    public String getName() {
        return name;
    }
    public String getStream() {
        return stream;
    }
    public String getVersion() {
        return version;
    }
    public String getContext() {
        return context;
    }
    public String getArch() {
        return arch;
    }
}

