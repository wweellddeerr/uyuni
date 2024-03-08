-- List of modules in a channel
CREATE TABLE IF NOT EXISTS suseAppstream(
    id NUMERIC NOT NULL
            CONSTRAINT suse_as_module_id_pk PRIMARY KEY,
    channel_id NUMERIC NOT NULL
            REFERENCES rhnChannel(id)
            ON DELETE CASCADE,
    name    VARCHAR(128) NOT NULL,
    stream  VARCHAR(128) NOT NULL,
    version VARCHAR(128) NOT NULL,
    context VARCHAR(16) NOT NULL,
    arch    VARCHAR(16) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_uq_as_module_nsvca
ON suseAppstream(name, stream, version, context, arch);

CREATE SEQUENCE IF NOT EXISTS suse_as_module_seq;

-- Which packages are included in a module
CREATE TABLE IF NOT EXISTS suseAppstreamPackage(
    package_id  NUMERIC NOT NULL
                REFERENCES rhnPackage(id)
                ON DELETE CASCADE,
    module_id   NUMERIC NOT NULL
                REFERENCES suseAppstream(id)
                ON DELETE CASCADE,
        CONSTRAINT uq_as_pkg_module UNIQUE (package_id, module_id)
);

-- Which modules are enabled on a server
CREATE TABLE IF NOT EXISTS suseServerAppstream(
    id  NUMERIC NOT NULL
            CONSTRAINT suse_as_servermodule_id_pk PRIMARY KEY,
    server_id NUMERIC NOT NULL
            REFERENCES rhnServer(id)
            ON DELETE CASCADE,
    name    VARCHAR(128) NOT NULL,
    stream  VARCHAR(128) NOT NULL,
    version VARCHAR(128) NOT NULL,
    context VARCHAR(16) NOT NULL,
    arch    VARCHAR(16) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS suse_as_servermodule_seq;

CREATE TABLE IF NOT EXISTS suseChannelModuleNewestPackage
(
    channel_id      NUMERIC NOT NULL CONSTRAINT suse_cmnp_cid_fk REFERENCES rhnChannel (id) ON DELETE CASCADE,
    name_id         NUMERIC NOT NULL CONSTRAINT suse_cmnp_nid_fk REFERENCES rhnPackageName (id),
    evr_id          NUMERIC NOT NULL CONSTRAINT suse_cmnp_eid_fk REFERENCES rhnPackageEVR (id),
    package_arch_id NUMERIC NOT NULL CONSTRAINT suse_cmnp_paid_fk REFERENCES rhnPackageArch (id),
    package_id      NUMERIC NOT NULL CONSTRAINT suse_cmnp_pid_fk REFERENCES rhnPackage (id) ON DELETE CASCADE,
    appstream_id    NUMERIC CONSTRAINT suse_cmnp_aid_fk REFERENCES suseAppstream (id),
    CONSTRAINT suse_cmnp_cid_nid_aid_uq UNIQUE (channel_id, name_id, package_arch_id, appstream_id)
);
