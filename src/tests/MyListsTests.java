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
        //ex5
        String searchLine = "Java";
        String folderName = "Learning programming";

        //Add first article to the list
        String articleIdentifier = "Object-oriented programming language";

        resetApp();

        SearchPageObject searchPageObject = new SearchPageObject(driver);
        getSearchResultsByKeyword(searchLine, searchPageObject);
        searchPageObject.clickByArticleWithSubstring(articleIdentifier);

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();
        articlePageObject.addArticleToReadingList(folderName);

        //Add second article
        articleIdentifier = "Java (software platform)";

        searchPageObject.search(searchLine);
        searchPageObject.clickByArticleWithSubstring(articleIdentifier);
        articlePageObject.waitForTitleElement();

        //put 2nd article to 'Learning programming' list
        articlePageObject.addArticleToReadingList(folderName);

        //Open list with two articles
        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openReadingListByName(folderName);

        //assert that list contains two articles
        System.out.println("List size: " + myListsPageObject.getAmountOfArticlesInTheReadingList());
        String articleToDeleteIdentifier = articleIdentifier;
        myListsPageObject.swipeArticleToDelete(articleToDeleteIdentifier);

        //assert that one article is still in the list
        assertEquals(folderName + " list contains wrong number of articles",
                1,
                myListsPageObject.getAmountOfArticlesInTheReadingList());

        //assert article title
        String expectedArticleTitle = "Java (programming language)";

        myListsPageObject.navigateToArticlesPage(expectedArticleTitle);
        System.out.println("Click on " + expectedArticleTitle);

        String actualArticleTitle = articlePageObject.getArticleTitle();

        assertEquals("Actual article title differs from expected one",
                expectedArticleTitle,
                actualArticleTitle
        );
    }

}
