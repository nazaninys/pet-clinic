Feature: Assign pet to owner
  Owner may want to own a pet

  Scenario: A pet can be assigned to an owner
    Given There exists an owner with id 123
    And There exist a pet with id 321
    When Owner wants to own the pet
    Then The pet is saved to owner's list
    And The pet is saved to repository


