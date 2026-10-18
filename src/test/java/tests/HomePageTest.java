package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;
import utils.ConfigReader;

/**
 * HomePageTest verifies the main landing page and header navigation.
 */
public class HomePageTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "Verify home page logo and title are displayed")
    public void testHomePageVisibility() {
        HomePage homePage = new HomePage(driver);

        // Verify the home page is visibly loaded
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page logo is not displayed!");

        // Verify title
        String expectedTitle = ConfigReader.getProperty("expectedHomePageTitle");
        Assert.assertEquals(homePage.getPageTitle(), expectedTitle, "Home page title does not match!");
    }

    @Test(priority = 2, groups = {"regression"}, description = "Verify navigation from Home to Products page")
    public void testNavigateToProducts() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.navigateToProducts();

        Assert.assertTrue(productsPage.isProductsPageVisible(), "Products page header is not visible!");
    }

    @Test(priority = 3, groups = {"regression"}, description = "Verify navigation from Home to Cart page")
    public void testNavigateToCart() {
        HomePage homePage = new HomePage(driver);
        CartPage cartPage = homePage.navigateToCart();

        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page breadcrumb is not visible!");
    }
}

