package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;

/**
 * CartTest verifies the shopping cart lifecycle:
 * 1. Navigate to Products page
 * 2. Add a product to the cart
 * 3. Open Cart from confirmation modal
 * 4. Verify product appears in the cart with correct quantity
 * 5. Remove product from cart and verify deletion
 */
public class CartTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "Verify adding product to cart and validating cart contents")
    public void testAddProductToCart() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.navigateToProducts();

        // 1. Get name of the first product
        String expectedProductName = productsPage.getFirstProductName();

        // 2. Add product to cart
        productsPage.addFirstProductToCart();

        // 3. Navigate to Cart page via modal
        CartPage cartPage = productsPage.clickViewCartFromModal();

        // 4. Verify cart page is visible
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not displayed!");

        // 5. Verify product exists in cart
        Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Cart is unexpectedly empty!");
        Assert.assertEquals(cartPage.getFirstCartItemQuantity(), "1", "Initial product quantity is not 1!");

        if (!expectedProductName.isEmpty()) {
            Assert.assertEquals(cartPage.getFirstCartItemName(), expectedProductName, 
                    "Cart item name does not match the product that was added!");
        }
    }

    @Test(priority = 2, groups = {"regression"}, description = "Verify removing a product from the cart")
    public void testRemoveProductFromCart() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.navigateToProducts();

        // 1. Add product to cart
        productsPage.addFirstProductToCart();

        // 2. Open cart
        CartPage cartPage = productsPage.clickViewCartFromModal();

        // 3. Verify item is in cart before removal
        Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Cart has no items to remove!");

        // 4. Click delete button on the item
        cartPage.removeFirstProduct();

        // 5. Verify cart is empty or item count reduced
        Assert.assertTrue(cartPage.isCartEmpty() || cartPage.getCartItemCount() == 0, 
                "Product was not removed from the cart!");
    }
}

