package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * CartPage represents the Shopping Cart view (https://automationexercise.com/view_cart).
 * Encapsulates cart table rows, item verification, quantity checks, and product removal.
 */
public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators (Private for Encapsulation)
    private By shoppingCartBreadcrumb = By.xpath("//li[@class='active' and contains(text(),'Shopping Cart')]");
    private By cartRows = By.cssSelector("#cart_info_table tbody tr");
    private By cartItemNames = By.cssSelector(".cart_description h4 a");
    private By cartItemQuantities = By.cssSelector(".cart_quantity button");
    private By deleteButtons = By.cssSelector(".cart_quantity_delete");
    private By emptyCartMessage = By.id("empty_cart");
    private By proceedToCheckoutBtn = By.cssSelector(".check_out");
    private By registerLoginModalLink = By.xpath("//div[@id='checkoutModal']//a[@href='/login']");

    // 2. Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Page Actions

    public boolean isCartPageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartBreadcrumb)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public int getCartItemCount() {
        try {
            List<WebElement> rows = driver.findElements(cartRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstCartItemName() {
        try {
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemNames));
            return name.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getFirstCartItemQuantity() {
        try {
            WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemQuantities));
            return qty.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void removeFirstProduct() {
        try {
            WebElement deleteBtn = wait.until(ExpectedConditions.presenceOfElementLocated(deleteButtons));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", deleteBtn);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
            } catch (Exception e) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);
            }
            // Wait for row removal
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(cartRows, getCartItemCount() + 1));
        } catch (Exception e) {
            System.err.println("Could not click delete button or wait for row removal: " + e.getMessage());
        }
    }

    public boolean isCartEmpty() {
        try {
            WebElement emptySpan = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
            return emptySpan.isDisplayed() || driver.findElements(cartRows).isEmpty();
        } catch (Exception e) {
            return driver.findElements(cartRows).isEmpty();
        }
    }

    public void clickProceedToCheckout() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(proceedToCheckoutBtn));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public LoginPage clickRegisterLoginFromModal() {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(registerLoginModalLink));
        try {
            link.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/login");
        }
        return new LoginPage(driver);
    }

    public CheckoutPage proceedToCheckout() {
        clickProceedToCheckout();
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/checkout");
        }
        return new CheckoutPage(driver);
    }
}

