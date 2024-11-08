import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Guru99DepositTest {
    static WebDriver driver;
    static List<String> testResults = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();

        // Truy cập trang đăng nhập
        driver.get("https://www.demo.guru99.com/V4/index.php");

        // Đăng nhập
        login("mngr595557", "sApEgad");

        // Chạy các trường hợp kiểm thử
        runTest(Guru99DepositTest::testAccountNumberBlank);
        runTest(Guru99DepositTest::testAccountNumberSpecialCharacters);
        runTest(Guru99DepositTest::testAccountNumberAlphabets);
        runTest(Guru99DepositTest::testAmountBlank);
        runTest(Guru99DepositTest::testAmountSpecialCharacters);
        runTest(Guru99DepositTest::testAmountAlphabets);
        runTest(Guru99DepositTest::testDescriptionBlank);
        runTest(Guru99DepositTest::testValidDepositSubmission);

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
        driver.get("https://www.demo.guru99.com/V4/manager/DepositInput.php");
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

    public static void testAmountBlank() {
        WebElement amountField = driver.findElement(By.name("ammount"));
        amountField.clear();
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Amount field must not be blank");
    }

    public static void testAmountSpecialCharacters() {
        WebElement amountField = driver.findElement(By.name("ammount"));
        amountField.clear();
        amountField.sendKeys("@#$%");
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Special characters are not allowed");
    }

    public static void testAmountAlphabets() {
        WebElement amountField = driver.findElement(By.name("ammount"));
        amountField.clear();
        amountField.sendKeys("ABC123");
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Characters are not allowed");
    }

    public static void testDescriptionBlank() {
        WebElement descriptionField = driver.findElement(By.name("desc"));
        descriptionField.clear();
        descriptionField.sendKeys(Keys.TAB);
        checkErrorMessage("message17", "Description can not be blank");
    }

    public static void testValidDepositSubmission() {
        WebElement accountField = driver.findElement(By.name("accountno"));
        WebElement amountField = driver.findElement(By.name("ammount"));
        WebElement descriptionField = driver.findElement(By.name("desc"));

        accountField.clear();
        amountField.clear();
        descriptionField.clear();

        accountField.sendKeys("139424");
        amountField.sendKeys("1");
        descriptionField.sendKeys("gg");
        driver.findElement(By.name("AccSubmit")).click();

        try {
            new WebDriverWait(driver, 2).until(
                ExpectedConditions.textToBePresentInElementLocated(
                    By.className("heading3"),
                    "Transaction details of Deposit for Account"
                )
            );
            System.out.println("Success: Deposit submission success message displayed.");
        } catch (Exception e) {
            throw new AssertionError("Deposit submission failed: Success message not displayed.");
        }
    }
}
