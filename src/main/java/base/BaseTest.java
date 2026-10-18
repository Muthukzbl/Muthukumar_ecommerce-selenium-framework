package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ConfigReader;

import java.time.Duration;
import java.util.Collections;

/**
 * BaseTest serves as the foundation for all test classes.
 * Responsibilities:
 * 1. Initializes the WebDriver based on centralized configuration (ConfigReader).
 * 2. Applies timeouts and browser options (including anti-bot flags).
 * 3. Navigates to the base application URL.
 * 4. Provides reusable explicit wait utilities for child test and page classes.
 * 5. Provides a driver getter for TestListener to capture screenshots on failure.
 * 6. Guarantees safe teardown after every test.
 */
@Listeners(listeners.TestListener.class)
public class BaseTest {

    protected WebDriver driver;

    /**
     * Getter method to allow TestListener to access the active WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Initializes the browser session before each @Test method executes.
     */
    @BeforeMethod(alwaysRun = true)
    public void setup() {
        String browser = ConfigReader.getProperty("browser");
        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }

        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

        driver = createDriver(browser, isHeadless);

        // Configure window and timeouts from properties
        driver.manage().window().maximize();

        int implicitWait = ConfigReader.getIntProperty("implicitWait", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Navigate to the base URL
        String baseUrl = ConfigReader.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.get(baseUrl);
        } else {
            driver.get("https://automationexercise.com");
        }
    }

    /**
     * Creates a WebDriver instance based on the requested browser type.
     */
    private WebDriver createDriver(String browser, boolean isHeadless) {
        switch (browser.toLowerCase().trim()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                // Anti-bot detection flags to prevent site bot blockers
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                return new ChromeDriver(chromeOptions);
        }
    }

    /**
     * Explicit wait helper: waits until the element located by locator is visible.
     */
    public WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Explicit wait helper: waits until the element located by locator is clickable.
     */
    public WebElement waitForClickable(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Closes the active browser session after each @Test method executes.
     */
    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error while quitting WebDriver: " + e.getMessage());
            }
        }
    }
}
