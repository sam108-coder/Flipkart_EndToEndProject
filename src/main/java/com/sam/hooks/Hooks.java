package com.sam.hooks;

import com.sam.baseclass.Library;
import com.sam.database.ReportRepository;
import com.sam.utilities.ConfigReader;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.UUID;

public class Hooks extends Library {

    private static long scenarioStartTime;

    private String executionId;

    @Before
    public void setUp(Scenario scenario) {
        scenarioStartTime = System.currentTimeMillis();
        executionId = UUID.randomUUID().toString().substring(0, 8);
    }

    @After
    public void tearDown(Scenario scenario) {

        long duration = System.currentTimeMillis() - scenarioStartTime;

        String status = scenario.isFailed() ? "FAILED" : "PASSED";

        String errorMessage = scenario.isFailed()
                ? scenario.getName() + " failed"
                : null;

        if (scenario.isFailed() && driver != null) {

            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        ReportRepository.saveResult(
                executionId,
                scenario.getName(),
                scenario.getName(),
                status,
                ConfigReader.get("browser"),
                ConfigReader.get("environment"),
                duration,
                errorMessage
        );

        tearDown();

        System.out.println("Scenario: " + scenario.getName() + " | Status: " + status + " | Duration: " + duration + "ms");
    }

//    @Before
//    public void setUp() {
//
//        String browser =
//                ConfigReader.get("browser");
//
//        DriverManager.setDriver(
//                DriverFactory.createDriver(browser));
//    }
//
//    @After
//    public void tearDown(Scenario scenario) {
//
//        if (scenario.isFailed()) {
//
//            // Take screenshot here
//        }
//
//        DriverManager.quitDriver();
//    }
}
