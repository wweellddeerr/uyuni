package com.redhat.rhn.domain.action.appstream;

import com.redhat.rhn.domain.action.Action;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AppStreamAction extends Action {

    private Set<AppStreamActionDetails> details = new HashSet<>();

    public Set<AppStreamActionDetails> getDetails() {
        return details;
    }

    public void setDetails(Set<AppStreamActionDetails> detailsIn) {
        if (detailsIn != null) {
            details = detailsIn.stream().peek(d -> d.setParentAction(this)).collect(Collectors.toSet());
        }
    }
}
