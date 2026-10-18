package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils captures screenshots during test execution (primarily on failure).
 * Screenshots are stored in the 'screenshots' directory with timestamps for unique traceability.
 */
public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "screenshots";

    /**
     * Captures a screenshot and saves it to the screenshots directory.
     *
     * @param driver the active WebDriver instance
     * @param testName name of the failing test method
     * @return relative path to the saved screenshot file, or null if capture failed
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            System.err.println("Cannot take screenshot: WebDriver instance is null.");
            return null;
        }

        try {
            // Ensure screenshots directory exists
            Path directoryPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Create timestamped filename: testName_yyyyMMdd_HHmmss.png
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            Path targetPath = directoryPath.resolve(fileName);

            // Capture screenshot as temporary file
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy to target path
            Files.copy(srcFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot captured: " + targetPath.toAbsolutePath());

            return targetPath.toString();
        } catch (IOException e) {
            System.err.println("Failed to save screenshot for test: " + testName + ". Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error taking screenshot: " + e.getMessage());
            return null;
        }
    }
}

