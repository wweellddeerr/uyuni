---
name: SUSE Multi-Linux Manager 5.1 MU submission
about: Use this template for SUSE Multi-Linux Manager 5.1 MU submissions
title: '5.1.Z Maintenance Update submission'
labels: ["vega-squad", "5.1"]
projects: ["SUSE/35"]
assignees: ''

---

# Important changes, for the release notes

-

# Deadlines

- Check the [Release Calendar](https://confluence.suse.com/display/SUSEMANAGER/Release+calendar)
  - Freeze: DD.MM.YYYY
  - Submission: DD.MM.YYYY
  - Release:  DD.MM.YYYY
- Jira: https://jira.suse.com/browse/MSQA-XXXXX

# Patchinfo

- Add here the link to the patchinfo so that the PO can consume them for the release notes

# Additional notes

- [ ] Does this Maintenance Update include a refresh of the VM and bare metal images? If so, please provide a link to the related issue.
- [ ] Does this Maintenance Update include updates for the monitoring stack? If so, please provide the related info at least in the section `Important changes, for the release notes`.
- [ ] Does this Maintenance Update require changes to the maintenance channels? If so
  - [ ] Mention them in the Jira ticket for Maintenance
  - [ ] Check if the fake channels in [Devel:Galaxy:Manager:Channels](https://build.suse.de/project/show/Devel:Galaxy:Manager:Channels) need changes too
  - [ ] Check if any product definition in [000product_SP7](https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/000product_SP7) or [000productcompose](https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/000productcompose) or [000package-groups](https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/000package-groups) or [000release-packages](https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/000release-packages). If so, don't forget to check and prepare a PR for https://src.suse.de/products/SUSE_Multi-Linux_Manager/src/branch/5.1/

# Procedure

https://confluence.suse.com/display/SUSEMANAGER/Maintenance+Update+procedure

# Needed issues or EPICs to be included in this version

Add any additional bugzilla report, PR, or EPIC that must be included in this release.

You can also create sub-issues if preferred, but the needed information must be mentioned in this section for having an overview of still pending changes that could block the release.

-

# TODO some days before

Add more tasks if needed.

- [ ] Check that there are no pending PRs to be included in the given maintenance update
  - [ ] spacewalk: https://github.com/SUSE/spacewalk/pulls?q=is%3Apr+is%3Aopen+base%3AManager-5.1+-is%3Adraft
  - [ ] uyuni-tools: https://github.com/SUSE/uyuni-tools/pulls?q=is%3Apr+is%3Aopen+base%3AManager-5.1+-is%3Adraft
  - [ ] check with Abid if there are PRs that he wants to include
- [ ] Create the JIRA ticket, with placeholders for the IDs (this is needed by ION squad too)
  - [ ] Don't forget to specify the due date
  - [ ] Add to the ticket all the info that will help the Maintenace team working at the maintenance requests, such as changes to the channels, packages that should not be added because they are only build dependencies...
- [ ] Prepare the version bumps for the new MU
  - [ ] Increase `web.version` in `web/conf/rhn_web.conf` (`x.y.z+1`) in `Manager-5.1` branch using the script [update_rhn_web_conf.py](https://gitlab.suse.de/galaxy/suma-release-tools/-/blob/master/update_rhn_web_conf.py?ref_type=heads)
  - [ ] Check that the sed that replaces the default tag for SUSE Multi-Linux Manager in the [push.sh](https://github.com/SUSE/uyuni-tools/blob/Manager-5.1/push.sh) script in [uyuni-tools](https://github.com/SUSE/uyuni-tools) specifies the same version you added in `web.version`. If the value is outdated, update it with a PR for [uyuni-tools](https://github.com/SUSE/uyuni-tools). For example, see the [PR done](https://github.com/SUSE/uyuni-tools/pull/43) for fixing the default pull tag after the release of SUSE Manager 5.0.2.
    - [ ] Check if the migration paths exist for both the main database and report database, if they are needed (more at https://confluence.suse.com/display/SUSEMANAGER/Maintenance+Update+procedure)
  - [ ] Check if schema migration directories exist between older and newer SUSE Manager version (more at https://confluence.suse.com/display/SUSEMANAGER/Maintenance+Update+procedure)
  - [ ] Push changes to `Manager-5.1` branch
- [ ] Ask the Ion Squad if they need to proote salt. Remember that we have an **[agreement on how and when to submit salt](https://docs.google.com/document/d/140wx3yvFk1klXiHMzBI-cKf4s-Z2nzycGWS2KC5qUvA) and that the client tools are submitted now from 5.2 MUs, not from 5.1 MUs**.
- [ ] Verify if there is any important pending PR/SR on the following:
  - [ ] `git-obs api "repos/Galaxy/MLMTools/pulls?state=open" | jq -r '.[] | select(.base.ref == "devel-stable") | "[#\(.number)] \(.title) - \(.html_url)"'`
  - [ ] https://build.suse.de/project/show/Devel:Galaxy:Manager:5.1 or via `osc -A https://api.suse.de rq list -t submit -s new,review --incoming Devel:Galaxy:Manager:5.1`

# TODO after "Last day for Changes"

Add more tasks if needed.

- [ ] Lock the branches
  - [ ] [Edit the branch protection rule](https://github.com/SUSE/spacewalk/settings/branch_protection_rules/65952452) for the `Manager-5.1` branch  on `SUSE/spacewalk`.
    - [ ] Ensure that the checkbox `Restrict who can push to matching branches` is marked
    - [ ] Rename the `Branch name pattern` from `BRANCH-TO-BE-LOCKED-FOR-MU-51` to `Manager-5.1` and save the rule
  - [ ] [Edit the branch protection rule](https://github.com/SUSE/spacewalk/settings/branch_protection_rules/65952452) for the branch `Manager-5.1` on `SUSE/uyuni-tools`.
    - [ ] Ensure that the checkbox `Restrict who can push to matching branches` is marked
    - [ ] Rename the `Branch name pattern` from `BRANCH-TO-BE-LOCKED-FOR-MU-51` to `Manager-5.1` and save the rule
- [ ] Send an email to galaxy-devel@suse.de informing that the branches `SUSE/spacewalk:Manager-5.1` and `SUSE/uyuni-tools:Manager-5.1` are locked, and adjust the topic on the slack channel [#team-multi-linux-manager](https://suse.slack.com/archives/C02D78LLS04)
- [ ] Ask the Doc Squad to:
  - [ ] Submit an update of the doc package to `Devel:Galaxy:Manager:5.1` project
  - [ ] Warn the translators so they can start their work
  - [ ] Open a Merge Request for the [documentation.suse.com repository](https://gitlab.suse.de/susedoc/docserv-external-tree-suma), this should be ready before the release, and NOT merged
- [ ] After the branch freeze, ask [Orion](https://suse.slack.com/archives/C02DDMY6R0R) to prepare the PR for the code translations. If nobody from Orion is available, ask @parlt91. They will add us as reviewers.
- [ ] Confirm that the SR for the documentation is merged, and translators warned
- [ ] Merge the PR for the translations with the option `Merge pull request`
- [ ] Quick review changelogs with `tito-wrapper`, and request changes if needed
- [ ] Check all the relevant [5.1 CI tests for the 5.1 version](https://ci.suse.de/view/Manager/view/Manager-5.1/): everything should be green, or otherwise submission must be approved by the RRTG

# TODO during the submission window

Add more tasks if needed (for example, asking Maintenace to change the channel definitions).

- [ ] Check all the relevant [5.1 CI tests for the 5.1 version](https://ci.suse.de/view/Manager/view/Manager-5.1/): everything should be green, or otherwise submission must be approved by the RRTG
- [ ] Tag everything with `tito tag`. Do **NOT** tag if the version has not been already bumped in `web/conf/rhn_web.conf` (`web.version.uyuni`), as requested in one of the previous points.
- [ ] Check that the job [manager-5.1-releng-2obs](https://ci.suse.de/view/Manager/view/Manager-5.1/job/manager-5.1-releng-2obs/) and [manager-5.1-uyunitools-2obs](https://ci.suse.de/view/Manager/view/Manager-5.1/job/manager-5.1-uyunitools-2obs/) have packaged and submitted the changes from `Manager-5.1` branch into the relevant https://build.suse.de/project/subprojects/Devel:Galaxy:Manager:5.1 project.
- [ ] Prepare the submissions with `mu-massive-task` or `patch-creator`. For any new packages that will be added to the codestreams, fetch the groups what will maintain them (one per package) and document this at the release card.
- [ ] Add all the IDs (and notes, if any), to the JIRA ticket, and ping the Maintenance Team at [#discuss-susemamanager-maintenance](https://app.slack.com/client/T02863RC2AC/C02DEF2U0E5)
- [ ] Once `autobuild-team` and `legal-auto` approve all MRs, create the `Manager-X.Y-MU.X.Y.Z` branch and push it.
- [ ] Before unlocking the branches `SUSE/spacewalk:Manager-5.1` and `SUSE/uyuni-tools:Manager-5.1`, consider PRs for merge that became ready during the branch freeze looking for the ["merge-candidate" label](https://github.com/SUSE/spacewalk/pulls?q=is%3Apr+is%3Aopen+label%3Amerge-candidate) or ["merge-candidate" label for uyuni-tools](https://github.com/SUSE/uyuni-tools/pulls?q=is%3Apr+is%3Aopen+label%3Amerge-candidate) or ping reviewers to take care of it.
- [ ] Unlock the branches
  - [ ] [Edit the branch protection rule](https://github.com/SUSE/spacewalk/settings/branch_protection_rules/80278870) for the `Manager-5.1` branch  on `SUSE/spacewalk`.
    - [ ] Ensure that the checkbox `Restrict who can push to matching branches` is marked
    - [ ] Rename the `Branch name pattern` from `Manager-5.1` to `BRANCH-TO-BE-LOCKED-FOR-MU-51` and save the rule
  - [ ] [Edit the branch protection rule](https://github.com/SUSE/spacewalk/settings/branch_protection_rules/80278870) for the branch `Manager-5.1` on `SUSE/uyuni-tools`.
    - [ ] Ensure that the checkbox `Restrict who can push to matching branches` is marked
    - [ ] Rename the `Branch name pattern` from `Manager-5.1` to `BRANCH-TO-BE-LOCKED-FOR-MU-51` and save the rule
- [ ] Send an email to galaxy-devel@suse.de informing that the branches `SUSE/spacewalk:Manager-5.1` and `SUSE/uyuni-tools:Manager-5.1` are unlocked, and adjust the topic on the slack channel [#team-multi-linux-manager](https://suse.slack.com/archives/C02D78LLS04)
- [ ] Add links to the patchinfos on top of this issue, and send for the PO with a link to this issue, and the deadline for the SR for the release notes.
- [ ] Submit the release notes
- [ ] Create the GitHub issues for the submission and release of the next MU.
