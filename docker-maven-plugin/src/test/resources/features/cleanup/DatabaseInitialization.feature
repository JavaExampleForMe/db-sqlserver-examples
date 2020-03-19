Feature: cleanup the database

  Scenario Outline: initialize database
    When The following scripts will be executed <ScriptName>
    Then Script will finish successfully

    Examples:
      | ScriptName      |
      | createDatabase  |
      | createTables    |
