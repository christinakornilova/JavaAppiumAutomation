package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

    private static final String
            FOLDER_BY_NAME_XPATH_TPL = "//*[@text='{FOLDER_NAME}']",
            SWIPE_BY_ARTICLE_XPATH_TPL = "//*[@text='{TITLE}']",
            MY_READING_LIST_ITEMS_XPATH = "org.wikipedia:id/page_list_item_container";

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    private static String getFolderXpathByName(String folderName) {
        return FOLDER_BY_NAME_XPATH_TPL.replace("{FOLDER_NAME}", folderName);
    }

    private static String getSavedArticleXpathByTitle(String articleTitle) {
        return SWIPE_BY_ARTICLE_XPATH_TPL.replace("{TITLE}", articleTitle);
    }

    public void openReadingListByName(String folderName) {
        this.waitForElementAndClick(
                By.xpath(getFolderXpathByName(folderName)),
                "Unable to find created folder by name " + folderName,
                5
        );
    }

    public void swipeArticleToDelete(String articleTitle) {
        this.waitForArticleToAppearByTitle(articleTitle);

        this.swipeElementLeft(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Unable to find saved article by title " + articleTitle
        );

        this.waitForArticleToDisappearByTitle(articleTitle);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle) {
        this.waitForElementNotPresent(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Unable to delete saved article by title " + articleTitle,
                5
        );
    }

    public void waitForArticleToAppearByTitle(String articleTitle) {
        this.waitForElementToBePresent(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Unable to find saved article by title " + articleTitle,
                5
        );
    }

    public int getAmountOfArticlesInTheReadingList() {
        return this.getAmountOfElements(By.id(MY_READING_LIST_ITEMS_XPATH));
    }

    public void navigateToArticlesPage(String articleTitle) {
        this.waitForElementAndClick(By.xpath(getSavedArticleXpathByTitle(articleTitle)), "Unable to click on first article title in the list after second one was deleted", 15);
    }

}
