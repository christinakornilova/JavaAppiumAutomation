package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class ChangeAppConditionTests extends CoreTestCase {

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        String titleBeforeRotation = articlePageObject.getArticleTitle();

        rotateScreenLandscape();
        String titleAfterRotation = articlePageObject.getArticleTitle();
        assertEquals("Article title has been changed after screen rotation", titleBeforeRotation, titleAfterRotation);

        rotateScreenPortrait();
        String titleAfterSecondRotation = articlePageObject.getArticleTitle();
        assertEquals("Article title has been changed after second screen rotation", titleBeforeRotation, titleAfterSecondRotation);
    }

    @Test
    public void testCheckSearchArticleInBackground() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");

        sendAppToBackground(2);
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }
}
