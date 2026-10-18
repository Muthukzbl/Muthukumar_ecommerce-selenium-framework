package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * HomePage represents the main landing page and top navigation of AutomationExercise.
 * Responsibilities:
 * - Top navigation links (Home, Products, Cart, Signup/Login, Logout)
 * - Home page visibility verification
 * - User session verification (Logged in as username)
 */
public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators (Private for Encapsulation)
    private By homeLogo = By.cssSelector(".logo img");
    private By signupLoginLink = By.cssSelector("a[href='/login']");
    private By productsLink = By.cssSelector("a[href='/products']");
    private By cartLink = By.cssSelector("a[href='/view_cart']");
    private By logoutLink = By.cssSelector("a[href='/logout']");
    private By loggedInUserText = By.xpath("//a[contains(text(), 'Logged in as')]");

    // 2. Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Page Actions

    public boolean isHomePageVisible() {
        try {
            WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(homeLogo));
            return logo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public LoginPage navigateToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)).click();
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/login");
        }
        return new LoginPage(driver);
    }

    public ProductsPage navigateToProducts() {
        wait.until(ExpectedConditions.elementToBeClickable(productsLink)).click();
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/products");
        }
        return new ProductsPage(driver);
    }

    public CartPage navigateToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/view_cart");
        }
        return new CartPage(driver);
    }

    public boolean isUserLoggedIn() {
        try {
            WebElement userText = wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInUserText));
            return userText.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedInUserName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInUserText)).getText();
    }

    public LoginPage clickLogout() {
        WebElement logoutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(logoutLink));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutBtn);
        }
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/login");
        }
        return new LoginPage(driver);
    }
}
