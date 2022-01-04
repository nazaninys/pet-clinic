Feature: Find pet

  Scenario: If pet is in pets list, it should be found
    Given There is a pet with id 123
    When Manager wants to find it by id
    Then The pet is found successfully
