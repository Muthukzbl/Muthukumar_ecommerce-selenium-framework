package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * ProductsPage represents the product catalog and search functionality
 * (https://automationexercise.com/products).
 * Encapsulates search operations, product listing, and adding products to cart.
 */
public class ProductsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators (Private for Encapsulation)
    private By allProductsTitle = By.xpath("//h2[contains(@class,'title') and (contains(text(),'All Products') or contains(text(),'ALL PRODUCTS'))]");
    private By searchInput = By.id("search_product");
    private By searchButton = By.id("submit_search");
    private By searchedProductsTitle = By.cssSelector(".features_items .title");
    private By productItems = By.cssSelector(".features_items .col-sm-4");
    private By productNames = By.cssSelector(".features_items .productinfo p");
    private By firstAddToCartBtn = By.xpath("(//div[@class='productinfo text-center']//a[contains(@class,'add-to-cart')])[1]");

    // Modal locators displayed after adding to cart
    private By cartModal = By.id("cartModal");
    private By continueShoppingBtn = By.cssSelector("button.close-modal");
    private By viewCartLink = By.xpath("//div[@id='cartModal']//a[@href='/view_cart']");

    // 2. Constructor
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Page Actions

    public boolean isProductsPageVisible() {
        try {
            if (driver.getCurrentUrl().contains("#google_vignette")) {
                driver.get("https://automationexercise.com/products");
            }
            return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("/products");
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void searchProduct(String productName) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(productName);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public boolean isSearchedProductsTitleVisible() {
        try {
            WebElement titleEle = wait.until(ExpectedConditions.visibilityOfElementLocated(searchedProductsTitle));
            return titleEle.isDisplayed() && titleEle.getText().equalsIgnoreCase("SEARCHED PRODUCTS");
        } catch (Exception e) {
            return false;
        }
    }

    public int getSearchResultsCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchedProductsTitle));
            List<WebElement> items = driver.findElements(productItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstProductName() {
        try {
            List<WebElement> names = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
            return names.isEmpty() ? "" : names.get(0).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void addFirstProductToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(firstAddToCartBtn));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addBtn);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        }
    }

    public void clickContinueShopping() {
        WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(continueShoppingBtn));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cartModal));
    }

    public CartPage clickViewCartFromModal() {
        WebElement viewCartEle = wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartLink));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(viewCartEle)).click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", viewCartEle);
        }
        return new CartPage(driver);
    }
}

