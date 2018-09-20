package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.SearchPageObject;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchTests extends CoreTestCase {

    private List<WebElement> getSearchResultsByKeyword(String keyword, SearchPageObject searchPageObject) {
        List<WebElement> listOfSearchResults = searchPageObject.search(keyword);
        //verify that search results list is not empty
        assertTrue("List is empty",
                searchPageObject.getSearchResultsCount() > 0);
        return listOfSearchResults;
    }

    @Test
    public void testSearch() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testCancelSearch() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testSearchResultCancellation() {
        //ex3
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");

        //wait for search results
        searchPageObject.waitForSearchResultsToAppear();

        //verify that search results list is not empty
        assertTrue("Search results list is empty", searchPageObject.getSearchResultsCount() > 0);

        //cancel search results
        searchPageObject.clickCancelSearch();

        if (Platform.getInstance().isAndroid()) {
            hideKeyboard();
            //check that Search... element contains default text now
            searchPageObject.validateDefaultSearchElementText();
            //verify that empty search result is displayed on page now
            assertTrue("Unable to locate empty search result list element", searchPageObject.isEmptySearchResultDisplayed());
        }

        //assert that no search results displayed
        assertFalse("Search results are still present on page", searchPageObject.isSearchResultsListPresent());

    }

    @Test
    public void testAmountOfNotEmptySearch() {
        String searchLine = "Linkin Park discography";
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine(searchLine);
        int amountOfSearchResults = searchPageObject.getAmountOfFoundArticles();

        assertTrue("Too few results were found!",
                amountOfSearchResults > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        String searchLine = "zxcvfgretw";
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine(searchLine);
        searchPageObject.waitForEmptyResultsLabel();
        searchPageObject.assertNoSearchResults();
    }

    @Test
    public void testSearchResultsValidation() {
        //ex4*
        String keyWord = "Java";

        //fill the list with search results
        List<WebElement> listOfSearchResults = getSearchResultsByKeyword(keyWord, SearchPageObjectFactory.get(driver));

        //check that each search result contains given word
        for(WebElement element : listOfSearchResults) {
            assertTrue("Search line does not contain appropriate word " + keyWord,
                    element.getText().toLowerCase().contains(keyWord.toLowerCase())
            );
        }
    }

    @Test
    public void testPositiveSearchArticleByTitleAndDescription() {
        String title = "Danny Worsnop";
        String description = "The lead singer of Asking Alexandria and We Are Harlot.";
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.search(title);
        //check first result
        assertTrue("Unable to locate search result article via title '" + title + "' and description '" + description + "'",
                searchPageObject.isElementPresent(
                        searchPageObject.waitForElementByTitleAndDescription(title, description),
                        5
                )
        );

        //check second result
        title = "We Are Harlot";
        description = "American hard rock supergroup";
        assertTrue("Unable to locate search result article via title '" + title + "' and description '" + description + "'",
                searchPageObject.isElementPresent(
                        searchPageObject.waitForElementByTitleAndDescription(title, description),
                        5
                )
        );

        //check third result
        title = "We Are Harlot (album)";
        description = "We Are Harlot (album)";
        assertTrue("Unable to locate search result article via title '" + title + "' and description '" + description + "'",
                searchPageObject.isElementPresent(
                        searchPageObject.waitForElementByTitleAndDescription(title, description),
                        5
                )
        );
    }

    @Test
    public void testNegativeSearchArticleByTitleAndDescription() {
        String title = "Asking Alexandria";
        String description = "XYZ";
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.search(title);
        assertTrue("Unable to locate search result article via title '" + title + "' and description '" + description + "'",
                searchPageObject.isElementPresent(
                        searchPageObject.waitForElementByTitleAndDescription(title, description),
                        5
                )
        );
    }

}
