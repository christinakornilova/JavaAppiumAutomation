package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ChangeAppConditionTests extends CoreTestCase {

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
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
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");

        sendAppToBackground(2);
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }
}
