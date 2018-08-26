import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
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

        System.out.println("Screen orientation is : " + driver.getOrientation().toString());
        if (driver.getOrientation() != ScreenOrientation.PORTRAIT) {
            System.out.println("Rotating the screen");
            driver.rotate(ScreenOrientation.PORTRAIT);
        }
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

    @Test
    public void testSwipeArticle() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Unable to locate search input",
                5
        );

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Unable to find 'Appium' article in the search",
                15
        );

        waitForElementToBePresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Unable to find article title",
                15
        );

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Unable to find article end",
                20
        );

    }

    @Test
    public void testSaveArticleToMyList() {
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

        waitForElementToBePresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Unable to find article title",
                15
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Unable to find 'More options' link",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Unable to select 'Add to reading list' context menu option",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Unable to locate 'GOT IT' tip overlay",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Unable to find input to set articles folder name",
                5
        );

        String folderName = "Learning programming";

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folderName,
                "Unable to put text into articles folder input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Unable to press OK button",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Unable to close article - missing X link",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Unable to locate 'My lists' navigation button",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='" + folderName + "']"),
                "Unable to find created folder",
                5
        );

        swipeElementLeft(
                By.xpath("//*[@text='Learning programming']"),
                "Unable to find saved article"
        );

    }

    @Test
    public void testAmountOfNotEmptySearch() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        String searchLine = "Linkin Park discography";
        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                searchLine,
                "Unable to locate search input",
                5
        );

        String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        waitForElementToBePresent(
                By.xpath(searchResultLocator),
                "Unable to find search results for request " + searchLine,
                15
        );

        int amountOfSearchResults = getAmountOfElements(
                By.xpath(searchResultLocator)
        );

        Assert.assertTrue("Too few results were found!",
                amountOfSearchResults > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        String searchLine = "zxcvfgretw";
        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                searchLine,
                "Unable to locate search input",
                5
        );

        String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";

        String emptyResultLabel = "//*[@resource-id='org.wikipedia:id/search_empty_text'][@text='No results found']";
        waitForElementToBePresent(
                By.xpath(emptyResultLabel),
                "Unable to find search results for request " + searchLine,
                15
        );

        assertElementNotPresent(
                By.xpath(searchResultLocator),
                "Non zero number of search results were found by request " + searchLine
        );
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        String searchLine = "Java";
        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                searchLine,
                "Unable to locate search input",
                5
        );

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Unable to find 'Object-oriented programming language' topic searching by '" + searchLine + "'",
                15
        );

        String titleBeforeRotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Unable to find article title",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String titleAfterRotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Unable to find article title",
                15
        );

        Assert.assertEquals("Article title has been changed after screen rotation",
                titleBeforeRotation,
                titleAfterRotation);

        driver.rotate(ScreenOrientation.PORTRAIT);

        String titleAfterSecondRotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Unable to find article title",
                15
        );

        Assert.assertEquals("Article title has been changed after second screen rotation",
                titleBeforeRotation,
                titleAfterSecondRotation);
    }

    @Test
    public void testCheckSearchArticleInBackground() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        String searchLine = "Java";
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchLine,
                "Unable to locate search input",
                5
        );

        waitForElementToBePresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Unable to find 'Object-oriented programming language' topic searching by '" + searchLine + "'",
                15
        );

        driver.runAppInBackground(2);

        waitForElementToBePresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Unable to find article after returning from background",
                15
        );
    }

    @Test
    public void testSaveTwoArticles() {

        String searchLine = "Java";

        //Add first article to the list
        String articleIdentifier = "Object-oriented programming language";
        String articleTitleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleIdentifier + "']";

        driver.resetApp();

        search(searchLine);

        openArticle(articleTitleXpath, searchLine);

        String folderName = "Learning programming";
        addArticleToReadingList(folderName);

        //Add second article
        articleIdentifier = "Java (software platform)";
        String secondArticleTitleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleIdentifier + "']";

        search(searchLine);

        openArticle(secondArticleTitleXpath, searchLine);

        //put 2nd article to 'Learning programming' list
        addArticleToReadingList(folderName);

        //Open list with two articles
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Unable to locate 'My lists' navigation button",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']"),
                "Unable to find created folder",
                5
        );

        //assert that list contains two articles
        Assert.assertEquals(folderName + " list contains wrong number of articles",
                2,
                getAmountOfElements(By.id("org.wikipedia:id/page_list_item_container")));

        String articleToDeleteXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + articleIdentifier + "']";

        swipeElementLeft(
                By.xpath(articleToDeleteXpath),
                "Unable to find saved article"
        );

        //assert that one article is still in the list
        Assert.assertEquals(folderName + " list contains wrong number of articles",
                1,
                getAmountOfElements(By.id("org.wikipedia:id/page_list_item_container")));

        //assert article title
        String expectedArticleTitle = "Java (programming language)";
        waitForElementAndClick(
                By.xpath("//*[@text='" + expectedArticleTitle + "']"),
                "Unable to find first article in the list after second one was deleted",
                15
        );
        System.out.println("click on " + expectedArticleTitle);

        String actualArticleTitle = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Unable to find article title",
                15
        );

        Assert.assertEquals("Actual article title differs from expected one",
                expectedArticleTitle,
                actualArticleTitle
        );
    }

    @Test
    public void testAssertArticleTitlePresence() {
        String keyWord = "Three Days Grace";
        search(keyWord);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='Canadian band']"),
                "Unable to locate article in the list searching by '" + keyWord + "'",
                5
        );

        String articleTitleXpath = "//*[@resource-id='org.wikipedia:id/view_page_title_text']";
        assertElementPresent(By.xpath(articleTitleXpath), ": unable to locate article title");
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
        element.clear();
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

    private void enterSearchKeyWord(String keyWord) {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search…')]"),
                keyWord,
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
    }

    private void search(String keyWord) {
        enterSearchKeyWord(keyWord);

        //get search results
        String listOfSearchResultsXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";
        waitForListOfElementsToBePresent(
                By.xpath(listOfSearchResultsXpath),
                "Unable to locate search results list",
                5
        );

        //verify that search results list is not empty
        Assert.assertTrue("List is empty",
                getAmountOfElements(By.xpath(listOfSearchResultsXpath)) > 0);
    }

    private List<WebElement> executeSearch(String keyWord) {
        enterSearchKeyWord(keyWord);

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

    private void openArticle(String articleTitleXpath, String searchLine) {
        String titleText = waitForElementAndGetAttribute(
                By.xpath(articleTitleXpath),
                "text",
                "",
                5
        );

        waitForElementAndClick(
                By.xpath(articleTitleXpath),
                "Unable to find '" + titleText + "' topic searching by '" + searchLine + "'",
                15
        );

        waitForElementToBePresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Unable to find article title",
                20
        );
    }

    private void addArticleToReadingList(String folderName) {
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Unable to find 'More options' link",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/title'][@text='Add to reading list']"),
                "Unable to select 'Add to reading list' context menu option",
                5
        );

        //user has no list with given name, create new one
        if (isElementPresent(By.id("org.wikipedia:id/onboarding_button"))) {
            waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Unable to locate 'GOT IT' tip overlay",
                5
            );

            waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Unable to find input to set articles folder name",
                5
            );

            waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folderName,
                "Unable to put text into articles folder input",
                5
            );

            waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Unable to press OK button",
                5
            );
        }

        //user has already created the folder, just select it
        if(isElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']"))) {
            waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']"),
                "Unable to add second article to " + folderName + " list",
                5
            );
        }

        waitForElementAndClick(
                By.xpath("//*[@content-desc='Navigate up']"),
                "Unable to close article - missing X link",
                5
        );
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width/2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        action
                .press(x, startY)
                .waitAction(timeOfSwipe)
                .moveTo(x, endY)
                .release()
                .perform();
    }

    protected void swipeUpQuck() {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
        int alreadySwiped = 0;
        while(driver.findElements(by).size() == 0) {
            if(alreadySwiped > maxSwipes) {
                waitForElementToBePresent(by, "Unable to find elent by swiping up. \n" + errorMessage);
                return;
            }
            swipeUpQuck();
            ++alreadySwiped;
        }
    }

    protected void  swipeElementLeft(By by, String errorMessage) {
        WebElement element = waitForElementToBePresent(by, errorMessage, 10);

        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY)/2;

        TouchAction action = new TouchAction(driver);
        action
                .press(leftX, middleY)
                .waitAction(300)
                .moveTo(rightX, middleY)
                .release()
                .perform();
    }

    private int getAmountOfElements(By by) {
        return driver.findElements(by).size();
    }

    private void assertElementNotPresent(By by, String errorMessage) {
        if(getAmountOfElements(by) > 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    private void assertElementPresent(By by, String errorMessage) {
        if(getAmountOfElements(by) == 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String errorMessage, long timeoutInSeconds) {
        return waitForElementToBePresent(by, errorMessage, timeoutInSeconds)
                .getAttribute(attribute);
    }

}
