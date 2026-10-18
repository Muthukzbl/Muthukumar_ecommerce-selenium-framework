package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

/**
 * Test Case 24: Download Invoice after purchase order
 * Full End-to-End E-Commerce Flow:
 * 1. Verify Home Page
 * 2. Add product to cart
 * 3. Proceed to Checkout as guest -> Modal triggers Register/Login
 * 4. Register new user dynamically with unique credentials
 * 5. Verify account creation and logged-in user state
 * 6. Return to Cart & Proceed to Checkout
 * 7. Verify delivery address and enter order comment
 * 8. Place order and enter payment card details
 * 9. Confirm order and verify order confirmation
 * 10. Download invoice and finish order
 */
@Epic("E-Commerce Purchase Flow")
@Feature("Checkout & Invoice Generation")
public class DownloadInvoiceTest extends BaseTest {

    @Test(priority = 1, groups = {"e2e", "regression"}, description = "Test Case 24: Full end-to-end checkout with registration and invoice download")
    @Story("Complete End-to-End Purchase Order with Invoice Download")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Automates the entire 20-step e-commerce journey: Product selection, guest checkout modal, dynamic user registration, address verification, payment processing, order confirmation, and invoice download.")
    public void testDownloadInvoiceAfterPurchaseOrder() {
        // Step 1 & 2: Launch browser & verify Home Page
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible!");

        // Step 3: Add product to cart
        ProductsPage productsPage = homePage.navigateToProducts();
        productsPage.addFirstProductToCart();

        // Step 4 & 5: Open Cart & verify cart page
        CartPage cartPage = productsPage.clickViewCartFromModal();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not displayed!");

        // Step 6 & 7: Click Proceed to Checkout & Click Register / Login from modal
        cartPage.clickProceedToCheckout();
        LoginPage loginPage = cartPage.clickRegisterLoginFromModal();
        Assert.assertTrue(loginPage.isLoginPageVisible(), "Login/Signup page is not displayed!");

        // Step 8: Dynamic signup with unique timestamped email to avoid duplicates
        long timestamp = System.currentTimeMillis();
        String uniqueName = "TestUser" + timestamp;
        String uniqueEmail = "sdet_" + timestamp + "@automationtest.com";

        SignupPage signupPage = loginPage.signup(uniqueName, uniqueEmail);

        // Step 9: Fill account information and create account
        signupPage.fillAccountInformation(
                "Pass12345!", "15", "8", "1995",
                "Automation", "Tester", "QA Org", "123 Tech Park Way",
                "United States", "California", "Los Angeles", "90001", "1234567890"
        );

        // Step 10: Verify 'ACCOUNT CREATED!' and click Continue
        Assert.assertTrue(signupPage.isAccountCreatedDisplayed(), "'ACCOUNT CREATED!' is not displayed!");
        homePage = signupPage.clickContinue();

        // Step 11: Verify 'Logged in as username' at top
        Assert.assertTrue(homePage.isUserLoggedIn(), "'Logged in as username' is not visible!");

        // Step 12 & 13: Click Cart & Proceed to Checkout as logged in user
        cartPage = homePage.navigateToCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        // Step 14 & 15: Verify Address Details, enter comment, click Place Order
        Assert.assertTrue(checkoutPage.isAddressDetailsDisplayed(), "Delivery address details are not displayed!");
        checkoutPage.enterComment("Automated E2E Test Order - Test Case 24");
        PaymentPage paymentPage = checkoutPage.clickPlaceOrder();

        // Step 16 & 17: Enter payment details and confirm order
        OrderConfirmationPage confirmationPage = paymentPage.enterPaymentDetailsAndConfirm(
                "Automation Tester", "4111111111111111", "311", "12", "2028"
        );

        // Step 18: Verify order placement confirmation
        Assert.assertTrue(confirmationPage.isOrderPlacedMessageVisible(), 
                "Order success message was not visible!");

        // Step 19: Verify Download Invoice button is available and click it
        Assert.assertTrue(confirmationPage.isDownloadInvoiceButtonVisible(), 
                "Download Invoice button is not displayed!");
        confirmationPage.clickDownloadInvoice();

        // Step 20: Click Continue
        HomePage finalHomePage = confirmationPage.clickContinue();
        Assert.assertTrue(finalHomePage.isHomePageVisible(), 
                "Did not return to Home Page after completing order!");
    }
}

