Feature: New pet is added to owner pets
  Owner should know his pets

  Scenario: Owner can have a new pet
    Given When there is an owner called "Melika"
    When She wants to have a new pet
    Then A pet should be added to her pets
