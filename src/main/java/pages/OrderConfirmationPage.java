package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * OrderConfirmationPage represents the order confirmation and invoice download page
 * (https://automationexercise.com/payment_done).
 */
public class OrderConfirmationPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By orderPlacedHeader = By.cssSelector("[data-qa='order-placed']");
    private By successMessage = By.xpath("//p[contains(text(),'Your order has been placed successfully!') or contains(text(),'Congratulations')]");
    private By downloadInvoiceBtn = By.cssSelector("a.check_out");
    private By continueBtn = By.cssSelector("[data-qa='continue-button']");

    public OrderConfirmationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOrderPlacedMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedHeader)).isDisplayed()
                    || wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("payment_done");
        }
    }

    public String getSuccessMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isDownloadInvoiceButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(downloadInvoiceBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDownloadInvoice() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(downloadInvoiceBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public HomePage clickContinue() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(continueBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get("https://automationexercise.com");
        }
        return new HomePage(driver);
    }
}

