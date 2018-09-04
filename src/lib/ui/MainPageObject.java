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

    public void waitForElementAndValidateText(By by, String errorMessage, long timeoutInSeconds, String expectedText) {
        WebElement element = waitForElementToBePresent(by, errorMessage, timeoutInSeconds);
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

    public void assertElementAttributeValue(By by, String attribute, String expectedValue, String errorMessage, long timeoutInSeconds) {
        String actualValue = waitForElementAndGetAttribute(by, attribute, "Unable to locate element by " + by.toString(), timeoutInSeconds);
        if(!actualValue.equals(expectedValue)) {
            String defaultMessage = "An element's attribute '" + attribute + "' supposed to be equals to value " + expectedValue + " but actual value is " + actualValue + " ";
            throw new AssertionError(defaultMessage + errorMessage);
        }
    }

    public String waitForElementAndGetAttribute(By by, String attribute, String errorMessage, long timeoutInSeconds) {
        return waitForElementToBePresent(by, errorMessage, timeoutInSeconds)
                .getAttribute(attribute);
    }

}
