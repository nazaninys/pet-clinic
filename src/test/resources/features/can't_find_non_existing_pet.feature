Feature: Can't find non-existing pet

  Scenario: Unknown pet should not be found
    Given A pet is introduced with id 123
    And The pet is not in our list
    When We try to find the pet
    Then It cannot be found in the list
