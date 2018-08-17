import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/Christina/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Unable to locate Search Wikipedia input",
                5
        );
        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Unable to locate search input",
                5
        );
        waitForElementToBePresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Unable to find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Unable to locate search input",
                5
        );

        waitForElementAndClear(By.id("org.wikipedia:id/search_src_text"),
                "Unable to clear search field",
                5
        );

        //emulator clicks on 'x' button too fast after clearing the field: click while element is present
        while(isElementPresent(By.id("org.wikipedia:id/search_close_btn"))) {
            waitForElementAndClick(By.id("org.wikipedia:id/search_close_btn"),
                    "Unable to locate 'X' button to cancel search",
                    5
            );
        }

        waitForElementNotPresent(By.id("org.wikipedia:id/search_close_btn"),
                "'X' button is still present on the page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Unable to locate search input",
                5
        );

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Unable to find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );

        WebElement articleTitleElement = waitForElementToBePresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Unable to find article title",
                15
        );

        Assert.assertEquals("Unexpected title is displayed",
                "Java (programming language)",
                articleTitleElement.getAttribute("text")
        );
    }

    private WebElement waitForElementToBePresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementToBePresent(By by, String errorMessage) {
        return waitForElementToBePresent(by, errorMessage, 5);
    }

    private boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    private boolean isElementPresent(By by) {
        return driver.findElements(by).size() == 1;
    }
}
