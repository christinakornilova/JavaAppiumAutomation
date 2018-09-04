package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class ArticleTests extends CoreTestCase {

    @Test
    public void testCompareArticleTitle() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        String articleTitle =  articlePageObject.getArticleTitle();

        assertEquals("Unexpected title is displayed",
                "Java (programming language)",
                articleTitle
        );
    }

    @Test
    public void testSwipeArticle() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Appium");
        searchPageObject.clickByArticleWithSubstring("Appium");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();
        articlePageObject.swipeToFooter();

    }

    @Test
    public void testArticleTitlePresence() {
        //ex6
        String keyWord = "Three Days Grace";
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.search(keyWord);
        searchPageObject.clickByArticleWithSubstring("Canadian band");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.assertArticleTitlePresence();
    }

}
