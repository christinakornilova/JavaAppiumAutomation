package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject{
    public final static String
            TITLE_ID = "org.wikipedia:id/view_page_title_text",
            ARTICLE_TITLE_IDENTIFIER_XPATH_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{ARTICLE_IDENTIFIER}']",
            FOOTER_ELEMENT_XPATH = "//*[@text='View page in browser']",
            OPTIONS_BUTTON_XPATH = "//android.widget.ImageView[@content-desc='More options']",
            OPTIONS_ADD_TO_MY_LIST_BUTTON_XPATH = "//*[@text='Add to reading list']",
            ADD_TO_MY_LIST_OVERLAY_GOT_IT_XPATH = "org.wikipedia:id/onboarding_button",
            MY_LIST_ITEM_IDENTIFIER_XPATH_TPL = "//*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']",
            MY_LIST_NAME_INPUT_ID = "org.wikipedia:id/text_input",
            MY_LIST_OK_BUTTON_XPATH = "//*[@text='OK']",
            CLOSE_ARTICLE_BUTTON_XPATH = "//android.widget.ImageButton[@content-desc='Navigate up']";

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    private static String getArticleTitleXpath(String substring) {
        return ARTICLE_TITLE_IDENTIFIER_XPATH_TPL.replace("{ARTICLE_IDENTIFIER}", substring);
    }

    private static String getFolderNameXpath(String substring) {
        return MY_LIST_ITEM_IDENTIFIER_XPATH_TPL.replace("{FOLDER_NAME}", substring);
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementToBePresent(By.id(TITLE_ID), "Unable to locate article title on page", 15);
    }

    public String getArticleTitle() {
        return waitForTitleElement().getAttribute("text");
    }

    public void swipeToFooter() {
        swipeUpToFindElement(By. xpath(FOOTER_ELEMENT_XPATH), "Unable to find the end of the article", 20);
    }

    public void addArticleToReadingList(String folderName) {
        this.waitForElementAndClick(By.xpath(OPTIONS_BUTTON_XPATH), "Unable to find 'More options' link", 5);

        this.waitForElementAndClick(By.xpath(OPTIONS_ADD_TO_MY_LIST_BUTTON_XPATH), "Unable to select 'Add to reading list' context menu option", 5);

        //user has no list with given name, create new one
        if (this.isElementPresent(By.id(ADD_TO_MY_LIST_OVERLAY_GOT_IT_XPATH))) {
            this.waitForElementAndClick(By.id(ADD_TO_MY_LIST_OVERLAY_GOT_IT_XPATH), "Unable to locate 'GOT IT' tip overlay", 5);
            this.waitForElementAndClear(By.id(MY_LIST_NAME_INPUT_ID), "Unable to find input to set articles folder name", 5);
            this.waitForElementAndSendKeys(By.id(MY_LIST_NAME_INPUT_ID), folderName, "Unable to put text into articles folder input", 5);
            this.waitForElementAndClick(By.xpath(MY_LIST_OK_BUTTON_XPATH), "Unable to press OK button", 5);
        }
        //user has already created the folder, just select it
        String folderNameXpath = getFolderNameXpath(folderName);
        if(this.isElementPresent(
                By.xpath(folderNameXpath))) {
            this.waitForElementAndClick(By.xpath(folderNameXpath), "Unable to add second article to " + folderName + " list", 5);
        }

        this.waitForElementAndClick(By.xpath(CLOSE_ARTICLE_BUTTON_XPATH), "Unable to close article - missing X link", 5);
    }

    public void closeArticle() {
        this.waitForElementAndClick(By.xpath(CLOSE_ARTICLE_BUTTON_XPATH), "Unable to close article - missing X link", 5);
    }

    public void assertArticleTitlePresence() {
        this.assertElementPresent(By.id(TITLE_ID), " : unable to locate article title");
    }
}
