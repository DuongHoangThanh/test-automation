import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class Guru99BankTests {
    static WebDriver driver;
    static List<String> testResults = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path_to_chromedriver"); 
        driver = new ChromeDriver();
        driver.get("https://www.demo.guru99.com/V4/index.php");

        login("mngr595557", "sApEgad");
        runTests();

        for (String result : testResults) {
            System.out.println(result);
        }
        driver.quit();
    }

    // Login function
    public static void login(String username, String password) {
        driver.findElement(By.name("uid")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank"
        ));

        driver.get("https://www.demo.guru99.com/V4/manager/DepositInput.php");
    }

    // Utility function for checking error message
    public static void checkErrorMessage(String elementId, String expectedMessage) {
        WebElement messageElement = driver.findElement(By.id(elementId));
        String actualMessage = messageElement.getText();
        if (!actualMessage.equals(expectedMessage)) {
            throw new AssertionError("Expected: " + expectedMessage + ", but got: " + actualMessage);
        }
    }

    // Function to run each test case
    public static void runTest(Runnable testFunc, String testName) {
        try {
            testFunc.run();
            testResults.add(testName + ": PASS");
        } catch (AssertionError e) {
            testResults.add(testName + ": FAIL - " + e.getMessage());
        } catch (Exception e) {
            testResults.add(testName + ": ERROR - " + e.getMessage());
        }
    }

    // Withdrawal Test Cases
    public static void testVerifyAccountNumberFieldNotBlank() {
        driver.findElement(By.name("accountno")).clear();
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Account Number must not be blank");
    }

    public static void testSpecialCharactersNotAllowedInAccountNumber() {
        driver.findElement(By.name("accountno")).clear();
        driver.findElement(By.name("accountno")).sendKeys("!@#$%^&*()");
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Special characters are not allowed");
    }

    public static void testAlphabeticCharactersNotAllowedInAccountNumber() {
        driver.findElement(By.name("accountno")).clear();
        driver.findElement(By.name("accountno")).sendKeys("abcdef");
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message2", "Characters are not allowed");
    }

    public static void testVerifyAmountFieldNotBlank() {
        driver.findElement(By.name("accountno")).sendKeys("123456");
        driver.findElement(By.name("ammount")).clear();
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Amount field must not be blank");
    }

    public static void testSpecialCharactersNotAllowedInAmount() {
        driver.findElement(By.name("accountno")).sendKeys("123456");
        driver.findElement(By.name("ammount")).clear();
        driver.findElement(By.name("ammount")).sendKeys("!@#$%");
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Special characters are not allowed");
    }

    public static void testAlphabeticCharactersNotAllowedInAmount() {
        driver.findElement(By.name("accountno")).sendKeys("123456");
        driver.findElement(By.name("ammount")).clear();
        driver.findElement(By.name("ammount")).sendKeys("abc");
        driver.findElement(By.name("ammount")).sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Characters are not allowed");
    }

    public static void testVerifyDescriptionFieldNotBlank() {
        driver.findElement(By.name("accountno")).sendKeys("123456");
        driver.findElement(By.name("ammount")).sendKeys("100");
        driver.findElement(By.name("desc")).clear();
        driver.findElement(By.name("desc")).sendKeys(Keys.TAB);
        checkErrorMessage("message17", "Description can not be blank");
    }

    public static void testSuccessfulDepositWithValidValues() {
        driver.findElement(By.name("accountno")).clear();
        driver.findElement(By.name("accountno")).sendKeys("139424");

        driver.findElement(By.name("ammount")).clear();
        driver.findElement(By.name("ammount")).sendKeys("1");

        driver.findElement(By.name("desc")).clear();
        driver.findElement(By.name("desc")).sendKeys("gg");

        driver.findElement(By.name("AccSubmit")).click();

        WebDriverWait wait = new WebDriverWait(driver, 2);
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//p[contains(text(), 'Transaction successful')]")
        ));
        if (successMessage == null) {
            throw new AssertionError("Deposit was not successful.");
        }
    }

    // Run all test cases
    public static void runTests() {
        runTest(Guru99BankTests::testVerifyAccountNumberFieldNotBlank, "Verify Account Number Field Not Blank");
        runTest(Guru99BankTests::testSpecialCharactersNotAllowedInAccountNumber, "Special Characters Not Allowed in Account Number");
        runTest(Guru99BankTests::testAlphabeticCharactersNotAllowedInAccountNumber, "Alphabetic Characters Not Allowed in Account Number");
        runTest(Guru99BankTests::testVerifyAmountFieldNotBlank, "Verify Amount Field Not Blank");
        runTest(Guru99BankTests::testSpecialCharactersNotAllowedInAmount, "Special Characters Not Allowed in Amount");
        runTest(Guru99BankTests::testAlphabeticCharactersNotAllowedInAmount, "Alphabetic Characters Not Allowed in Amount");
        runTest(Guru99BankTests::testVerifyDescriptionFieldNotBlank, "Verify Description Field Not Blank");
        runTest(Guru99BankTests::testSuccessfulDepositWithValidValues, "Successful Deposit with Valid Values");
    }
}
