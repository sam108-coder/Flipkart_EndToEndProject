package com.sam.baseclass;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class Library {

    public static WebDriver driver;
    public static Properties prop;

    public void launchApplication() throws IOException {

        FileInputStream input = new FileInputStream("src\\test\\java\\resources\\Properties\\Config.Property");
        prop = new Properties();
        prop.load(input);

        try {
            if (prop.getProperty("browser").equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (prop.getProperty("browser").equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if (prop.getProperty("browser").equalsIgnoreCase("edge")) {

                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            driver.get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Browser did not Launch");
        }
    }

    public void tearDown() {
        driver.quit();
    }
}
