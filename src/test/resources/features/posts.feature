@posts
Feature: JSONPlaceholder Posts API

  As an API consumer
  I want to interact with the /posts endpoint
  So that I can perform complete CRUD operations on post resources

  # NOTE: jsonplaceholder.org is a read-only fake API. Write operations (POST,
  # PUT, DELETE) always return HTTP 200, and the response reflects pre-seeded
  # data rather than the submitted payload. Tests are written to match the
  # documented contract of this specific API.
  #
  # The base URL is seeded automatically by the @Before hook (Hooks.java).
  # Individual scenarios can override it with:
  #   Given the API is available at "<url>"

  # ---------------------------------------------------------------------------
  # READ
  # ---------------------------------------------------------------------------

  @smoke @get-all
  Scenario: Retrieve all posts
    When I send a GET request to "/posts"
    Then the response status code should be 200
    And  the response content type should contain "application/json"
    And  the response should be a non-empty list
    And  each item in the list should have the fields "id,title,content,userId"

  # NOTE: jsonplaceholder.org only supports numeric ID routing for /posts/1.
  # All other numeric IDs redirect to the homepage (301). Use slug-based routing
  # (e.g. /posts/lorem-ipsum) if individual resources beyond ID 1 are needed.
  @smoke @get-single
  Scenario: Retrieve a single post by ID
    When I send a GET request to "/posts/1"
    Then the response status code should be 200
    And  the response content type should contain "application/json"
    And  the response field "id" should equal the integer 1
    And  the response field "title" should not be empty
    And  the response field "content" should not be empty
    And  the response field "userId" should be a positive integer

  # ---------------------------------------------------------------------------
  # CREATE
  # ---------------------------------------------------------------------------

  @smoke @create
  Scenario: Create a new post
    Given I have the following payload:
      | title   | My New Post Title |
      | content | My new post body  |
      | userId  | 1                 |
    When I send a POST request to "/posts"
    Then the response status code should be 200
    And  the response content type should contain "application/json"
    And  the response should be a non-empty list
    And  each item in the list should have the fields "id,title,content,userId"

  # ---------------------------------------------------------------------------
  # UPDATE
  # ---------------------------------------------------------------------------

  @smoke @update
  Scenario: Update an existing post
    Given I have the following payload:
      | title   | Updated Post Title |
      | content | Updated content    |
      | userId  | 1                  |
    When I send a PUT request to "/posts/1"
    Then the response status code should be 200
    And  the response content type should contain "application/json"
    And  the response field "id" should equal the integer 1
    And  the response field "title" should not be empty
    And  the response field "content" should not be empty

  # ---------------------------------------------------------------------------
  # DELETE
  # ---------------------------------------------------------------------------

  @smoke @delete
  Scenario: Delete an existing post
    When I send a DELETE request to "/posts/1"
    Then the response status code should be 200
    And  the response content type should contain "application/json"
    And  the response field "id" should equal the integer 1
    And  the response field "title" should not be empty
