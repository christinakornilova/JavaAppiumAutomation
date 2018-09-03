package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public int getSearchResultsCount(List<WebElement> list) {
        return list.size();
    }

    public WebElement waitForElementToBePresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> waitForListOfElementsToBePresent (By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return new ArrayList<>(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)));
    }

    public WebElement waitForElementToBePresent(By by, String errorMessage) {
        return waitForElementToBePresent(by, errorMessage, 5);
    }

    public boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        element.clear();
        return element;
    }

    public boolean isElementPresent(By by) {
        return driver.findElements(by).size() == 1;
    }

    /*
    Написать функцию, которая проверяет наличие текста “Search…” в строке поиска перед вводом текста
     и помечает тест упавшим, если такого текста нет.
     */
    public void validateSearchElementText() {
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
    public void validateElementText(WebElement element, String expectedText) {
        Assert.assertEquals("Actual element text does not match expected value",
                expectedText,
                element.getAttribute("text"));
    }

    public void waitForElementAndValidateText(By by, String errorMessage, long timeoutInSeconds, String expectedText) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
        validateElementText(element, expectedText);
    }

    public void enterSearchKeyWord(String keyWord) {
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

    public void search(String keyWord) {
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

    public List<WebElement> executeSearch(String keyWord) {
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

    public void openArticle(String articleTitleXpath, String searchLine) {
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

    public void addArticleToReadingList(String folderName) {
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

    public void swipeUp(int timeOfSwipe) {
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

    public void swipeUpQuick() {
        swipeUp(200);
    }

    public void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
        int alreadySwiped = 0;
        while(driver.findElements(by).size() == 0) {
            if(alreadySwiped > maxSwipes) {
                waitForElementToBePresent(by, "Unable to find elent by swiping up. \n" + errorMessage);
                return;
            }
            swipeUpQuick();
            ++alreadySwiped;
        }
    }

    public void  swipeElementLeft(By by, String errorMessage) {
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

    public int getAmountOfElements(By by) {
        return driver.findElements(by).size();
    }

    public void assertElementNotPresent(By by, String errorMessage) {
        if(getAmountOfElements(by) > 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    public void assertElementPresent(By by, String errorMessage) {
        if(getAmountOfElements(by) == 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    public String waitForElementAndGetAttribute(By by, String attribute, String errorMessage, long timeoutInSeconds) {
        return waitForElementToBePresent(by, errorMessage, timeoutInSeconds)
                .getAttribute(attribute);
    }

}
