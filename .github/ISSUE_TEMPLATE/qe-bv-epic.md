---
name: QE - Build Validation
about: QE - Use this template for a new build validation
title: "BV "
labels: ["qe-squad", "build validation", "epics"]
projects: ["SUSE/32"]
assignees: ''

---

## Information

- Planned start date:
- Real start date:
- Planned deadline:
- Real end date:
- Resubmissions:

---

- Submission card:
- Jira card:
- Release notes draft:

---

- Jenkins pipeline:
  - Full:
  - Mini:
- Server URL:
  - Full:
  - Mini:

---

### Links

- [Step-by-step guide](https://confluence.suse.com/display/SUSEMANAGER/QE+Build+Validation)
- [Automated tests](https://confluence.suse.com/display/SUSEMANAGER/Automated+tests)
- [Pipeline parameters](https://confluence.suse.com/display/SUSEMANAGER/The+new+BV+pipeline)

---

## Legend

- Selected checkbox means, we tested it and the testing is completed with no
  pending blockers to be verified as fixed in a resubmission
- :white_check_mark: : Test/verification was successful
- :x: : Test/verification was not successful
- :test_tube: : Test failed due to test suite issue but succeed manually
- If multiple emotes: task was run several times
  - Example: :x: :white_check_mark: = first run failed, second run passed (resubmission)
  - Example: In case of a blocker, we should wait for the fix and retest -> :x:
    and not checked checkbox. In case of non-blocker -> :x: and checked
    checkbox.

---

## Manual tests and tasks

### Release notes and new features

- [ ] Update and verify the release notes for server and proxy
- [ ] Take a look at the (new) documentation

### Fixed bugs to verify

- [ ] ...

---

## Approval

- [ ] Ping our release engineers in Slack
- [ ] Approve all related MUs in [IBS](https://smelt.suse.de/overview/?7=qam-manager#testing) or via the [command line](https://confluence.suse.com/display/SUSEMANAGER/QE+Build+Validation)

---

## Report

- [ ] Prepare the report according to the template in [Confluence](https://confluence.suse.com/x/d4GTdg)
- [ ] Add the report to Confluence
- [ ] Send the report to `galaxy-devel@suse.de`

---

## Test suite issues or fixes

- [ ] ...

---

## Reported Bugs

### Non-blocker

- [ ] ...

### Blocker

- [ ] ...
