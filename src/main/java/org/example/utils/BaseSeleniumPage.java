package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public abstract class BaseSeleniumPage {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static Actions actions;

    public static void setDriver(WebDriver webDriver) {
        int time = Integer.parseInt(System.getProperty("timeout"));
        driver = webDriver;
        wait = new WebDriverWait(BaseSeleniumPage.driver, Duration.of(time, ChronoUnit.SECONDS));
        actions = new Actions(BaseSeleniumPage.driver);
    }
}
