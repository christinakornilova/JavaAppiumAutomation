package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class WelcomePageObject extends MainPageObject {

    private final static String
            STEP_LEARN_MORE_LINK_ID = "Learn more about Wikipedia",
            STEP_NEW_WAYS_TO_EXPLORE_TEXT_ID = "New ways to explore",
            STEP_ADD_OR_EDIT_PREFERRED_LANGS_LINK_ID = "Add or edit preferred languages",
            STEP_LEARN_MORE_ABOUT_DATA_COLLECTED_LINK_ID = "Learn more about data collected",
            NEXT_BUTTON_ID = "Next",
            GET_STARTED_BUTTON_ID = "Get started";

    public WelcomePageObject(AppiumDriver driver) {
        super(driver);
    }

    public void waitForLearnMoreLink() {
        this.waitForElementToBePresent(By.id(STEP_LEARN_MORE_LINK_ID), "Unable to locate 'Learn more about Wikipedia' link", 10);
    }

    public void waitForNewWaysToExploreText() {
        this.waitForElementToBePresent(By.id(STEP_NEW_WAYS_TO_EXPLORE_TEXT_ID), "Unable to locate 'New ways to explore' text", 10);
    }

    public void waitForAddOrEditPreferredLangsLink() {
        this.waitForElementToBePresent(By.id(STEP_ADD_OR_EDIT_PREFERRED_LANGS_LINK_ID), "Unable to locate 'Add or edit preferred languages' link", 10);
    }

    public void waitForLearnMoreAboutDataCollectedLink() {
        this.waitForElementToBePresent(By.id(STEP_LEARN_MORE_ABOUT_DATA_COLLECTED_LINK_ID), "Unable to locate 'Learn more about data collected' link", 10);
    }

    public void clickNextButton() {
        this.waitForElementAndClick(By.id(NEXT_BUTTON_ID), "Unable to locate and click 'Next' button", 10);
    }

    public void clickGetStartedButton() {
        this.waitForElementAndClick(By.id(GET_STARTED_BUTTON_ID), "Unable to locate and click 'Get started' button", 10);
    }
}