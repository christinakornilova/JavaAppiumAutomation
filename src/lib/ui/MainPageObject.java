package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import lib.Platform;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public int getSearchResultsCount(List<WebElement> list) {
        return list.size();
    }

    public WebElement waitForElementToBePresent(String locator, String errorMessage, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> waitForListOfElementsToBePresent (String locator, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        By by = getLocatorByString(locator);
        return new ArrayList<>(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)));
    }

    public WebElement waitForElementToBePresent(String locator, String errorMessage) {
        return waitForElementToBePresent(locator, errorMessage, 5);
    }

    public boolean waitForElementNotPresent(String locator, String errorMessage, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndClick(String locator, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(locator, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(locator, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public WebElement waitForElementAndClear(String locator, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementToBePresent(locator, errorMessage, timeoutInSeconds);
        element.clear();
        return element;
    }

    public boolean isElementPresent(String locator) {
        return driver.findElements(getLocatorByString(locator)).size() == 1;
    }

    public boolean isElementPresent(WebElement element, long timeOutInSeconds) {
        try {
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            e.toString();
        }
        return false;
    }

    //more general methods to compare any element text attribute value with given expected text
    //and mark test as 'failed' if text comparison fails
    public void validateElementText(WebElement element, String expectedText) {
        Assert.assertEquals("Actual element text does not match expected value",
                expectedText,
                element.getAttribute("text"));
    }

    public void waitForElementAndValidateText(String locator, String errorMessage, long timeoutInSeconds, String expectedText) {
        WebElement element = waitForElementToBePresent(locator, errorMessage, timeoutInSeconds);
        validateElementText(element, expectedText);
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

    public void swipeUpToFindElement(String locator, String errorMessage, int maxSwipes) {
        int alreadySwiped = 0;
        while(driver.findElements(getLocatorByString(locator)).size() == 0) {
            if(alreadySwiped > maxSwipes) {
                waitForElementToBePresent(locator, "Unable to find elent by swiping up. \n" + errorMessage);
                return;
            }
            swipeUpQuick();
            ++alreadySwiped;
        }
    }

    public void swipeUpTillElementAppear(String locator, String errorMessage, int maxSwipes) {
        int alreadySwiped = 0;
        while(!this.isElementLocatedOnTheScreen(locator)) {
            if(alreadySwiped > maxSwipes) {
                Assert.assertTrue(errorMessage, this.isElementLocatedOnTheScreen(locator));
                return;
            }
            swipeUpQuick();
            ++alreadySwiped;
        }
    }

    public boolean isElementLocatedOnTheScreen(String locator) {
        int elementLocationByY = this.waitForElementToBePresent(locator, "Unable to find element by locator " + locator, 1).getLocation().getY();
        int screenSizeByY = driver.manage().window().getSize().getHeight();
        return elementLocationByY < screenSizeByY;
    }

    public void clickElementToTheRightUpperCorner(String locator, String errorMessage) {
        WebElement element = waitForElementToBePresent(locator + "/..", errorMessage, 10);
        int rightX = element.getLocation().getX();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY)/2;
        int width = element.getSize().getWidth();

        int pointToClickX = (rightX + width) - 3;
        int pointToClickY = middleY;

        TouchAction action = new TouchAction(driver);
        action.tap(pointToClickX, pointToClickY).perform();
    }

    public void  swipeElementLeft(String locator, String errorMessage) {
        WebElement element = waitForElementToBePresent(locator, errorMessage, 10);

        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY)/2;

        TouchAction action = new TouchAction(driver);
        action.press(leftX, middleY);
        action.waitAction(300);

        if (Platform.getInstance().isAndroid()) {
            action.moveTo(rightX, middleY);
        } else {
            int offsetX = -1 * (element.getSize().getWidth());
            action.moveTo(offsetX, 0);
        }

        action.release();
        action.perform();;
    }

    public int getAmountOfElements(String locator) {
        return driver.findElements(getLocatorByString(locator)).size();
    }

    public void assertElementNotPresent(String locator, String errorMessage) {
        if(getAmountOfElements(locator) > 0) {
            String defaultMessage = "An element '" + locator + "' supposed to be not present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    public void assertElementPresent(String locator, String errorMessage) {
        if(getAmountOfElements(locator) == 0) {
            String defaultMessage = "An element '" + locator + "' supposed to be present";
            throw new AssertionError(defaultMessage + " " + errorMessage);
        }
    }

    public void assertElementAttributeValue(String locator, String attribute, String expectedValue, String errorMessage, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        String actualValue = waitForElementAndGetAttribute(locator, attribute, "Unable to locate element by " + by.toString(), timeoutInSeconds);
        if(!actualValue.equals(expectedValue)) {
            String defaultMessage = "An element's attribute '" + attribute + "' supposed to be equals to value " + expectedValue + " but actual value is " + actualValue + " ";
            throw new AssertionError(defaultMessage + errorMessage);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String errorMessage, long timeoutInSeconds) {
        return waitForElementToBePresent(locator, errorMessage, timeoutInSeconds)
                .getAttribute(attribute);
    }

    public By getLocatorByString(String locatorWithType) {
        String[] explodedLocator = locatorWithType.split(Pattern.quote(":"), 2);
        String byType = explodedLocator[0];
        String locator = explodedLocator[1];

        if(byType.equals("xpath")) {
            return By.xpath(locator);
        } else if(byType.equals("id")) {
            return By.id(locator);
        } else {
            throw new IllegalArgumentException("Unable to get locator type. Locator: " + locatorWithType);
        }
    }

}
