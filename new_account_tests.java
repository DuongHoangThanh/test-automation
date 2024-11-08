import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountTests {
    private static WebDriver driver;
    private static List<String> testResults = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        // Khởi tạo WebDriver và mở trang đăng nhập
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.demo.guru99.com/V4/index.php");
        login("mngr595557", "sApEgad");
        driver.get("https://www.demo.guru99.com/V4/manager/addAccount.php");
    }

    @AfterAll
    public static void tearDown() {
        // In kết quả và đóng trình duyệt
        System.out.println("\nTest Results:");
        for (String result : testResults) {
            System.out.println(result);
        }
        driver.quit();
    }

    private static void login(String username, String password) {
        driver.findElement(By.name("uid")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.textToBePresentInElementLocated(By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank")
        );
    }

    private void checkErrorMessage(String elementId, String expectedMessage) {
        String actualMessage = driver.findElement(By.id(elementId)).getText();
        Assertions.assertEquals(expectedMessage, actualMessage, 
            "Expected '" + expectedMessage + "', but got '" + actualMessage + "'");
    }

    @Test
    @Order(1)
    public void testCustomerIdBlank() {
        driver.findElement(By.name("cusid")).clear();
        driver.findElement(By.name("cusid")).sendKeys(Keys.TAB);
        checkErrorMessage("message14", "Customer ID is required");
        testResults.add("testCustomerIdBlank: PASS");
    }

    @Test
    @Order(2)
    public void testCustomerIdSpecialCharacters() {
        WebElement customerIdField = driver.findElement(By.name("cusid"));
        customerIdField.clear();
        customerIdField.sendKeys("@#$%");
        customerIdField.sendKeys(Keys.TAB);
        checkErrorMessage("message14", "Special characters are not allowed");
        testResults.add("testCustomerIdSpecialCharacters: PASS");
    }

    @Test
    @Order(3)
    public void testCustomerIdAlphabets() {
        WebElement customerIdField = driver.findElement(By.name("cusid"));
        customerIdField.clear();
        customerIdField.sendKeys("ABC123");
        customerIdField.sendKeys(Keys.TAB);
        checkErrorMessage("message14", "Characters are not allowed");
        testResults.add("testCustomerIdAlphabets: PASS");
    }

    @Test
    @Order(4)
    public void testInitialDepositBlank() {
        WebElement depositField = driver.findElement(By.name("inideposit"));
        depositField.clear();
        depositField.sendKeys(Keys.TAB);
        checkErrorMessage("message19", "Initial Deposit must not be blank");
        testResults.add("testInitialDepositBlank: PASS");
    }

    @Test
    @Order(5)
    public void testInitialDepositSpecialCharacters() {
        WebElement depositField = driver.findElement(By.name("inideposit"));
        depositField.clear();
        depositField.sendKeys("@#$%");
        depositField.sendKeys(Keys.TAB);
        checkErrorMessage("message19", "Special characters are not allowed");
        testResults.add("testInitialDepositSpecialCharacters: PASS");
    }

    @Test
    @Order(6)
    public void testInitialDepositAlphabets() {
        WebElement depositField = driver.findElement(By.name("inideposit"));
        depositField.clear();
        depositField.sendKeys("DepositABC");
        depositField.sendKeys(Keys.TAB);
        checkErrorMessage("message19", "Characters are not allowed");
        testResults.add("testInitialDepositAlphabets: PASS");
    }

    @Test
    @Order(7)
    public void testInitialDepositBelowMinimum() {
        driver.get("https://www.demo.guru99.com/V4/manager/addAccount.php");
        driver.findElement(By.name("cusid")).sendKeys("47674");
        WebElement depositField = driver.findElement(By.name("inideposit"));
        depositField.clear();
        depositField.sendKeys("5");
        driver.findElement(By.name("button2")).click();

        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.alertIsPresent());
        Assertions.assertEquals("Initial deposit must be 500 or more", alert.getText());
        alert.accept();
        testResults.add("testInitialDepositBelowMinimum: PASS");
    }

    @Test
    @Order(8)
    public void testValidAccountCreation() {
        driver.findElement(By.name("cusid")).sendKeys("47674");
        WebElement depositField = driver.findElement(By.name("inideposit"));
        depositField.clear();
        depositField.sendKeys("5000");
        driver.findElement(By.name("button2")).click();

        WebElement successMessage = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.className("heading3"))
        );
        Assertions.assertTrue(successMessage.getText().contains("Account Generated Successfully!!!"));
        testResults.add("testValidAccountCreation: PASS");
    }
}
