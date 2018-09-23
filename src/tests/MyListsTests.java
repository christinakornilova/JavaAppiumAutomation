package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MyListsTests extends CoreTestCase {
    private final static String folderName = "Learning programming";

    private List<WebElement> getSearchResultsByKeyword(String keyword, SearchPageObject searchPageObject) {
        List<WebElement> listOfSearchResults = searchPageObject.search(keyword);
        //verify that search results list is not empty
        assertTrue("List is empty",
                searchPageObject.getSearchResultsCount() > 0);
        return listOfSearchResults;
    }

    @Test
    public void testSaveArticleToMyList() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.waitForTitleElement();
        String articleTitle =  articlePageObject.getArticleTitle();

        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleToReadingList(folderName);
        } else {
            articlePageObject.addArticleToMySaved();
        }

        articlePageObject.closeArticle();

        NavigationUI navigationUI = NavigationUIObjectFactory.get(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            myListsPageObject.openReadingListByName(folderName);
        }
        myListsPageObject.swipeArticleToDelete(articleTitle);

    }

    @Test
    public void testSaveTwoArticles() {
        //ex5, ex11
        String searchLine = "Java";

        //Add first article to the list
        String articleIdentifier = "Object-oriented programming language";

//        resetApp();

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        getSearchResultsByKeyword(searchLine, searchPageObject);
        searchPageObject.clickByArticleWithSubstring(articleIdentifier);

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleToReadingList(folderName);
        } else {
            articlePageObject.addArticleToMySaved();
        }
        articlePageObject.closeArticle();

        //Add second article
        articleIdentifier = "Java (software platform)";

        searchPageObject.search(searchLine);
        searchPageObject.clickByArticleWithSubstring(articleIdentifier);

        if (Platform.getInstance().isAndroid()) {
            articlePageObject.waitForTitleElement();
        } else {
            String id = "Set of several computer software products and specifications";
            articlePageObject.waitForTitleElement(id);
        }

        //put 2nd article to 'Learning programming' list
        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleToReadingList(folderName);
        } else {
            articlePageObject.addArticleToMySaved();
        }
        articlePageObject.closeArticle();

        //Open list with articles
        NavigationUI navigationUI = NavigationUIObjectFactory.get(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            myListsPageObject.openReadingListByName(folderName);
        }

        //assert that list contains two articles
        assertEquals("List contains wrong number of articles", 2, myListsPageObject.getAmountOfArticlesInTheReadingList());
        String articleToDeleteIdentifier = articleIdentifier;

        //delete one article
        myListsPageObject.swipeArticleToDelete(articleToDeleteIdentifier);

        //assert that one article is still in the list
        String assertMessage;
        if(Platform.getInstance().isAndroid()) {
            assertMessage = folderName + " list contains wrong number of articles";
        } else {
            assertMessage = "Saved list contains wrong number of articles";
        }
        assertEquals(assertMessage, 1, myListsPageObject.getAmountOfArticlesInTheReadingList());

        //assert article title/description
        String expectedArticleTitle = "Java (programming language)";
        String expectedArticleDesc = "Object-oriented programming language";

        myListsPageObject.navigateToArticlesPage(expectedArticleTitle);

        if(Platform.getInstance().isAndroid()) {
            String actualArticleTitle = articlePageObject.getArticleTitle();
            assertEquals("Actual article description differs from expected one", expectedArticleTitle, actualArticleTitle);
        } else {
            String actualArticleDesc = articlePageObject.getArticleDesc(expectedArticleDesc);
            assertEquals("Actual article description differs from expected one", expectedArticleDesc, actualArticleDesc);
        }
    }

}
