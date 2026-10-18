package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductsPage;
import utils.ConfigReader;

/**
 * ProductSearchTest verifies searching for catalog products:
 * 1. Navigate to Products page
 * 2. Search for a product keyword (e.g., 'Tshirt')
 * 3. Verify 'SEARCHED PRODUCTS' section is displayed
 * 4. Verify search results are returned and match keyword
 */
public class ProductSearchTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "Verify searching for a product displays relevant results")
    public void testSearchProduct() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.navigateToProducts();

        // 1. Verify Products page is visible
        Assert.assertTrue(productsPage.isProductsPageVisible(), "Products page is not visible!");

        // 2. Perform search using test data
        String searchKeyword = ConfigReader.getProperty("searchKeyword");
        productsPage.searchProduct(searchKeyword);

        // 3. Verify 'SEARCHED PRODUCTS' title
        Assert.assertTrue(productsPage.isSearchedProductsTitleVisible(), "'SEARCHED PRODUCTS' title is not visible!");

        // 4. Verify results count
        int resultsCount = productsPage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0, "No products were found for search keyword: " + searchKeyword);

        // 5. Verify the first result name contains the search keyword (case-insensitive)
        String firstProductName = productsPage.getFirstProductName();
        Assert.assertTrue(firstProductName.toLowerCase().contains(searchKeyword.toLowerCase()), 
                "Product title '" + firstProductName + "' does not contain search keyword: " + searchKeyword);
    }
}

