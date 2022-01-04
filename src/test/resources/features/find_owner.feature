Feature: Find owner

  Scenario: If owner is in owners list, it should be found
    Given There is an owner with id 123
    When Manager wants to find him by id
    Then The owner is found successfully
