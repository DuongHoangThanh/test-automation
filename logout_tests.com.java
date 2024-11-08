import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LogoutTest {
    static List<String> testResults = new ArrayList<>();

    // Hàm khởi tạo WebDriver
    public static WebDriver initDriver() {
        return new ChromeDriver();
    }

    // Hàm đóng WebDriver
    public static void closeDriver(WebDriver driver) {
        driver.quit();
    }

    // Hàm chạy từng trường hợp kiểm thử và lưu kết quả
    public static void runTest(TestCase testFunc) {
        WebDriver driver = initDriver();  // Khởi tạo WebDriver cho từng kiểm thử
        try {
            testFunc.run(driver);
            testResults.add(testFunc.getClass().getName() + ": PASS");
        } catch (AssertionError e) {
            testResults.add(testFunc.getClass().getName() + ": FAIL - " + e.getMessage());
        } catch (Exception e) {
            testResults.add(testFunc.getClass().getName() + ": ERROR - " + e.toString());
        } finally {
            closeDriver(driver);  // Đảm bảo đóng driver sau mỗi kiểm thử
        }
    }

    // Kiểm thử: Đăng xuất với phiên hợp lệ
    public static void logoutWithValidSession(WebDriver driver) {
        driver.get("https://www.demo.guru99.com/V4/index.php");
        
        // Đăng nhập
        WebElement usernameField = driver.findElement(By.name("uid"));
        usernameField.sendKeys("mngr595557");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("sApEgad");
        driver.findElement(By.name("btnLogin")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank"
        ));

        // Đăng xuất
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out")));
        logoutLink.click();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        if (!alertText.contains("Successfully Logged Out")) {
            throw new AssertionError("Expected 'Successfully Logged Out', but got: '" + alertText + "'");
        }
        driver.switchTo().alert().accept(); // Chấp nhận cảnh báo để đóng
    }

    // Kiểm thử: Đăng xuất khi không có phiên hợp lệ
    public static void logoutWithInvalidSession(WebDriver driver) {
        driver.get("https://www.demo.guru99.com/V4/manager/Managerhomepage.php");
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out")));
        logoutLink.click();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        
        if (alertText.contains("Logged Out")) {
            throw new AssertionError("Logout was unexpectedly successful, should not have logged out.");
        } else {
            throw new AssertionError("Unexpected alert text: " + alertText);
        }
    }

    public static void main(String[] args) {
        // Chạy các trường hợp kiểm thử
        runTest(LogoutTest::logoutWithValidSession);
        runTest(LogoutTest::logoutWithInvalidSession);

        // In kết quả kiểm thử
        System.out.println("\nTest Results:");
        for (String result : testResults) {
            System.out.println(result);
        }
    }

    // Functional interface để truyền kiểm thử vào hàm runTest
    @FunctionalInterface
    interface TestCase {
        void run(WebDriver driver) throws Exception;
    }
}
