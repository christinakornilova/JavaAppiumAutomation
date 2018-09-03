package tests;

import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class SearchTests extends CoreTestCase {

    @Test
    public void testSearch() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testCancelSearch() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchElement();
//        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        String searchLine = "Linkin Park discography";
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine(searchLine);
        int amountOfSearchResults = searchPageObject.getamountOfFoundArticles();

        assertTrue("Too few results were found!",
                amountOfSearchResults > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        String searchLine = "zxcvfgretw";
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchElement();
        searchPageObject.typeSearchLine(searchLine);
        searchPageObject.waitForEmptyResultsLabel();
        searchPageObject.assertNoSearchResults();
    }
}
