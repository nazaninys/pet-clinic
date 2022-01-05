Feature: Find pet

  Scenario: If pet is in pets list, it should be found
    Given There is a pet with id 123
    When Manager wants to find it by id
    Then The pet is found successfully

  Scenario: Unknown pet should not be found
    Given A pet is introduced with id 123
    And The pet is not in our list
    When We try to find the pet
    Then It cannot be found in the list
