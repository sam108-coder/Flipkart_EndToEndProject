package com.pages;

import com.sam.baseclass.Library;
import com.sam.reusablefunction.SeleniumReusable;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SearchPage extends Library {
    SeleniumReusable se;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.se = new SeleniumReusable(driver);
    }

    @FindBy(xpath = "//input[@placeholder='Search for Products, Brands and More']")
    WebElement searchtext;

    @FindBy(xpath = "//html[@lang='en-IN']")
    WebElement homepage;


    @FindBy(xpath = "//html[@lang='en']")
    WebElement searchResult;

    @FindBy(xpath = "//span[@role='button']")
    WebElement popup;

    @FindBy(xpath = "//*[@id='container']/div/div[3]/div[1]/div[2]/div/div/div/div/a/div[2]/div[1]")
    List<WebElement> entireResult;

    @FindBy(xpath = "//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[4]/div/div/div")
    WebElement thirdResult;

    public void handlePopup() {
        se = new SeleniumReusable(driver);
        se.click(popup);
    }

    public void search(String text) {
        se.enterValue(searchtext, text);
    }

    public void clickSearch() {
        searchtext.sendKeys(Keys.ENTER);
    }

    public void homeScreen() {
        System.out.println(homepage.isDisplayed());
    }

    public void result() {
        System.out.println(searchResult.isDisplayed());
        System.out.println(driver.getTitle());
    }

    public void printEntireResults() {
        se.multipleGetText(entireResult);
    }

    public void printThirdResult() {
        se.getValue(thirdResult);
    }
}
