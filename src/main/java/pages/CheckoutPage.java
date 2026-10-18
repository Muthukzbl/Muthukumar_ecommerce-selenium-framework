package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * CheckoutPage represents the order review and checkout step (https://automationexercise.com/checkout).
 * Encapsulates delivery address verification, order comments, and proceeding to payment.
 */
public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By addressDeliveryBox = By.id("address_delivery");
    private By commentTextArea = By.cssSelector("textarea[name='message']");
    private By placeOrderButton = By.cssSelector("a[href='/payment']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isAddressDetailsDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addressDeliveryBox)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterComment(String comment) {
        WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated(commentTextArea));
        commentField.clear();
        commentField.sendKeys(comment);
    }

    public PaymentPage clickPlaceOrder() {
        WebElement placeOrderBtn = wait.until(ExpectedConditions.presenceOfElementLocated(placeOrderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", placeOrderBtn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderBtn);
        }
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com/payment");
        }
        return new PaymentPage(driver);
    }
}

