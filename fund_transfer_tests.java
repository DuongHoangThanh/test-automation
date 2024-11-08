import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class FundTransferTest {
    static WebDriver driver;
    static List<String> testResults = new ArrayList<>();

    // Hàm đăng nhập
    public static void login(String username, String password) {
        driver.get("https://www.demo.guru99.com/V4/index.php");
        driver.findElement(By.name("uid")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank"));
        driver.get("https://www.demo.guru99.com/V4/manager/FundTransInput.php");
    }

    // Hàm kiểm tra thông báo lỗi
    public static void checkErrorMessage(String elementId, String expectedMessage) {
        WebElement messageElement = driver.findElement(By.id(elementId));
        if (!messageElement.getText().equals(expectedMessage)) {
            throw new AssertionError("Expected '" + expectedMessage + "', but got '" + messageElement.getText() + "'");
        }
    }

    // Chạy từng trường hợp kiểm thử và lưu kết quả
    public static void runTest(Runnable testFunc) {
        try {
            testFunc.run();
            testResults.add(testFunc.getClass().getName() + ": PASS");
        } catch (AssertionError e) {
            testResults.add(testFunc.getClass().getName() + ": FAIL - " + e.getMessage());
        } catch (Exception e) {
            testResults.add(testFunc.getClass().getName() + ": ERROR - " + e.toString());
        }
    }

    // Các trường hợp kiểm thử
    public static void testVerifyPayersAccountNumberNotEmpty() {
        WebElement payersAccountField = driver.findElement(By.name("payersaccount"));
        payersAccountField.clear();
        payersAccountField.sendKeys(Keys.TAB);
        checkErrorMessage("message10", "Payers Account Number must not be blank");
    }

    public static void testSpecialCharactersNotAllowedInPayersAccount() {
        WebElement payersAccountField = driver.findElement(By.name("payersaccount"));
        payersAccountField.clear();
        payersAccountField.sendKeys("!@#$%^&*()");
        driver.findElement(By.name("payeeaccount")).sendKeys(Keys.TAB);
        checkErrorMessage("message10", "Special characters are not allowed");
    }

    public static void testAlphabeticCharactersNotAllowedInPayersAccount() {
        WebElement payersAccountField = driver.findElement(By.name("payersaccount"));
        payersAccountField.clear();
        payersAccountField.sendKeys("abcdef");
        driver.findElement(By.name("payeeaccount")).sendKeys(Keys.TAB);
        checkErrorMessage("message10", "Characters are not allowed");
    }

    public static void testVerifyAmountNotEmpty() {
        driver.findElement(By.name("payersaccount")).sendKeys("139424");
        driver.findElement(By.name("payeeaccount")).sendKeys("139425");
        WebElement amountField = driver.findElement(By.name("ammount"));
        amountField.clear();
        amountField.sendKeys(Keys.TAB);
        checkErrorMessage("message1", "Amount field must not be blank");
    }

    public static void testSuccessfulFundTransfer() {
        driver.findElement(By.name("payersaccount")).clear();
        driver.findElement(By.name("payersaccount")).sendKeys("139424");
        driver.findElement(By.name("payeeaccount")).clear();
        driver.findElement(By.name("payeeaccount")).sendKeys("139425");
        driver.findElement(By.name("ammount")).clear();
        driver.findElement(By.name("ammount")).sendKeys("1");
        driver.findElement(By.name("desc")).clear();
        driver.findElement(By.name("desc")).sendKeys("Transfer Test");
        driver.findElement(By.name("AccSubmit")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Fund Transfer Details')]")));
        if (successMessage == null) {
            throw new AssertionError("Expected success message for fund transfer not displayed.");
        }
    }

    public static void main(String[] args) {
        // Khởi tạo WebDriver
        System.setProperty("webdriver.chrome.driver", "path_to_chromedriver"); // Cập nhật đường dẫn WebDriver
        driver = new ChromeDriver();
        login("mngr595557", "sApEgad");

        // Chạy các trường hợp kiểm thử
        runTest(FundTransferTest::testVerifyPayersAccountNumberNotEmpty);
        runTest(FundTransferTest::testSpecialCharactersNotAllowedInPayersAccount);
        runTest(FundTransferTest::testAlphabeticCharactersNotAllowedInPayersAccount);
        runTest(FundTransferTest::testVerifyAmountNotEmpty);
        runTest(FundTransferTest::testSuccessfulFundTransfer);

        // In kết quả kiểm thử
        System.out.println("Test Results:");
        for (String result : testResults) {
            System.out.println(result);
        }

        // Đóng trình duyệt
        driver.quit();
    }
}
