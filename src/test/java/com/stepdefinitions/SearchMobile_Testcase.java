package com.stepdefinitions;

import com.pages.SearchPage;
import com.sam.baseclass.Library;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;

public class SearchMobile_Testcase extends Library {

    public static SearchPage sp;

    @Given("Launch the Flipkart Application")
    public void launch_the_flipkart_application() throws IOException {
        launchApplication();
    }

    @When("Close the Popup")
    public void close_the_popup() {
        sp = new SearchPage(driver);
        sp.handlePopup();
        System.out.println(driver.getTitle());
    }

    @Then("It Should Navigate to the Home Page")
    public void it_should_navigate_to_the_home_page() {
        sp = new SearchPage(driver);
        sp.homeScreen();
    }

    @Given("User enter the Text in the Search field")
    public void user_enter_the_text_in_the_search_field() {
        sp.search("Mobile");
    }

    @When("Click the Search button")
    public void click_the_search_button() {
        sp.clickSearch();
    }

    @Then("It Should navigate io the search result page and display the relevant details")
    public void it_should_navigate_io_the_search_result_page_and_display_the_relevant_details() {
        sp.result();
    }

    @Then("Extract the Results and print in console")
    public void extract_the_results_and_print_in_console() {
        sp.printEntireResults();
        System.out.println("***************************************");
    }

    @Then("Print the Third results")
    public void print_the_third_results() {
        sp.printThirdResult();
    }


}
