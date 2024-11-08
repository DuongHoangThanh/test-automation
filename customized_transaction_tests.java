import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class SeleniumTest {
    static WebDriver driver;
    static List<String> testResults = new ArrayList<>();

    public static void main(String[] args) {
        // Khởi tạo WebDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();

        // Truy cập trang đăng nhập
        driver.get("https://www.demo.guru99.com/V4/index.php");

        // Đăng nhập
        login("mngr595557", "sApEgad");

        // Chạy các trường hợp kiểm thử
        runTest(SeleniumTest::testAccountNumberBlank);
        runTest(SeleniumTest::testAccountNumberSpecialCharacters);
        runTest(SeleniumTest::testAccountNumberAlphabets);
        runTest(SeleniumTest::testFromDateBlank);
        runTest(SeleniumTest::testToDateBlank);
        runTest(SeleniumTest::testMinimumTransactionValueSpecialCharacters);
        runTest(SeleniumTest::testMinimumTransactionValueAlphabets);
        runTest(SeleniumTest::testNumberOfTransactionsSpecialCharacters);
        runTest(SeleniumTest::testNumberOfTransactionsAlphabets);
        runTest(SeleniumTest::testValidateDateOrder);
        runTest(SeleniumTest::testValidSubmission);

        // In kết quả kiểm thử
        System.out.println("\nTest Results:");
        for (String result : testResults) {
            System.out.println(result);
        }

        // Đóng trình duyệt
        driver.quit();
    }

    public static void login(String username, String password) {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.sendKeys(username);
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();

        new WebDriverWait(driver, 10).until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"),
                "Welcome To Manager's Page of Guru99 Bank"
            )
        );
        driver.get("https://www.demo.guru99.com/V4/manager/CustomisedStatementInput.php");
    }

    public static void runTest(Runnable testFunc) {
        try {
            testFunc.run();
            testResults.add(testFunc.getClass().getSimpleName() + ": PASS");
        } catch (AssertionError | Exception e) {
            testResults.add(testFunc.getClass().getSimpleName() + ": FAIL - " + e.getMessage());
        }
    }

    public static void checkErrorMessage(String elementId, String expectedMessage) {
        WebElement messageElement = driver.findElement(By.id(elementId));
        if (!messageElement.getText().equals(expectedMessage)) {
            throw new AssertionError("Expected '" + expectedMessage + "', but got '" + messageElement.getText() + "'");
        }
    }

    public static void testAccountNumberBlank() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        accountField.clear();
        accountField.sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Account Number must not be blank");
    }

    public static void testAccountNumberSpecialCharacters() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        accountField.clear();
        accountField.sendKeys("@#$%");
        accountField.sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Special characters are not allowed");
    }

    public static void testAccountNumberAlphabets() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        accountField.clear();
        accountField.sendKeys("ABC123");
        accountField.sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Characters are not allowed");
    }

    public static void testFromDateBlank() {
        WebElement fromDateField = driver.findElement(By.name("fdate"));
        fromDateField.clear();
        fromDateField.sendKeys(Keys.TAB);
        checkErrorMessage("message26", "From Date Field must not be blank");
    }

    public static void testToDateBlank() {
        WebElement toDateField = driver.findElement(By.name("tdate"));
        toDateField.clear();
        toDateField.sendKeys(Keys.TAB);
        checkErrorMessage("message27", "To Date Field must not be blank");
    }

    public static void testMinimumTransactionValueSpecialCharacters() {
        WebElement amountField = driver.findElement(By.name("amountlowerlimit"));
        amountField.clear();
        amountField.sendKeys("@#$%");
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message12", "Special characters are not allowed");
    }

    public static void testMinimumTransactionValueAlphabets() {
        WebElement amountField = driver.findElement(By.name("amountlowerlimit"));
        amountField.clear();
        amountField.sendKeys("ABC123");
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message12", "Characters are not allowed");
    }

    public static void testNumberOfTransactionsSpecialCharacters() {
        WebElement transactionField = driver.findElement(By.name("numtransaction"));
        transactionField.clear();
        transactionField.sendKeys("@#$%");
        transactionField.sendKeys(Keys.TAB);
        checkErrorMessage("message13", "Special characters are not allowed");
    }

    public static void testNumberOfTransactionsAlphabets() {
        WebElement transactionField = driver.findElement(By.name("numtransaction"));
        transactionField.clear();
        transactionField.sendKeys("ABC123");
        transactionField.sendKeys(Keys.TAB);
        checkErrorMessage("message13", "Characters are not allowed");
    }

    public static void testValidateDateOrder() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        WebElement fromDateField = driver.findElement(By.name("fdate"));
        WebElement toDateField = driver.findElement(By.name("tdate"));

        accountField.clear();
        accountField.sendKeys("139424");

        fromDateField.clear();
        fromDateField.sendKeys("2024-10-31");

        toDateField.clear();
        toDateField.sendKeys("2024-10-30");
        toDateField.sendKeys(Keys.TAB);

        new WebDriverWait(driver, 1).until(ExpectedConditions.alertIsPresent()).accept();
    }

    public static void testValidSubmission() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        WebElement fromDateField = driver.findElement(By.name("fdate"));
        WebElement toDateField = driver.findElement(By.name("tdate"));

        accountField.clear();
        accountField.sendKeys("139424");

        fromDateField.clear();
        fromDateField.sendKeys("2024-10-30");

        toDateField.clear();
        toDateField.sendKeys("2024-10-30");

        driver.findElement(By.name("AccSubmit")).click();

        String expectedUrl = "https://www.demo.guru99.com/V4/manager/CustomisedStatement.php";
        if (!driver.getCurrentUrl().equals(expectedUrl)) {
            throw new AssertionError("Expected URL: " + expectedUrl + ", Actual URL: " + driver.getCurrentUrl());
        }
    }
}
