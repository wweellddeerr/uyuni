---
name: QE - Testing new client OS migration
about: Use this template for manually testing a new OS migration
title: "Test the client migration from X to Y"
labels: ["qe-squad","manual tests", "client-migration"]
projects: ["SUSE/32"]
assignees: ''

---

# Description

This manually tests the client migration from X to Y on MLM .

## Information

- Start date:
- End date:
- Environment:
- MLM version:
- Used environment:

---

## Tasks

### Prerequisites

- [ ] Wait until the submissions are ready
- [ ] Install and setup a testing environment with the correct version
- [ ] Verify if the new OS appear in the webUI under Admin -> Product Wizard
  - if not, get in touch with Orion so they reach out to SCC and make them available there, or we get a special
    `product-tree.json` file from Orion with the updated content that we can then use on our mirror. See [here](https://confluence.suse.com/x/t4C9dQ)
- [ ] Open the MLM documentation

### Tests

#### Prerequisites

- [ ] sync the old and the to be upgraded product in the Product Wizard on your server
- [ ] created a custom channel with the MU repos (if needed)
- [ ] create activation keys for Minion and SSH Minion
- [ ] set up a VM from the official ISO for the Minion
- [ ] set up a VM from the official ISO for the SSH Minion

#### Bootstrap the Minion
- [ ] bootstrap the Minion and verify it works
- [ ] follow the documentation on how to do the OS migration
- [ ] do a dry-run of the migration (Minion -> Software -> Product Migration)
- [ ] do the real migration
- [ ] verify that the migration was successful
- [ ] do some minimal smoke tests so assure the Minion still works as expected

#### Bootstrap the SSH Minion
- [ ] bootstrap the SSH Minion and verify it works
- [ ] follow the documentation on how to do the OS migration
- [ ] do a dry-run of the migration (Minion -> Software -> Product Migration)
- [ ] do the real migration
- [ ] verify that the migration was successful
- [ ] do some minimal smoke tests so assure the SSH Minion still works as expected


### Special tasks

- [ ] ...

## Found issues/bugs

- ...

## Links

- [Client version upgrade documentation](https://documentation.suse.com/multi-linux-manager/5.1/en/docs/client-configuration/client-upgrades.html)
- ...

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
