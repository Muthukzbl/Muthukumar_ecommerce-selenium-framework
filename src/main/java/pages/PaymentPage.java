package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * PaymentPage represents the credit card payment interface (https://automationexercise.com/payment).
 * Encapsulates entering card information and confirming order payment.
 */
public class PaymentPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using reliable data-qa attributes
    private By nameOnCardField = By.cssSelector("[data-qa='name-on-card']");
    private By cardNumberField = By.cssSelector("[data-qa='card-number']");
    private By cvcField = By.cssSelector("[data-qa='cvc']");
    private By expiryMonthField = By.cssSelector("[data-qa='expiry-month']");
    private By expiryYearField = By.cssSelector("[data-qa='expiry-year']");
    private By payButton = By.cssSelector("[data-qa='pay-button']");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public OrderConfirmationPage enterPaymentDetailsAndConfirm(
            String nameOnCard, String cardNumber, String cvc, String month, String year) {

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameOnCardField));
        nameField.sendKeys(nameOnCard);
        driver.findElement(cardNumberField).sendKeys(cardNumber);
        driver.findElement(cvcField).sendKeys(cvc);
        driver.findElement(expiryMonthField).sendKeys(month);
        driver.findElement(expiryYearField).sendKeys(year);

        WebElement payBtn = driver.findElement(payButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", payBtn);
        try {
            payBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", payBtn);
        }

        return new OrderConfirmationPage(driver);
    }
}

