package com.pages;

import com.sam.baseclass.Library;
import com.sam.reusablefunction.SeleniumReusable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FilterBrandsRamPage extends Library {

    SeleniumReusable se;


    public FilterBrandsRamPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.se = new SeleniumReusable(driver);
    }

    @FindBy(xpath = "//*[@id='container']/div/div[3]/div[1]/div[1]/div/div[1]/div/section[8]/div[4]/div[1]/select")
    WebElement minimumAmount;

    @FindBy(xpath = "//*[@id='container']/div/div[3]/div[1]/div[1]/div/div[1]/div/section[8]/div[4]/div[3]/select")
    WebElement maximumAmount;

    @FindBy(xpath = "//div[text()='vivo']/preceding-sibling::div")
    WebElement brand;

    @FindBy(xpath = "//div[text()='3 GB']/preceding-sibling::div")
    WebElement ram;

    @FindBy(xpath = "//div[text()='Battery Capacity']/parent::div")
    WebElement batteryRow;

    @FindBy(xpath = "//div[text()='5000 - 5999 mAh']/preceding-sibling::div")
    WebElement batteryCapacity;


    public void minimum() {
        se = new SeleniumReusable(driver);
        se.dropDown(minimumAmount, "10000");
    }

    public void maximum() {
        se.dropDown(maximumAmount, "20000");
    }

    public void brandClick() {
        se.click(brand);
    }

    public void ramSelect() {
        se.scrollDown(ram);
        se.click(ram);
    }

    public void batteryClick(){
        se.scrollDown(batteryRow);
        se.click(batteryRow);
        se.click(batteryCapacity);
    }


}
