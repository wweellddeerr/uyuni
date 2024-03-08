CREATE OR REPLACE VIEW suseModulePackage AS
SELECT
    m.name,
    m.stream,
    m.version,
    pn.name as pkg_name,
    pe.evr
FROM
    suseAppstream m
    JOIN suseAppstreamPackage mp ON m.id = mp.module_id
    JOIN rhnPackage p on mp.package_id = p.id
    JOIN rhnPackageName pn ON p.name_id = pn.id
    JOIN rhnPackageEvr pe ON p.evr_id = pe.id
ORDER BY m.name, m.stream, m.version;

CREATE OR REPLACE VIEW suseServerModularPackages AS
SELECT
    c.id AS channel_id,
    c.label AS channel_label,
    m.id AS module_id,
    m.name,
    m.stream,
    m.version,
    m.context,
    m.arch,
    CONCAT(rpn.name, '-', COALESCE(rpe.epoch || ':', rpe.epoch), rpe.version, '-', rpe.release) AS package,
    s.server_id
FROM suseAppstream m
JOIN suseAppstreamPackage p ON p.module_id = m.id
JOIN rhnchannel c ON m.channel_id = c.id
JOIN rhnpackage rp ON rp.id = p.package_id
JOIN rhnpackageevr rpe ON rpe.id = rp.evr_id
JOIN rhnpackagename rpn ON rpn.id = rp.name_id
LEFT JOIN suseServerAppstream s
    ON  m.name = s.name
    AND m.stream = s.stream
    AND m.version = s.version
    AND m.context = s.context
    AND m.arch = s.arch
ORDER BY c.label, m.name, m.stream, m.version, m.context, m.arch, rpn.name;

CREATE OR REPLACE VIEW suseServerModularPackageView AS
SELECT
    p.package_id,
    c.id AS channel_id,
    c.label AS channel_label,
    s.server_id
FROM suseAppstream m
JOIN suseAppstreamPackage p ON p.module_id = m.id
JOIN rhnchannel c ON m.channel_id = c.id
LEFT JOIN suseServerAppstream s
    ON  m.name = s.name
    AND m.stream = s.stream
    AND m.version = s.version
    AND m.context = s.context
    AND m.arch = s.arch;

CREATE OR REPLACE VIEW suseServerPackagesDisabledModulesView AS
select distinct sasp.package_id as pid, sc.server_id as sid
from rhnserverchannel sc
inner join suseappstream sas
on sas.channel_id = sc.channel_id
inner join suseappstreampackage sasp
on sasp.module_id = sas.id
left join suseserverappstream ssa
    on ssa.name = sas.name
    and ssa.stream = sas.stream
    and sas.version = ssa.version
    and sas.context = ssa.context
    and sas.arch = ssa.arch
where ssa.id is null;

create or replace view
suseChannelModuleNewestPackageView
as
SELECT channel_id,
       name_id,
       evr_id,
       package_arch_id,
       package_id,
       module,
       stream
FROM (
      SELECT channel_id,
             name_id,
             evr_id,
             package_arch_id,
             build_time,
             max(package_id) as package_id,
             module,
             stream,
             ROW_NUMBER() OVER(PARTITION BY name_id, channel_id, package_arch_id, module, stream ORDER BY build_time DESC) rn
      FROM (
            SELECT m.channel_id          as channel_id,
                   p.name_id             as name_id,
                   p.evr_id              as evr_id,
                   m.package_arch_id     as package_arch_id,
                   p.id                  as package_id,
                   p.build_time          as build_time,
                   m.module              as module,
                   m.stream              as stream
            FROM (select max(pe.evr) AS max_evr,
			  cp.channel_id,
			  p.name_id,
			  p.package_arch_id,
			  appstream.name AS module,
			  appstream.stream as stream
			from rhnPackageEVR                               pe
			inner join susePackageExcludingPartOfPtf         p
			    on p.evr_id = pe.id
			inner join suseChannelPackageRetractedStatusView cp
			    on cp.package_id = p.id
			left join suseAppstreamPackage                   appstreampkg
			    on appstreampkg.package_id = p.id
			left join suseAppstream                          appstream
			    on appstream.channel_id = cp.channel_id and appstream.id = appstreampkg.module_id
			where NOT cp.is_retracted
			group by cp.channel_id, p.name_id, p.package_arch_id, appstream.name, appstream.stream) m,
                 rhnPackageEVR       pe,
                 rhnPackage          p,
                 rhnChannelPackage   chp
            WHERE m.max_evr = pe.evr
                AND m.name_id = p.name_id
                AND m.package_arch_id = p.package_arch_id
                AND p.evr_id = pe.id
                AND chp.package_id = p.id
                AND chp.channel_id = m.channel_id
      ) latest_packages
      group by channel_id, name_id, evr_id, package_arch_id, build_time, module, stream
) n
WHERE rn = 1;
