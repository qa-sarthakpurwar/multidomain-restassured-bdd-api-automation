@PlaceAPI
Feature: Place API - CRUD Operations
  As a QA Engineer
  I want to validate the Place API endpoints
  So that I can ensure the API works correctly for all CRUD operations

  Background:
    Given the Place API is up and running

  # =====================================================================
  # POST - ADD PLACE
  # =====================================================================

  @PlaceAPI @AddPlace @TC01
  Scenario: TC01 - Verify user can add a place with valid payload
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    Then the response status code should be 200
    And the response body should contain "status" as "add.response.status"
    And the response body should contain a valid "place_id"
    And the response body should contain "scope" as "add.response.scope"
    
     # =====================================================================
  # GET - GET PLACE
  # =====================================================================

  @Positive @GetPlace @TC02
  Scenario: TC02 - Verify place is persisted correctly after adding
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    And I store the "place_id" from the response
    And I send a "GET" request to add place endpoint with "GetPlaceAPI"
    Then the response status code should be 200
    And the response body should contain "name" as "place.name"
    And the response body should contain "address" as "place.address"
    And the response body should contain "phone_number" as "place.phone"
    And the response body should contain "website" as "place.website"
    And the response body should contain "language" as "place.language"



    
    
