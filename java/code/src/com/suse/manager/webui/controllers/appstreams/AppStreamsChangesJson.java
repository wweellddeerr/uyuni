package com.suse.manager.webui.controllers.appstreams;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class AppStreamsChangesJson {
    private Long sid;
    private Set<String> toEnable;
    private Set<String> toDisable;
    private Optional<LocalDateTime> earliest = Optional.empty();
    private Optional<String> actionChainLabel = Optional.empty();

    public Long getSid() {
        return sid;
    }

    public Set<String> getToEnable() {
        return toEnable;
    }

    public Set<String> getToDisable() {
        return toDisable;
    }

    public Optional<LocalDateTime> getEarliest() {
        return earliest;
    }

    public Optional<String> getActionChainLabel() {
        return actionChainLabel;
    }
}
