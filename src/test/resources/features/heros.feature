Feature: Heros

   Scenario: Post a Hero
    When a hero is posted to the api
    Then the hero is saved in the database
