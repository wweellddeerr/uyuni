--
-- Copyright (c) 2024 SUSE LLC
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
--
-- Red Hat trademarks are not licensed under GPLv2. No permission is
-- granted to use or replicate Red Hat trademarks that are incorporated
-- in this software or its documentation.
--
CREATE OR REPLACE VIEW suseServerPackagesDisabledModulesView AS
-- If a package is part of any appstream,
-- and this appstream is not enabled in
-- a server, it should appear here.
select distinct sasp.package_id as pid, sc.server_id as sid
from rhnserverchannel sc
inner join suseappstream sas
on sas.channel_id = sc.channel_id
inner join suseappstreampackage sasp
on sasp.module_id = sas.id
left join suseserverappstream ssa
    on ssa.name = sas.name
    and ssa.stream = sas.stream
    and sas.arch = ssa.arch
where ssa.id is null

union

-- If a package is part of an enabled appstream, all the packages
-- whose name matches with appstream api need to be filtered out
-- except the packages that are part of the enabled appstream.
select     distinct p.id as pid, server_stream.server_id as sid
from       suseServerAppstream server_stream
inner join suseAppstream       appstream 
      on   appstream.name    = server_stream.name
      and  appstream.arch    = server_stream.arch
inner join suseAppstreamApi    api on api.module_id = appstream.id
inner join rhnPackageName      pn  on pn.name = api.rpm
inner join rhnPackage          p   on p.name_id = pn.id
where p.id not in (
    select package_id
    from suseServerModularPackageView
    where server_id = server_stream.server_id
);
