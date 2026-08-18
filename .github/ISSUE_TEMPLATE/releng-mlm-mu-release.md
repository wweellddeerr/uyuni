---
name: SUSE Multi-Linux Manager MU release
about: Use this template for SUSE Multi-Linux Manager MU releases (announcements)
title: 'X.Y.Z Maintenance Update release'
labels: ["vega-squad"]
projects: ["SUSE/35"]
assignees: ''

---

Related: #

A couple of days before the release deadline:
- [ ] Prepare a list of RR IDs to speed up the release process. You can take the list from SMELT or from the assigned Jira ticket.
- [ ] Prepare the release announcement (as example see https://github.com/SUSE/spacewalk/issues/29103)
  - [ ] Prepare the email draft (use the Gemini gem)
  - [ ] Prepare the slack draft (use the Gemini gem)
- [ ] Ensure that the documentation team integrates the most recent updates into the master branch and the appropriate version directory at the [documentation.suse.com repository](https://gitlab.suse.de/susedoc/docserv-external-tree-suma). This process should encompass all translations, except in cases where an exemption has been explicitly granted.

Release deadline:

- [ ] When QE gives the ok and approves all the pending reviews for `qam-manager`, approve all RRs and ping the Maintenance Team at `#discuss-multi-linux-manager-maintenance`, so they can release.  See `List of Incidents and Status` at
  - [ ] https://jira.suse.com/browse/MSQA-XXXX or
  - [ ] https://smelt.suse.de/overview/?7=susemanager-releng&9=msqa-XXXX#testing 
- [ ] Approve all RRs and ping the Maintenance Team, so they can release
  - [ ] `for rr in ADD_YOUR_RR_LIST_HERE´; do isc review accept -m ok -G susemanager-releng $rr; done`
- [ ] Ping doc team at `#team-multi-linux-manager-docs` and ask them to publish the latest documentation build(s) for the given MU at documentation.suse.com
- [ ] Run `osc -A https://api.suse.de pr SUSE:Containers:SUSE-Manager:X.Y` (replace `X.Y` to see if the containers are published). You can also check their status following https://confluence.suse.com/spaces/SUSEMANAGER/pages/1795129626/How+to+check+the+status+of+the+released+containers+in+the+registry
- [ ] Wait a couple of hours and announce to the `multi-linux-manager@suse.de` mailing list. See example email https://mailman.suse.de/mlarch/SuSE/multi-linux-manager/2026/multi-linux-manager.2026.02/msg00004.html . In general, wait for the ACK from maintenance that the release is complete. The release of containers and SL Micro products increments takes usually quite long. It's not a given that waiting 2h will be enough.
- [ ] Create requests for maintainership for the new packages with `osc -A https://api.suse.de bugowner -S group:<GROUP> -m "Add maintainer" <CODESTREAM>/<PACKAGE>` (this must be done **AFTER** the release)
