Feature: OAuth2 Authentication and Course Retrieval

# =============================================================================
# Author      : qa-sarthakpurwar
# Created On  : 2026-06-02
# Feature     : O Auth Get Course API Automation
# Description : O Auth Authentication -> AuthorizationCode
# =============================================================================

Background:
Given I have the Authorization Server URL
And I have already obtained the authorization code

@OAuthCourse @GetAccessToken @Regression
Scenario: Generate access token and fetch course details successfully
# Generate Access Token
Given I have a valid request payload for access token generation  
When I send a "POST" request to the "GetAccessToken" endpoint  
Then the authentication "getAccessTokenResponse" response status code should be 200  
And I store the "access_token" from the authentication "getAccessTokenResponse" response  

# Fetch Courses using Access Token
Given I have a valid request to fetch course details  
When I send a "GET" request to the "GetCourse" endpoint  
Then the authentication "getCourseResponse" response status code should be 200  
And the authentication "getCourseResponse" response should match the expected JSON schema  
And I should receive the list of all available courses from "getCourseResponse"