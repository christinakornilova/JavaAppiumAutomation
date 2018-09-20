package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;

abstract public class MyListsPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            SWIPE_BY_ARTICLE_TPL,
            MY_READING_LIST_ITEMS;

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    private static String getFolderXpathByName(String folderName) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", folderName);
    }

    private static String getSavedArticleXpathByTitle(String articleTitle) {
        return SWIPE_BY_ARTICLE_TPL.replace("{TITLE}", articleTitle);
    }

    public void openReadingListByName(String folderName) {
        this.waitForElementAndClick(
                getFolderXpathByName(folderName),
                "Unable to find created folder by name " + folderName,
                5
        );
    }

    public void swipeArticleToDelete(String articleTitle) {
        this.waitForArticleToAppearByTitle(articleTitle);
        String articleXpath = getSavedArticleXpathByTitle(articleTitle);
        this.swipeElementLeft(
                articleXpath,
                "Unable to find saved article by title " + articleTitle
        );

        if (Platform.getInstance().isIOS()) {
            this.clickElementToTheRightUpperCorner(articleXpath, "Unable to find saved article ");
        }

        this.waitForArticleToDisappearByTitle(articleTitle);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle) {
        this.waitForElementNotPresent(
                getSavedArticleXpathByTitle(articleTitle),
                "Unable to delete saved article by title " + articleTitle,
                5
        );
    }

    public void waitForArticleToAppearByTitle(String articleTitle) {
        this.waitForElementToBePresent(
                getSavedArticleXpathByTitle(articleTitle),
                "Unable to find saved article by title " + articleTitle,
                5
        );
    }

    public int getAmountOfArticlesInTheReadingList() {
        return this.getAmountOfElements(MY_READING_LIST_ITEMS);
    }

    public void navigateToArticlesPage(String articleTitle) {
        this.waitForElementAndClick(getSavedArticleXpathByTitle(articleTitle), "Unable to click on first article title in the list after second one was deleted", 15);
    }

}
