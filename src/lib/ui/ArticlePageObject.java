package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

abstract public class ArticlePageObject extends MainPageObject{
    protected static String
            TITLE,
            ARTICLE_IDENTIFIER_TPL,
            FOOTER_ELEMENT,
            OPTIONS_BUTTON,
            OPTIONS_ADD_TO_MY_LIST_BUTTON,
            ADD_TO_MY_LIST_OVERLAY_GOT_IT,
            MY_LIST_ITEM_IDENTIFIER_TPL,
            MY_LIST_NAME_INPUT,
            MY_LIST_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON;

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    private static String getArticleIdentifierXpath(String substring) {
        return ARTICLE_IDENTIFIER_TPL.replace("{ARTICLE_IDENTIFIER}", substring);
    }

    private static String getFolderNameXpath(String substring) {
        return MY_LIST_ITEM_IDENTIFIER_TPL.replace("{FOLDER_NAME}", substring);
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementToBePresent(TITLE, "Unable to locate article title on page", 15);
    }

    public WebElement waitForTitleElement(String title) {
        String locator =  getArticleIdentifierXpath(title);
        return this.waitForElementToBePresent(locator, "Unable to locate article title on page", 15);
    }

    public String getArticleDesc(String desc) {
        WebElement articleDesc = waitForTitleElement(desc);
        if (Platform.getInstance().isAndroid()) {
            return articleDesc.getAttribute("text");
        } else {
            return articleDesc.getAttribute("value");
        }
    }

    public String getArticleTitle() {
        WebElement titleElement = waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            return titleElement.getAttribute("text");
        } else {
            return titleElement.getAttribute("name");
        }
    }

    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid()) {
            this.swipeUpToFindElement(FOOTER_ELEMENT, "Unable to find the end of the article", 50);
        } else {
            this.swipeUpTillElementAppear(FOOTER_ELEMENT, "Unable to find the end of the article", 50);
        }
    }

    public void addArticleToReadingList(String folderName) {
        this.waitForElementAndClick(OPTIONS_BUTTON, "Unable to find 'More options' link", 5);

        this.waitForElementAndClick(OPTIONS_ADD_TO_MY_LIST_BUTTON, "Unable to select 'Add to reading list' context menu option", 5);

        //user has no list with given name, create new one
        if (this.isElementPresent(ADD_TO_MY_LIST_OVERLAY_GOT_IT)) {
            this.waitForElementAndClick(ADD_TO_MY_LIST_OVERLAY_GOT_IT, "Unable to locate 'GOT IT' tip overlay", 5);
            this.waitForElementAndClear(MY_LIST_NAME_INPUT, "Unable to find input to set articles folder name", 5);
            this.waitForElementAndSendKeys(MY_LIST_NAME_INPUT, folderName, "Unable to put text into articles folder input", 5);
            this.waitForElementAndClick(MY_LIST_OK_BUTTON, "Unable to press OK button", 5);
        }
        //user has already created the folder, just select it
        String folderNameXpath = getFolderNameXpath(folderName);
        if(this.isElementPresent(folderNameXpath)) {
            this.waitForElementAndClick(folderNameXpath, "Unable to add second article to " + folderName + " list", 5);
        }

        this.waitForElementAndClick(CLOSE_ARTICLE_BUTTON, "Unable to close article - missing X link", 5);
    }

    public void addArticleToMySaved() {
        this.waitForElementAndClick(OPTIONS_ADD_TO_MY_LIST_BUTTON, "Unable to find option to add article to reading list", 5);
        if (this.isElementPresent(ADD_TO_MY_LIST_OVERLAY_GOT_IT)) {
            this.waitForElementAndClick(ADD_TO_MY_LIST_OVERLAY_GOT_IT, "Unable to locate 'X' button on 'Sync...' dialog", 5);
        }
    }

    public void closeArticle() {
        this.waitForElementAndClick(CLOSE_ARTICLE_BUTTON, "Unable to close article - missing X link", 5);
    }

    public void assertArticleTitlePresence() {
        this.assertElementPresent(TITLE, " : unable to locate article title");
    }
}
