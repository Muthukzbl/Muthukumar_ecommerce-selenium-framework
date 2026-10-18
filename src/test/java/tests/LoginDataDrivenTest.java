package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

/**
 * LoginDataDrivenTest demonstrates Data-Driven Testing (DDT) using TestNG's @DataProvider.
 * It executes the same test method multiple times with different datasets.
 */
@Epic("Authentication Management")
@Feature("Login Validation")
public class LoginDataDrivenTest extends BaseTest {

    /**
     * @DataProvider supplies multiple sets of test data to the test method.
     * Each row in the 2D array represents one complete test iteration.
     */
    @DataProvider(name = "invalidLoginCredentials")
    public Object[][] getInvalidLoginData() {
        return new Object[][]{
                {"invalid_email_user1@mail.com", "wrongpass1", "incorrect"},
                {"invalid_email_user2@mail.com", "wrongpass2", "incorrect"},
                {"nonexistent_qa_user@mail.com", "fakeSecret123", "incorrect"}
        };
    }

    /**
     * Executes once for EVERY row in the DataProvider (3 iterations total).
     */
    @Test(
            dataProvider = "invalidLoginCredentials",
            groups = {"ddt", "regression"},
            description = "DDT: Verify error message is displayed for multiple sets of invalid credentials"
    )
    @Story("Negative Login with Multiple Datasets")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that invalid email/password combinations consistently trigger the error message.")
    public void testInvalidLoginDataDriven(String email, String password, String expectedKeyword) {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.navigateToLogin();

        // Perform login with current dataset
        loginPage.login(email, password);

        // Verify error message is displayed and contains the expected keyword
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Error message was not displayed for email: " + email);
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains(expectedKeyword.toLowerCase()),
                "Error message did not contain '" + expectedKeyword + "' for email: " + email);
    }
}

