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
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testSearchResultCancellation() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Unable to locate search input",
                5
        );

        driver.hideKeyboard();

        //wait for search results
        waitForElementToBePresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"),
                "Unable to locate search results",
                15
        );

        //get search results to list
        String listOfSearchResultsXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";
        List<WebElement> listOfSearchResults = waitForListOfElementsToBePresent(
                By.xpath(listOfSearchResultsXpath),
                "Unable to locate search results list",
                10
        );

        //verify that search results list is not empty
        Assert.assertTrue("List is empty",
                getSearchResultsCount(listOfSearchResults) > 0);

        //clear search
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Unable to locate X clear search link",
                15
        );

        driver.hideKeyboard();

        //check that Search... element contains default text now
        validateSearchElementText();

        Assert.assertFalse("Search results are still present on page",
                isElementPresent(By.xpath(listOfSearchResultsXpath))
        );

        //verify that empty search result is displayed on page now
        Assert.assertTrue("Unable to locate empty search result list element",
                isElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/search_empty_container']"))
        );
    }

    @Test
    public void testSearchResultsValidation() {
        String word = "Java";

        //fill the list with search results;
        List<WebElement> listOfSearchResults = executeSearch(word);

        //check that each search result contains given word
        for(WebElement element : listOfSearchResults) {
            Assert.assertTrue("Search line does not contain appropriate word " + word,
                    element.getText().toLowerCase().contains(word.toLowerCase())
            );
        }
    }

    private int getSearchResultsCount(List<WebElement> list) {
        return list.size();
    }

    private WebElement waitForElementToBePresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private List<WebElement> waitForListOfElementsToBePresent (By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return new ArrayList<>(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)));
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

    /*
    Написать функцию, которая проверяет наличие текста “Search…” в строке поиска перед вводом текста
     и помечает тест упавшим, если такого текста нет.
     */
    private void validateSearchElementText() {
        WebElement searchElement = waitForElementToBePresent(By.id("org.wikipedia:id/search_src_text"),
                "Unable to locate 'Search...' input",
                5);
        Assert.assertEquals("Unexpected 'Search...' field default text is displayed",
                "Search…",
                searchElement.getAttribute("text")
        );
    }

    //more general methods to compare any element text attribute value with given expected text
    //and mark test as 'failed' if text comparison fails
    private void validateElementText(WebElement element, String expectedText) {
        Assert.assertEquals("Actual element text does not match expected value",
                expectedText,
                element.getAttribute("text"));
    }

    private void waitForElementAndValidateText(By by, String errorMessage, long timeoutInSeconds, String expectedText) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        validateElementText(element, expectedText);
    }

    private List<WebElement> executeSearch(String searchWord) {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search…')]"),
                searchWord,
                "Unable to locate search input",
                5
        );

        driver.hideKeyboard();

        //wait for search results
        waitForElementToBePresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"),
                "Unable to locate search results",
                15
        );

        //get search results to list
        String listOfSearchResultsXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";
        List<WebElement> listOfSearchResults = waitForListOfElementsToBePresent(
                By.xpath(listOfSearchResultsXpath),
                "Unable to locate search results list",
                5
        );

        //verify that search results list is not empty
        Assert.assertTrue("List is empty",
                getSearchResultsCount(listOfSearchResults) > 0);

        return listOfSearchResults;
    }

}
