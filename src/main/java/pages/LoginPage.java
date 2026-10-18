package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage represents the Signup / Login page (https://automationexercise.com/login).
 * Encapsulates login elements, credentials entry, error message verification, and navigation.
 */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators (Private for Encapsulation)
    private By loginHeader = By.xpath("//h2[text()='Login to your account']");
    private By emailField = By.cssSelector("[data-qa='login-email']");
    private By passwordField = By.cssSelector("[data-qa='login-password']");
    private By loginButton = By.cssSelector("[data-qa='login-button']");
    private By loginErrorMessage = By.xpath("//div[@class='login-form']//p[contains(text(),'incorrect')]");

    // Signup form locators (for stretch goal validation)
    private By signupHeader = By.xpath("//h2[text()='New User Signup!']");
    private By signupNameField = By.cssSelector("[data-qa='signup-name']");
    private By signupEmailField = By.cssSelector("[data-qa='signup-email']");
    private By signupButton = By.cssSelector("[data-qa='signup-button']");

    // 2. Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Page Actions

    public boolean isLoginPageVisible() {
        try {
            if (driver.getCurrentUrl().contains("#google_vignette")) {
                driver.get("https://automationexercise.com/login");
            }
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("/login");
        }
    }

    public void enterEmail(String email) {
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passElement.clear();
        passElement.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    /**
     * Executes login action and returns a HomePage instance for method chaining.
     */
    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        return new HomePage(driver);
    }

    /**
     * Fills name and email in the New User Signup section and clicks Signup.
     */
    public SignupPage signup(String name, String email) {
        WebElement nameEle = wait.until(ExpectedConditions.visibilityOfElementLocated(signupNameField));
        nameEle.clear();
        nameEle.sendKeys(name);
        WebElement emailEle = wait.until(ExpectedConditions.visibilityOfElementLocated(signupEmailField));
        emailEle.clear();
        emailEle.sendKeys(email);
        
        WebElement signupBtn = wait.until(ExpectedConditions.elementToBeClickable(signupButton));
        try {
            signupBtn.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", signupBtn);
        }
        return new SignupPage(driver);
    }

    /**
     * Retrieves the text of the error message displayed on invalid login.
     */
    public String getErrorMessage() {
        try {
            WebElement errorEle = wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage));
            return errorEle.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
