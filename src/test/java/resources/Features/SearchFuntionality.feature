Feature: To Validate the Flipkart Application
  Background:
    Given Launch the Flipkart Application
    When Close the Popup
    Then It Should Navigate to the Home Page

  Scenario: To Validate the Search Functionality
    Given User enter the Text in the Search field
    When Click the Search button
    Then It Should navigate io the search result page and display the relevant details
    Then Extract the Results and print in console
    Then Print the Third results

    And Select Minimum and Maximum Amount
    And Select the Brand
    And Select the Ram
    And Select the Battery Capacity
    Then It should display the relevant results






