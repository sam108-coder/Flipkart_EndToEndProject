package com.stepdefinitions;

import com.pages.FilterBrandsRamPage;
import com.sam.baseclass.Library;
import com.sam.reusablefunction.SeleniumReusable;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;

public class FilterByBrandsAndRam extends Library {

    FilterBrandsRamPage fp;
    SeleniumReusable se;

    @Then("Select Minimum and Maximum Amount")
    public void select_minimum_and_maximum_amount() throws InterruptedException {
        fp = new FilterBrandsRamPage(driver);
        String beforeFilter = driver.findElement(By.xpath("//*[@id='container']/div/div[3]/div[1]/div[2]/div[2]/div/div/div/a/div[2]/div[1]")).getText();
        System.out.println("Before Filter : " + beforeFilter);


        fp.minimum();
        se = new SeleniumReusable(driver);
        se.waits();
        fp.maximum();
        se.waits();
    }

    @Then("Select the Brand")
    public void select_the_brand() throws InterruptedException {
        fp.brandClick();
        se.waits();
    }

    @Then("Select the Ram")
    public void select_the_ram() throws InterruptedException {
        fp.ramSelect();
        se.waits();
    }

    @Then("Select the Battery Capacity")
    public void select_the_battery_capacity() throws InterruptedException {
        fp.batteryClick();
        se.waits();
    }

    @Then("It should display the relevant results")
    public void it_should_display_the_relevant_results() {
        System.out.println("******************************************");
        String afterFilter = driver.findElement(By.xpath("//*[@id='container']/div/div[3]/div/div[2]/div[2]/div/div/div/a/div[2]/div[1]")).getText();
        System.out.println("Before Filter : " + afterFilter);
    }



}
