# Copyright (c) 2022 SUSE LLC
# Licensed under the terms of the MIT license.

@sle15sp4_client
Feature: Bootstrap a SLES 15 SP4 traditional client

  Scenario: Clean up sumaform leftovers on a SLES 15 SP4 traditional client
    When I perform a full salt minion cleanup on "sle15sp4_client"

  Scenario: Register a SLES 15 SP4 traditional client
    When I bootstrap traditional client "sle15sp4_client" using bootstrap script with activation key "1-sle15sp4_client_key" from the proxy
    And I install package "spacewalk-client-setup mgr-cfg-actions" on this "sle15sp4_client"
    And I run "mgr-actions-control --enable-all" on "sle15sp4_client"
    Then I should see "sle15sp4_client" via spacecmd

  Scenario: Log in as admin user
    Given I am authorized for the "Admin" section

  Scenario: The onboarding of SLES 15 SP4 traditional client is completed
    When I wait until onboarding is completed for "sle15sp4_client"

  Scenario: Check registration values of SLES 15 SP4 traditional
    Given I update the profile of "sle15sp4_client"
    When I am on the Systems overview page of this "sle15sp4_client"
    And I wait until I see "Software Updates Available" text or "System is up to date" text
    Then I should see a "System Status" text
    And I should see a "Edit These Properties" link
    And I should see a "[Management]" text
    And I should see a "Add to SSM" link
    And I should see a "Delete System" link
    And I should see a "Initial Registration Parameters:" text
    And I should see a "OS: sles-release" text

@proxy
  Scenario: Check connection from SLES 15 SP4 traditional to proxy
    Given I am on the Systems overview page of this "sle15sp4_client"
    When I follow "Details" in the content area
    And I follow "Connection" in the content area
    Then I should see "proxy" short hostname

@proxy
  Scenario: Check registration on proxy of SLES 15 SP4 traditional
    Given I am on the Systems overview page of this "proxy"
    When I follow "Details" in the content area
    And I follow "Proxy" in the content area
    Then I should see "sle15sp4_client" hostname
