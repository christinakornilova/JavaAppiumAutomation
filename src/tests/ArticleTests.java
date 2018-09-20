package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ArticleTests extends CoreTestCase {

    @Test
    public void testCompareArticleTitle() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        String articleTitle =  articlePageObject.getArticleTitle();

        assertEquals("Unexpected title is displayed",
                "Java (programming language)",
                articleTitle
        );
    }

    @Test
    public void testSwipeArticle() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.waitForTitleElement();
        articlePageObject.swipeToFooter();

    }

    @Test
    public void testArticleTitlePresence() {
        //ex6
        String keyWord, substring;

        if (Platform.getInstance().isAndroid()) {
            keyWord = "Three Days Grace";
            substring = "Canadian band";
        } else {
            keyWord = "Java";
            substring = "Object-oriented programming language";
        }

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.search(keyWord);
        searchPageObject.clickByArticleWithSubstring(substring);

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.assertArticleTitlePresence();
    }

}
