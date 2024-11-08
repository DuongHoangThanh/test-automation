import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Guru99Tests {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static List<String> testResults = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path_to_chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.demo.guru99.com/V4/index.php");

        // Đăng nhập
        login("mngr595557", "sApEgad");

        // Thực thi các trường hợp kiểm thử
        runTest(Guru99Tests::testCustomerNameBlank);
        runTest(Guru99Tests::testCustomerNameNumbers);
        runTest(Guru99Tests::testCustomerNameSpecialCharacters);
        runTest(Guru99Tests::testCustomerNameLeadingSpace);
        runTest(Guru99Tests::testDateOfBirthBlank);
        // (Thêm các trường hợp kiểm thử khác tương tự...)

        runTest(Guru99Tests::testSuccessfulSubmission);

        // In kết quả kiểm thử
        testResults.forEach(System.out::println);

        // Đóng trình duyệt
        driver.quit();
    }

    private static void login(String username, String password) {
        driver.findElement(By.name("uid")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"), "Welcome To Manager's Page of Guru99 Bank"
        ));
        driver.get("https://www.demo.guru99.com/V4/manager/addcustomerpage.php");
    }

    private static void runTest(Runnable test) {
        try {
            test.run();
            testResults.add(test.getClass().getSimpleName() + ": PASS");
        } catch (AssertionError | Exception e) {
            testResults.add(test.getClass().getSimpleName() + ": FAIL - " + e.getMessage());
        }
    }

    private static void checkErrorMessage(String elementId, String expectedMessage) {
        WebElement messageElement = driver.findElement(By.id(elementId));
        assert messageElement.getText().equals(expectedMessage) : 
                "Expected '" + expectedMessage + "', but got '" + messageElement.getText() + "'";
    }

    // Trường hợp kiểm thử cụ thể
    private static void testCustomerNameBlank() {
        WebElement customerNameField = driver.findElement(By.name("name"));
        customerNameField.clear();
        customerNameField.sendKeys(Keys.TAB);
        checkErrorMessage("message", "Customer name must not be blank");
    }

    private static void testCustomerNameNumbers() {
        WebElement customerNameField = driver.findElement(By.name("name"));
        customerNameField.clear();
        customerNameField.sendKeys("12345");
        customerNameField.sendKeys(Keys.TAB);
        checkErrorMessage("message", "Numbers are not allowed");
    }

    private static void testCustomerNameSpecialCharacters() {
        WebElement customerNameField = driver.findElement(By.name("name"));
        customerNameField.clear();
        customerNameField.sendKeys("!@#$$%");
        customerNameField.sendKeys(Keys.TAB);
        checkErrorMessage("message", "Special characters are not allowed");
    }

    private static void testCustomerNameLeadingSpace() {
        WebElement customerNameField = driver.findElement(By.name("name"));
        customerNameField.clear();
        customerNameField.sendKeys(" John Doe");
        customerNameField.sendKeys(Keys.TAB);
        checkErrorMessage("message", "First character can not have space");
    }

    private static void testDateOfBirthBlank() {
        WebElement dobField = driver.findElement(By.name("dob"));
        dobField.clear();
        dobField.sendKeys(Keys.TAB);
        checkErrorMessage("message24", "Date Field must not be blank");
    }

    // Các hàm kiểm thử khác như testAddressBlank, testCityBlank, v.v. có cấu trúc tương tự.

    private static String generateRandomEmail() {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        Random random = new Random();

        int usernameLength = 8 + random.nextInt(5);
        StringBuilder username = new StringBuilder();

        for (int i = 0; i < usernameLength; i++) {
            char randomChar = random.nextBoolean() ? letters.charAt(random.nextInt(letters.length()))
                                                   : digits.charAt(random.nextInt(digits.length()));
            username.append(randomChar);
        }

        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com"};
        String domain = domains[random.nextInt(domains.length)];
        return username.toString() + "@" + domain;
    }

    private static void testSuccessfulSubmission() {
        driver.findElement(By.name("name")).sendKeys("John Doe");
        driver.findElement(By.name("dob")).sendKeys("01/01/1990");
        driver.findElement(By.name("addr")).sendKeys("123 Main St");
        driver.findElement(By.name("city")).sendKeys("New York");
        driver.findElement(By.name("state")).sendKeys("NY");
        driver.findElement(By.name("pinno")).sendKeys("123456");
        driver.findElement(By.name("telephoneno")).sendKeys("1234567890");
        driver.findElement(By.name("emailid")).sendKeys(generateRandomEmail());
        driver.findElement(By.name("password")).sendKeys("123");

        driver.findElement(By.name("sub")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("heading3"), "Customer Registered Successfully!!!"
        ));
    }
}
