/*
 * Copyright (c) 2009--2015 Red Hat, Inc.
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
package com.redhat.rhn.domain.server;

import com.redhat.rhn.domain.appstreams.AppStream;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "suseServerAppstream")
public class ServerModule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appstreams_servermodule_seq")
    @SequenceGenerator(name = "appstreams_servermodule_seq", sequenceName = "suse_as_servermodule_seq",
            allocationSize = 1)
    private Long id;

    @Embedded
    private AppStream appStream;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    /**
     * Constructs a ServerModule instance.
     */
    public ServerModule() { }

    /**
     * Constructs a ServerModule based on a provided Map containing NSVCA keys.
     * @param server the server
     * @param appStream .
     */
    public ServerModule(Server server, AppStream appStream) {
        this.server = server;
        this.appStream = appStream;
    }

    public AppStream getAppStream() {
        return appStream;
    }

    public Long getId() {
        return id;
    }

    public Server getServer() {
        return server;
    }
}
