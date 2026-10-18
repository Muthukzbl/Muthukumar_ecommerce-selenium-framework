package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * LogoutTest verifies the user logout functionality:
 * 1. Log in with valid credentials
 * 2. Verify logged-in state
 * 3. Click Logout
 * 4. Verify user is redirected back to the Login page
 */
public class LogoutTest extends BaseTest {

    @Test(priority = 1, groups = {"regression"}, description = "Verify that a logged-in user can log out successfully")
    public void testUserLogout() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.navigateToLogin();

        // 1. Perform valid login
        String email = ConfigReader.getProperty("validUserEmail");
        String password = ConfigReader.getProperty("validUserPassword");
        HomePage loggedInHome = loginPage.login(email, password);

        // 2. Validate user is logged in
        Assert.assertTrue(loggedInHome.isUserLoggedIn(), "User was not logged in prior to logout test!");

        // 3. Click Logout
        LoginPage postLogoutLoginPage = loggedInHome.clickLogout();

        // 4. Validate redirect back to Login page
        Assert.assertTrue(postLogoutLoginPage.isLoginPageVisible(), "User was not redirected to the Login page after logout!");
    }
}

