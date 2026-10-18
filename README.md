# 🛒 E-Commerce Test Automation Framework (Selenium Java + TestNG + Allure)

A production-grade, modular, Page Object Model (POM) test automation framework built using **Java**, **Selenium WebDriver**, **TestNG**, **Allure Report**, and **Maven**.

This framework automates real-world e-commerce user journeys on [AutomationExercise.com](https://automationexercise.com/), featuring centralized configuration, explicit wait strategies, automated failure screenshot capture via TestNG listeners, Data-Driven Testing (DDT), interactive Allure reporting, and a full 20-step end-to-end checkout & invoice generation test suite.

---

## 🏗️ Architecture & Design Pattern

The framework strictly adheres to the **Page Object Model (POM)** design pattern with a clean separation of concerns:

```
selenium-learning/
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   └── BaseTest.java              # WebDriver lifecycle, multi-browser factory, anti-bot flags, explicit wait helpers
│   │   ├── pages/                         # Encapsulated page objects (locators + actions)
│   │   │   ├── HomePage.java              # Navigation, headers, user session checks, logout
│   │   │   ├── LoginPage.java             # Authentication, error handling, signup initiation
│   │   │   ├── SignupPage.java            # Account registration & personal details form
│   │   │   ├── ProductsPage.java          # Catalog search, product selection, add to cart
│   │   │   ├── CartPage.java              # Cart items validation, quantity verification, item removal
│   │   │   ├── CheckoutPage.java          # Address details review, order comments, place order
│   │   │   ├── PaymentPage.java           # Payment card entry and confirmation
│   │   │   └── OrderConfirmationPage.java # Order placement verification, invoice download
│   │   ├── utils/
│   │   │   ├── ConfigReader.java          # Centralized configuration & test data reader with CLI overrides
│   │   │   └── ScreenshotUtils.java       # Timestamped screenshot capture on test failure
│   │   └── listeners/
│   │       └── TestListener.java          # TestNG ITestListener: logging, failure screenshots & Allure report attachments
│   └── test/
│       ├── java/tests/                    # TestNG test classes with prioritization & groups
│       │   ├── HomePageTest.java          # Home page visibility, title, and navigation tests
│       │   ├── LoginTest.java             # Valid/Invalid login and title checks
│       │   ├── LoginDataDrivenTest.java   # Data-Driven Testing (DDT) with TestNG @DataProvider & Allure metadata
│       │   ├── LogoutTest.java            # Session teardown and redirect validation
│       │   ├── ProductSearchTest.java     # Product catalog search validation
│       │   ├── CartTest.java              # Add to cart & remove from cart validations
│       │   └── DownloadInvoiceTest.java   # Full 20-step E2E checkout & invoice download (Test Case 24)
│       └── resources/
│           ├── config.properties          # Environment settings (browser, timeouts, baseUrl)
│           └── testdata.properties        # Externalized test data and assertions
├── testng.xml                             # Full Regression Test Suite XML
├── testng-smoke.xml                       # Targeted Smoke Suite XML
├── screenshots/                           # Directory for failure screenshots
├── pom.xml                                # Dependencies (Selenium 4.24, TestNG 7.10, Allure 2.27) & Surefire
├── .gitignore                             # Clean repository exclusions
└── README.md                              # Professional GitHub documentation
```

---

## 🛠️ Technology Stack

| Component | Technology | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Java | 11+ / 24 | Core programming language |
| **Automation Tool** | Selenium WebDriver | 4.24.0 | Browser automation (Chrome, Firefox) |
| **Test Runner** | TestNG | 7.10.2 | Test management, assertions, grouping, @DataProvider |
| **Reporting** | Allure Report | 2.27.0 | Interactive visual HTML execution reports with attachments |
| **Build Tool** | Apache Maven | 3.9+ | Dependency management, build lifecycle, test execution |
| **Design Pattern** | Page Object Model | - | Separation of UI locators from test logic |

---

## 📋 Scenarios Automated

All automated scenarios align with the official test specifications from [AutomationExercise Test Cases](https://automationexercise.com/test_cases):

1. **Test Case 1 & 2: Home Page & Navigation** (`HomePageTest.java`)
   - Verify home page visibility and title.
   - Verify header navigation to Products and Cart pages.
2. **Test Case 2: Login User with Correct Credentials** (`LoginTest.java`)
   - Authenticate with valid credentials and verify "Logged in as username".
3. **Test Case 3: Login User with Incorrect Credentials** (`LoginTest.java`)
   - Negative test validating error message *"Your email or password is incorrect!"*.
4. **Data-Driven Testing (DDT): Multiple Invalid Credentials** (`LoginDataDrivenTest.java`)
   - Uses `@DataProvider` to execute 3 distinct invalid email/password combinations in one automated cycle.
5. **Test Case 4: Logout User** (`LogoutTest.java`)
   - Verify active session termination and redirection back to login screen.
6. **Test Case 9: Search Product** (`ProductSearchTest.java`)
   - Search catalog for keyword (`Tshirt`), verify search count and matching results.
7. **Test Case 12 & 17: Add & Remove Products from Cart** (`CartTest.java`)
   - Add product to cart, verify cart table entries and quantity.
   - Remove product from cart and verify deletion.
8. **Test Case 24: End-to-End Purchase & Download Invoice** (`DownloadInvoiceTest.java`)
   - **Full 20-step e-commerce journey**: Add to cart $\rightarrow$ Proceed to Checkout $\rightarrow$ Dynamic User Registration $\rightarrow$ Account Creation $\rightarrow$ Return to Cart $\rightarrow$ Address Verification $\rightarrow$ Payment Confirmation $\rightarrow$ Order Placed $\rightarrow$ Download Invoice $\rightarrow$ Finish Order.

---

## ⚡ Key Framework Features

### 1. Data-Driven Testing (DDT) with TestNG `@DataProvider`
Test data is decoupled from test logic:
```java
@DataProvider(name = "invalidLoginCredentials")
public Object[][] getInvalidLoginData() {
    return new Object[][]{
        {"invalid_email_user1@mail.com", "wrongpass1", "incorrect"},
        {"invalid_email_user2@mail.com", "wrongpass2", "incorrect"},
        {"nonexistent_qa_user@mail.com", "fakeSecret123", "incorrect"}
    };
}
```

### 2. Allure Report Integration with Dynamic Screenshots
- Annotated with `@Epic`, `@Feature`, `@Story`, `@Severity`, and `@Description`.
- `TestListener.java` automatically attaches full-resolution PNG screenshots to the Allure Report upon any test failure via `@Attachment`.

### 3. Centralized Configuration & Command-Line Overrides
All environment properties are managed in `src/test/resources/config.properties`.
`ConfigReader.java` prioritizes System properties over file properties:
```bash
# Run tests on Chrome (default):
mvn clean test

# Override browser on the fly via CLI:
mvn test -Dbrowser=firefox

# Run headless in CI/CD environments:
mvn test -Dheadless=true
```

---

## 🏃 Test Execution & Reporting Commands

```bash
# 1. Execute Full Regression Suite:
mvn clean test

# 2. Execute Smoke Test Suite:
mvn test -DsuiteXmlFile=testng-smoke.xml

# 3. Execute Data-Driven Suite:
mvn test -Dtest=LoginDataDrivenTest

# 4. Execute Full End-to-End Checkout Test:
mvn test -Dtest=DownloadInvoiceTest

# 5. Generate and View Allure HTML Report:
mvn allure:report
# Open target/site/allure-maven-plugin/index.html in any browser!
```
