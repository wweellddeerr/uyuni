---
name: SUSE Multi-Linux Manager 5.1 VM and bare metal images refresh
about: Use this template when refreshing the 5.1 VM and bare metal images
title: 'SUSE Multi-Linux Manager 5.1 VM and bare metal images refresh based on MLM 5.1.Z'
labels: ["vega-squad", "5.1"]
projects: ["SUSE/35"]
assignees: ''

---

# Deadlines

Check the [Release Calendar](https://confluence.suse.com/display/SUSEMANAGER/Release+calendar)
- **Freeze:** DD.MM.YYYY
- **Submission:** DD.MM.YYYY
- **Release:** DD.MM.YYYY
- **Jira:** https://jira.suse.com/browse/MSQA-XXXX
- **Based on 5.1 MU:** <!-- Add spacewalk card here -->

# Procedure

[5.1 How to release new VM images for SUSE Multi-Linux Manager](https://confluence.suse.com/spaces/SUSEMANAGER/pages/1998225706/5.1+How+to+release+new+VM+images+for+SUSE+Multi-Linux+Manager) based on [#24689](https://github.com/SUSE/spacewalk/issues/24689) and updated with feedback from [release-support#375](https://gitlab.suse.de/buildops/release-support/-/issues/375)

# Needed issues or EPICs to be included in this version

Add any additional bugzilla report, PR, or EPIC that must be included in this release.

You can also create sub-issues if preferred, but the needed information must be mentioned in this section for having an overview of still pending changes that could block the release.

# TODO a few days before

Add more tasks if needed.

- [ ] Create a new jira ticket to coordinate the work between autobuild, maintenance and QE.
  - [ ] Review the optional section about additional submissions and drop it if this refresh doesn't include any documentation or release notes refresh.
- [ ] Report a new issue for the autobuild team with the details of the needed projects at the [release-support board with this pre-filled template](https://gitlab.suse.de/buildops/release-support/-/issues/new?issue%5Btitle%5D=Refreshed%20VM%20and%20bare%20metal%20images%20for%20MLM%205.1%20%285.1.Z%20-%205.1-YYYY-MM%29&issue%5Bdescription%5D=%3E%20%2A%2A%E2%9A%A0%EF%B8%8F%20MANUAL%20ACTIONS%20FROM%20MLM%20RELENGS%20REQUIRED%3A%2A%2A%0A%3E%201.%20Update%20%2A%2A5.1.Z%2A%2A%20and%20%2A%2A5.1-YYYY-MM%2A%2A%20in%20the%20%2A%2ATitle%2A%2A%20and%20%2A%2Adescription%2A%2A.%0A%3E%202.%20%2A%2ADELETE%20THIS%20INSTRUCTION%20BLOCK%2A%2A%20before%20submitting%20the%20issue.%0A%0AHi%20folks%2C%0A%0ANow%20that%20we%20are%20preparing%20MLM%205.1.Z%20we%20would%20also%20like%20to%20do%20a%20refresh%20of%20the%20VM%20and%20bare%20metal%20images%2C%20that%20will%20need%20to%20be%20based%20on%20the%20content%20of%20the%205.1.Z%20maintenance%20update.%0A%0AThe%20sources%20of%20those%20images%20will%20be%20updated%20with%20a%20PR%20to%20those%202%20sources%20%28releng%20work%20is%20in%20progress%29%3A%0A%2A%20https%3A%2F%2Fsrc.suse.de%2Fproducts%2FSUSE_Multi-Linux_Manager%2Fsrc%2Fbranch%2F5.1%2FMulti-Linux-Manager-Proxy%0A%2A%20https%3A%2F%2Fsrc.suse.de%2Fproducts%2FSUSE_Multi-Linux_Manager%2Fsrc%2Fbranch%2F5.1%2FMulti-Linux-Manager-Server%0A%2A%20with%20the%20%60currentmilestone%60%20macro%20in%20https%3A%2F%2Fsrc.suse.de%2Fproducts%2FSUSE_Multi-Linux_Manager%2Fsrc%2Fbranch%2F5.1%2F_config%23L68-L74%20updated%20to%20%605.1-YYYY-MM%60%0A%0AFor%20what%20concerns%20the%20architectures%3A%0A%2A%20the%20%2A%2AServer%2A%2A%20is%20supported%20on%3A%20aarch64%20ppc64le%20s390x%20x86_64%0A%2A%20the%20%2A%2AProxy%2A%2A%20is%20supported%20on%3A%20aarch64%20x86_64%0A%0A%23%23%23%20Create%20New%20Projects%0A-%20%5B%20%5D%20Create%20%60SUSE%3AProducts%3AMulti-Linux-Manager-Server%3A5.1-YYYY-MM%3A%24arch%60%0A-%20%5B%20%5D%20Create%20%60SUSE%3AProducts%3AMulti-Linux-Manager-Proxy%3A5.1-YYYY-MM%3A%24arch%60%0A%0AThanks%20a%20lot%20for%20your%20support%21%0A%0A%2Flabel%20~High). Follow the instructions at the top of the description of the newly created card and replace the values as suggested. Add more infos if needed.
  - [ ] Check with @admd the list of images and their architectures to be released.
  - [ ] Check with autobuild if the list provided matches with the scripts used by autobuild for the release.
- [ ] Check the devel and the submit projects. If the prjconf has been updated, do NOT forget to retrigger a rebuild of the vm images.
  - [ ] https://build.suse.de/project/show/Devel:Galaxy:Manager:5.1
  - [ ] https://build.suse.de/project/show/Devel:Galaxy:Manager:VMImages:5.1
- [ ] Check that the images are built in the Devel project
  - [ ] https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/Multi-Linux-Manager-Server
  - [ ] https://build.suse.de/package/show/Devel:Galaxy:Manager:5.1/Multi-Linux-Manager-Proxy
- [ ] Check that the images are built in the submission project
  - [ ] https://build.suse.de/package/show/Devel:Galaxy:Manager:VMImages:5.1/Multi-Linux-Manager-Server
  - [ ] https://build.suse.de/package/show/Devel:Galaxy:Manager:VMImages:5.1/Multi-Linux-Manager-Proxy
- [ ] Coordinate with PO if there's the need to update the release notes or the documentation.
- [ ] In case of documentation changes, make sure the documentation team is aware and ready to provide them. Ask the documentation squad to submit an update of the doc package to the `Devel:Galaxy:Manager:5.1` project, and ask them to warn the translators. A Merge Request for the [documentation.suse.com repository](https://gitlab.suse.de/susedoc/docserv-external-tree-suma) should be created.
- [ ] If needed (see step before), create the JIRA ticket with placeholders for the release notes and/or documentation IDs.
- [ ] Prepare the draft of the release announcement for the `multi-linux-manager@suse.de` mailing list.

# TODO during the submission window

Add more tasks if needed.

- [ ] Run the CI job [manager-5.1-releng-2obs](https://ci.suse.de/view/Manager/view/Manager-5.1/job/manager-5.1-releng-2obs/) using as `spacewalk_branch` the last released MU branch (e.g., `Manager-5.1-MU-5.1.1`) and verify the changes are submitted to the [Devel:Galaxy:Manager:5.1](https://build.suse.de/project/show/Devel:Galaxy:Manager:5.1) project.
- [ ] Verify that images are built correctly in the Devel project.
- [ ] Verify that images are built correctly in the submission project (which builds with released content).
- [ ] **Prepare the Maintenance Submission via Gitea Pull Request:** Ensure you have a properly configured local fork of `gitea@src.suse.de:products/SUSE_Multi-Linux_Manager.git`. Run the `submit-mlm-product.sh` script from within your local clone to automatically bump versions, update the config milestone, create changelogs, and submit the PR.
  - [ ] Execute the script (replace with correct versions and ID). for example
    ```bash
    ./submit-mlm-product.sh -u YOUR_USERNAME -t 5.1 -j MSQA-1059 -m 5.1.5 --vm-only -v 5.1.5 -D 2026-08 -p Multi-Linux-Manager-Server,Multi-Linux-Manager-Proxy
    ```
- [ ] Ping maintenance in [#discuss-multi-linux-manager-maintenance](https://suse.slack.com/archives/C02DEF2U0E5) and provide the links to the generated Gitea Pull Requests. They will review and merge them.
- [ ] After autobuild has approved the PRs, run the CI job [manager-5.1-releng-2obs](https://ci.suse.de/view/Manager/view/Manager-5.1/job/manager-5.1-releng-2obs/) with parameters, using as `spacewalk_branch` the branch `Manager-5.1`.
- [ ] For any **new image** added to the codestreams, fetch the group that will maintain them and document this in the release card.
- [ ] **Resubmission of release notes and documentation (if needed):** *Keep in mind that these changes will only affect the online version. The bundled version updates only via standard MUs.*
  - The packages affected are/could be (check what's really needed):
    - [ ] `release-notes-multi-linux-manager`
    - [ ] `release-notes-multi-linux-manager-proxy`
    - [ ] `susemanager-docs_en`
  - [ ] Create a new MR for these packages using patch-creator
    - [ ] Do not forget to review the patchinfos and strip out what's unneeded.
    - [ ] Check also that the submit script `Maintenancerequest.sh` submits only the right content.
  - [ ] Add the link of the maintenance PRs for release notes/documentation to the Jira card. Ping the Maintenance Team at [#discuss-susemanager-maintenance](https://app.slack.com/client/T02863RC2AC/C02DEF2U0E5).
- [ ] When the sources finish building and everything is published in [SUSE:SLFO:Products:Multi-Linux-Manager:5.1](https://build.suse.de/project/show/SUSE:SLFO:Products:Multi-Linux-Manager:5.1), ask maintenance to release the images to [SUSE:SLFO:Products:Multi-Linux-Manager:5.1:ToTest](https://build.suse.de/project/show/SUSE:SLFO:Products:Multi-Linux-Manager:5.1:ToTest).
- [ ] Ping the QA team and that the images are ready to be tested from the ToTest project.

# Release deadline:

- [ ] After QE is done with testing, in case of changes for the release notes and documentation, ask them to approve all the `qam-manager` reviews.
- [ ] Approve all RRs for `susemanager-releng` and ping the Maintenance Team, so they can prepare to release coordinating the task with autobuild.
- [ ] In case of changes to the documentation, create a new request (`/request` command) at [#proj-doc-suse-com-request](https://suse.slack.com/archives/C02CUMY276J), so they can accept the Merge request and sync the repository to the staging server. Once the `/request` command is run, in the menu, remember to fill in the "comment" field with `Please publish to the staging server then ping @multi-linux-manager-docs so that they can review the content.`
- [ ] **Release to PUBLISH:** When testing is done, ask maintenance to release the images from `ToTest` to [SUSE:SLFO:Products:Multi-Linux-Manager:5.1:PUBLISH](https://build.suse.de/project/show/SUSE:SLFO:Products:Multi-Linux-Manager:5.1:PUBLISH).
- [ ] **Final Autobuild Release:** Once the images are in the `PUBLISH` project, ask autobuild to release the images to the final projects they created via the GitLab ticket (e.g. `SUSE:Products:Multi-Linux-Manager-Server:5.1-YYYY-MM:$arch`).
- [ ] Check with autobuild (and eventually maintenance) and ask for confirmation that the release is done
- [ ] Ping the PO and inform that the images are ready for release, he will need this info for the [MR for Schotty](https://gitlab.suse.de/scc/schotty) to be generated with the script [schotty_MLM_updater.py](https://gitlab.suse.de/galaxy/suma-release-tools/-/blob/master/schotty_MLM_updater.py)
- [ ] Create the GitHub issues for the submission and release of the next VM refresh.
