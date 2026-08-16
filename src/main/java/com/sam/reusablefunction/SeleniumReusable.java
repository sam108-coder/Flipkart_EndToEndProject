package com.sam.reusablefunction;

import com.sam.baseclass.Library;
import org.aspectj.util.FileUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

public class SeleniumReusable extends Library {

    public SeleniumReusable(WebDriver driver) {
        this.driver = driver;
    }

    public void enterValue(WebElement element, String text) {
        try {
            element.sendKeys(text);
        } catch (Exception e) {
            System.out.println("No such element exception");
        }
    }

    public void click(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            System.out.println("No such element exception");
        }
    }

    public void getTitle(WebElement element) {
        try {
            System.out.println(driver.getTitle());
        } catch (Exception e) {
            System.out.println("Could not get title");
        }
    }

    public void multipleGetText(List<WebElement> element) {
        List<WebElement> text = element;
        System.out.println(text.size());

        for (WebElement textCount : text) {
            String totalList = textCount.getText();
            System.out.println("*************************************************");
            System.out.println(totalList);
        }
    }

    public void getValue(WebElement element) {
        String text = element.getText();
        System.out.println(text);
    }

    public void dropDown(WebElement element, String text) {
        Select drop = new Select(element);
        drop.selectByValue(text);
    }

    public void scrollDown(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
    }

    public void waits() throws InterruptedException {
        Thread.sleep(3000);
    }

    public void screenshot(String path) {

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        try {
            FileUtil.copyFile(source, new File(path));
        } catch (Exception e) {
            System.out.println("Could not take screenshot");
        }
    }
}
