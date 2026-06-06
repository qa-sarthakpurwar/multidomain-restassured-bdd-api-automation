@Regression @GooglePlace
Feature: Place API - CRUD Operations
  As a QA Engineer
  I want to validate the Place API endpoints
  So that I can ensure the API works correctly for all CRUD operations

  Background:
    Given the Place API is up and running

  # =====================================================================
  # POST - ADD PLACE
  # =====================================================================
  @Regression @AddPlace @TC01
  Scenario: TC01 - Verify user can add a place with valid payload
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    Then the "addPlaceResponse" response status code should be 200
    And the "addPlaceResponse" response body should contain "status" as "add.response.status"
    And the "addPlaceResponse" response body should contain a valid "place_id"
    And the "addPlaceResponse" response body should contain "scope" as "add.response.scope"

  # =====================================================================
  # GET - GET PLACE
  # =====================================================================
  @Regression @GetPlace @TC02
  Scenario: TC02 - Verify place is persisted correctly after adding
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    And I store the "place_id" from the "addPlaceResponse" place response
    Given I have a valid get place request
    When I send a "GET" request to add place endpoint with "GetPlaceAPI"
    Then the "getPlaceResponse" response status code should be 200
    And the "getPlaceResponse" response body should contain "name" as "place.name"
    And the "getPlaceResponse" response body should contain "address" as "place.address"
    And the "getPlaceResponse" response body should contain "phone_number" as "place.phone"
    And the "getPlaceResponse" response body should contain "website" as "place.website"
    And the "getPlaceResponse" response body should contain "language" as "place.language"

  # =====================================================================
  # PUT - UPDATE PLACE
  # =====================================================================
  @Regression @UpdatePlace @TC03
  Scenario: TC03 - Verify user can update the address of an existing place
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    And I store the "place_id" from the "addPlaceResponse" place response
    Given I have a valid update place request with new address
    When I send a "PUT" request to add place endpoint with "UpdatePlaceAPI"
    Then the "updatePlaceResponse" response status code should be 200
    And the "updatePlaceResponse" response body should contain "msg" as "update.response.msg"
    Given I have a valid get place request
    When I send a "GET" request to add place endpoint with "GetPlaceAPI"
    Then the "getPlaceResponse" response status code should be 200
    And the "getPlaceResponse" response body should contain "address" as "place.updated.address"

  # =====================================================================
  # DELETE - DELETE PLACE
  # =====================================================================
  @Regression @DeletePlace @TC04
  Scenario: TC04 - Verify user can delete an existing place
    Given I have a valid add place request payload
    When I send a "POST" request to add place endpoint with "AddPlaceAPI"
    And I store the "place_id" from the "addPlaceResponse" place response
    Given I have a valid delete place request with  valid  stored placeId
    When I send a "DELETE" request to add place endpoint with "DeletePlaceAPI"
    Then the "deletePlaceResponse" response status code should be 200
    And the "deletePlaceResponse" response body should contain "status" as "add.response.status"
    Given I have a valid get place request
    When I send a "GET" request to add place endpoint with "GetPlaceAPI"
    Then the "getPlaceResponse" response status code should be 404
    And the "getPlaceResponse" response body should contain "msg" as "invalid.place.id.get.response"

  # =====================================================================
  # NEGATIVE - UPDATE PLACE
  # =====================================================================
  @Regression @UpdatePlace @TC05
  Scenario: TC05 - Verify API fails when invalid place_id is used in update request
    Given I have an invalid place_id
    Given I have a valid update place request with new address
    When I send a "PUT" request to add place endpoint with "UpdatePlaceAPI"
    Then the "updatePlaceResponse" response status code should be 404
    And the "updatePlaceResponse" response body should contain "msg" as "invalid.place.id.update.response"

  # =====================================================================
  # NEGATIVE - DELETE PLACE
  # =====================================================================
  @Regression @DeletePlace @TC06
  Scenario: TC06 - Verify API fails when non-existing place_id is used in delete request
    Given I have an invalid place_id
    Given I have a valid delete place request with  valid  stored placeId
    When I send a "DELETE" request to add place endpoint with "DeletePlaceAPI"
    Then the "deletePlaceResponse" response status code should be 404
    And the "deletePlaceResponse" response body should contain "msg" as "invalid.place.id.delete.response"

  # =====================================================================
  # NEGATIVE - GET PLACE
  # =====================================================================
  @Regression @GetPlace @TC07
  Scenario: TC07 - Verify API fails with invalid or non-existing place_id in delete request
    Given I have an invalid place_id
    Given I have a valid get place request
    When I send a "GET" request to add place endpoint with "GetPlaceAPI"
    Then the "getPlaceResponse" response status code should be 404
    And the "getPlaceResponse" response body should contain "msg" as "invalid.place.id.get.response"
