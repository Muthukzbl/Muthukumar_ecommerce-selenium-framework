package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * SignupPage handles entering new user registration information and verifying account creation.
 * Target URLs: /signup and /account_created
 */
public class SignupPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for 'Enter Account Information' form
    private By genderMaleRadio = By.id("id_gender1");
    private By passwordField = By.cssSelector("[data-qa='password']");
    private By daysDropdown = By.cssSelector("[data-qa='days']");
    private By monthsDropdown = By.cssSelector("[data-qa='months']");
    private By yearsDropdown = By.cssSelector("[data-qa='years']");

    private By firstNameField = By.cssSelector("[data-qa='first_name']");
    private By lastNameField = By.cssSelector("[data-qa='last_name']");
    private By companyField = By.cssSelector("[data-qa='company']");
    private By address1Field = By.cssSelector("[data-qa='address']");
    private By address2Field = By.cssSelector("[data-qa='address2']");
    private By countryDropdown = By.cssSelector("[data-qa='country']");
    private By stateField = By.cssSelector("[data-qa='state']");
    private By cityField = By.cssSelector("[data-qa='city']");
    private By zipcodeField = By.cssSelector("[data-qa='zipcode']");
    private By mobileNumberField = By.cssSelector("[data-qa='mobile_number']");
    private By createAccountButton = By.cssSelector("[data-qa='create-account']");

    // Locators for Account Created page
    private By accountCreatedHeader = By.cssSelector("[data-qa='account-created']");
    private By continueButton = By.cssSelector("[data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Fills out the entire registration form and clicks Create Account.
     */
    public void fillAccountInformation(
            String password, String day, String month, String year,
            String firstName, String lastName, String company, String address,
            String country, String state, String city, String zip, String mobile) {

        // Select Gender
        WebElement genderRadio = wait.until(ExpectedConditions.presenceOfElementLocated(genderMaleRadio));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", genderRadio);

        // Password
        driver.findElement(passwordField).sendKeys(password);

        // Date of Birth
        new Select(driver.findElement(daysDropdown)).selectByValue(day);
        new Select(driver.findElement(monthsDropdown)).selectByValue(month);
        new Select(driver.findElement(yearsDropdown)).selectByValue(year);

        // Address Details
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(companyField).sendKeys(company);
        driver.findElement(address1Field).sendKeys(address);
        new Select(driver.findElement(countryDropdown)).selectByVisibleText(country);
        driver.findElement(stateField).sendKeys(state);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(zipcodeField).sendKeys(zip);
        driver.findElement(mobileNumberField).sendKeys(mobile);

        // Click Create Account button (with scroll and JS fallback to bypass ad overlays)
        WebElement createBtn = driver.findElement(createAccountButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", createBtn);
        try {
            createBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createBtn);
        }
    }

    public boolean isAccountCreatedDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(accountCreatedHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("account_created");
        }
    }

    public HomePage clickContinue() {
        WebElement continueBtn = wait.until(ExpectedConditions.presenceOfElementLocated(continueButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", continueBtn);
        try {
            continueBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
        }
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com");
        }
        return new HomePage(driver);
    }
}

