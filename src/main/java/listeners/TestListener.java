package listeners;

import base.BaseTest;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

/**
 * TestListener implements TestNG's ITestListener to monitor test execution events.
 * Crucially, it automatically captures local screenshots and attaches them to Allure Reports on failure.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== TEST SUITE STARTED: " + context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========== TEST SUITE FINISHED: " + context.getName() + " ==========");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("--> Starting Test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("--> Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.err.println("--> Test FAILED: " + testName);
        System.err.println("    Failure Reason: " + result.getThrowable());

        // Extract WebDriver instance from running test class
        Object currentClass = result.getInstance();
        if (currentClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) currentClass).getDriver();
            if (driver != null) {
                // 1. Save screenshot to local disk
                String screenshotPath = ScreenshotUtils.captureScreenshot(driver, testName);
                if (screenshotPath != null) {
                    System.out.println("    Saved failure screenshot at: " + screenshotPath);
                }

                // 2. Attach screenshot directly into Allure Report
                saveScreenshotToAllure(testName, driver);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("--> Test SKIPPED: " + result.getMethod().getMethodName());
    }

    /**
     * Attaches screenshot bytes directly to the Allure HTML report.
     */
    @Attachment(value = "Failure Screenshot: {0}", type = "image/png")
    public byte[] saveScreenshotToAllure(String testName, WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Failed to attach screenshot to Allure: " + e.getMessage());
            return new byte[0];
        }
    }
}
