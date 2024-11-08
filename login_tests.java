import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LoginTest {
    static WebDriver driver;
    static List<String> testResults = new ArrayList<>();

    // Hàm kiểm tra thông báo lỗi
    public static void checkErrorMessage(String elementId, String expectedMessage) {
        WebElement messageElement = driver.findElement(By.id(elementId));
        if (!messageElement.getText().equals(expectedMessage)) {
            throw new AssertionError("Expected '" + expectedMessage + "', but got '" + messageElement.getText() + "'");
        }
    }

    // Hàm chạy từng trường hợp kiểm thử và lưu kết quả
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

    // Trường hợp kiểm thử: kiểm tra trường User ID không để trống
    public static void testValidateUsernameField() {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.clear();
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys("pass123");
        usernameField.sendKeys(Keys.TAB);
        checkErrorMessage("message23", "User-ID must not be blank");
    }

    // Trường hợp kiểm thử: kiểm tra trường Password không để trống
    public static void testValidatePasswordField() {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.clear();
        usernameField.sendKeys("user123");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(Keys.TAB);
        checkErrorMessage("message18", "Password must not be blank");
    }

    // Trường hợp kiểm thử: kiểm tra cả hai trường đều để trống
    public static void testLeaveBothFieldsBlank() {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.clear();
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(Keys.TAB);
        checkErrorMessage("message23", "User-ID must not be blank");
        checkErrorMessage("message18", "Password must not be blank");
    }

    // Trường hợp kiểm thử: đăng nhập với cả hai trường để trống
    public static void testLoginWithBothFieldsBlank() {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.clear();
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        driver.findElement(By.name("btnLogin")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        if (!alertText.equals("User or Password is not valid")) {
            throw new AssertionError("Expected alert message 'User or Password is not valid', but got '" + alertText + "'");
        }
        driver.switchTo().alert().accept();
    }

    // Trường hợp kiểm thử: đăng nhập thành công với thông tin hợp lệ
    public static void testSuccessfulLoginWithValidCredentials() {
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.clear();
        usernameField.sendKeys("mngr595557");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys("sApEgad");
        driver.findElement(By.name("btnLogin")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank"
        ));
    }

    public static void main(String[] args) {
        // Khởi tạo WebDriver
        System.setProperty("webdriver.chrome.driver", "path_to_chromedriver"); // Cập nhật đường dẫn WebDriver
        driver = new ChromeDriver();
        driver.get("https://www.demo.guru99.com/V4/index.php");

        // Chạy các trường hợp kiểm thử
        runTest(LoginTest::testValidateUsernameField);
        runTest(LoginTest::testValidatePasswordField);
        runTest(LoginTest::testLeaveBothFieldsBlank);
        runTest(LoginTest::testLoginWithBothFieldsBlank);
        runTest(LoginTest::testSuccessfulLoginWithValidCredentials);

        // In kết quả kiểm thử
        System.out.println("Test Results:");
        for (String result : testResults) {
            System.out.println(result);
        }

        // Đóng trình duyệt
        driver.quit();
    }
}
