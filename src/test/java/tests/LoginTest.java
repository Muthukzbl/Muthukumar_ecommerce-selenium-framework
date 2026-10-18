package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * LoginTest verifies user authentication flows:
 * - Login page visibility and title check
 * - Negative login test with invalid credentials
 * - Positive login test with valid registered credentials
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, groups = {"regression"}, description = "Verify login page loads with expected title")
    public void testLoginPageTitle() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.navigateToLogin();

        String expectedTitle = ConfigReader.getProperty("expectedLoginPageTitle");
        Assert.assertEquals(loginPage.getPageTitle(), expectedTitle, "Login page title is incorrect.");
        Assert.assertTrue(loginPage.isLoginPageVisible(), "Login form header is not displayed.");
    }

    @Test(priority = 2, groups = {"regression"}, description = "Verify error message when logging in with invalid credentials")
    public void testInvalidLogin() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.navigateToLogin();

        String invalidEmail = ConfigReader.getProperty("invalidUserEmail");
        String invalidPassword = ConfigReader.getProperty("invalidUserPassword");

        loginPage.login(invalidEmail, invalidPassword);

        // Verify error message is visible
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid login!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("incorrect"), 
                "Error message text does not match expected text: " + loginPage.getErrorMessage());
    }

    @Test(priority = 3, groups = {"smoke", "regression"}, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.navigateToLogin();

        String validEmail = ConfigReader.getProperty("validUserEmail");
        String validPassword = ConfigReader.getProperty("validUserPassword");

        HomePage loggedInHomePage = loginPage.login(validEmail, validPassword);

        // Assertion: Verify user is logged in
        Assert.assertTrue(loggedInHomePage.isUserLoggedIn(), "User was nt logged in successfully!");
    }
}
