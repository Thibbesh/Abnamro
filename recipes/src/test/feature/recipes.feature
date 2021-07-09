Feature: Testing a recipes service REST API Users should be able to submit GET, PUT, POST and DELETE requests.

  Background:
    Given I Set sample recipes REST API url
    And I signup with api/authentication/signup with username, password and roles assigned
    And I signin with api/authentication/signin with username and password to generate bearer-token.

  Scenario: GET recipes data from recipes service for unauthorized
    Given I Set GET recipes service api endpoint "1"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Send GET HTTP request
    Then I receive valid HTTP response code 401 for "GET."
    And Response BODY "GET" is empty

  Scenario: POST recipes data to a recipes service
    Given I Set POST recipes service api endpoint
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Set request Body as raw from postman
    And Send a POST HTTP request
    Then I receive valid HTTP response code 201
    And Response BODY "POST" is "Recipe saved successfully..".

  Scenario: GET recipes data from recipes service
    Given I Set GET recipes service api endpoint "1"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Send GET HTTP request
    Then I receive valid HTTP response code 200 for "GET."
    And Response BODY "GET" is non-empty

  Scenario: GET recipes data from recipes service which recipe doesnt exist
    Given I Set GET recipes service api endpoint "2"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Send GET HTTP request
    Then I receive valid HTTP response code 404 for "GET."
    And Response BODY "GET" is non-empty
    And Response BODY with "error": "Error_CODE-0001" and "message": "No recipe found with Id 2"

  Scenario: GET ALL recipes data from recipes service
    Given I Set GET recipes service api endpoint without PathVariable
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Send GET ALL HTTP request
    Then I receive valid HTTP response code 200 for "GET ALL."
    And Response BODY "GET" is non-empty and list of recipes

  Scenario: GET ALL recipes data from recipes service which is empty list
    Given I Set GET recipes service api endpoint without PathVariable
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And Send GET HTTP request
    Then I receive valid HTTP response code 204 for "GET "
    And Response BODY "GET" is empty


  Scenario: UPDATE recipes data to a recipes service
    Given I Set PUT recipes service api endpoint "1"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And I Set PUT request Body as raw from postman
    And Send PUT HTTP request
    Then I receive valid HTTP response code 200 for "PUT."
    And Response BODY "PUT" is non-empty and updated recipe object

  Scenario: UPDATE recipes data from recipes service which recipe doesnt exist
    Given I Set UPDATE recipes service api endpoint "2"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And I Set PUT request Body as raw from postman
    And Send PUT HTTP request
    Then I receive valid HTTP response code 404 for "PUT."
    And Response BODY "GET" is non-empty
    And Response BODY with "error": "Error_CODE-0001" and "message": "No recipe found with Id 2"


  Scenario: DELETE recipes data from a recipes service
    Given I Set DELETE recipes service api endpoint for "1"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And I Send DELETE HTTP request
    Then I receive valid HTTP response code 200 for "DELETE."
    And Response BODY "DELETE" is "Recipe deleted successfully..".

  Scenario: DELETE recipes data from recipes service which recipe doesnt exist
    Given I Set DELETE recipes service api endpoint "2"
    When I Set request HEADER param content type as "application/json."
    And I set request HEADER param Authorization "Key as Authorization and Value as Bearer <bearer-token>"
    And I Set DELETE request Body as raw from postman
    And Send DELETE HTTP request
    Then I receive valid HTTP response code 404 for "PUT."
    And Response BODY "GET" is non-empty
    And Response BODY with "error": "Error_CODE-0001" and "message": "No recipe found with Id 2"