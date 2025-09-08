# Copyright (c) 2025 SUSE LLC
# Licensed under the terms of the MIT license.

@containerized_server
Feature: Check client tools commands availability
  From the server host
  All client tool commands must be accessible
  So they can be used reliably with valid parameters

  Scenario: Check mgradm support ptf podman
    When I validate the "mgradm support ptf podman" command
