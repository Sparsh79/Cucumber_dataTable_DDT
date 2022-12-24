package com.knoldus;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        monochrome = true,
        features = {"src/test/resources/features/test.feature"},
        plugin = {"pretty"},
        glue = {"classpath:com.knoldus"}
)
public class DataRunner extends AbstractTestNGCucumberTests {

}
