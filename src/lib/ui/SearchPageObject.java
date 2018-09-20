package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract public class SearchPageObject extends MainPageObject {

    protected static String
            SEARCH_INIT_ELEMENT,
            SEARCH_INPUT,
            SEARCH_CANCEL_BUTTON,
            SEARCH_RESULT_BY_SUBSTRING_TPL,
            SEARCH_RESULT_ELEMENT,
            SEARCH_EMPTY_RESULT_LABEL,
            SEARCH_RESULTS,
            SEARCH_RESULTS_LIST_ELEMENT,
            EMPTY_SEARCH_RESULT_ELEMENT,
            SEARCH_ELEMENT_DEFAULT_TEXT,
            SEARCH_RESULT_ELEMENT_BY_TITLE_AND_DESCRIPTION_TPL;

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATES METHODS */
    private static String getResultSearchElement(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultSearchElementXpathByTitleAndDescription(String title, String description) {
        String xpath = SEARCH_RESULT_ELEMENT_BY_TITLE_AND_DESCRIPTION_TPL.replace("{ARTICLE_TITLE}", title);
        xpath = xpath.replace("{ARTICLE_DESCRIPTION}", description);
        return xpath;
    }
    /* TEMPLATES METHODS */

    public void initSearchElement() {
        this.waitForElementAndClick(SEARCH_INIT_ELEMENT, "Unable to locate and click search init element", 5);
        this.waitForElementToBePresent(SEARCH_INPUT, "Unable to locate search input after clicking search init element", 5);
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementToBePresent(SEARCH_CANCEL_BUTTON, "Unable to locate search cancel 'X' button", 5);
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Search cancel 'X' button is still present", 5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(SEARCH_CANCEL_BUTTON, "Unable to locate and click search cancel 'X' button", 5);
    }

    public void typeSearchLine(String searchLine) {
        this.waitForElementAndSendKeys(SEARCH_INPUT, searchLine, "Unable to find and type into search input", 5);
    }

    public void waitForSearchResult(String substring) {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementToBePresent(searchResultXpath, "Unable to find search result with substring " + substring);
    }

    public void clickByArticleWithSubstring(String substring) {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementAndClick(searchResultXpath, "Unable to find and click search result with substring " + substring, 10);
    }

    public int getAmountOfFoundArticles() {
        this.waitForElementToBePresent(SEARCH_RESULT_ELEMENT, "Unable to find anything by request", 15);
        return this.getAmountOfElements(SEARCH_RESULT_ELEMENT);
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementToBePresent(SEARCH_EMPTY_RESULT_LABEL, "Unable to find empty result element", 15);
    }

    public void assertNoSearchResults() {
        this.assertElementNotPresent(SEARCH_RESULT_ELEMENT, "Non zero number of search results were found by request");
    }

    public void waitForSearchResultsToAppear() {
        //wait for search results
        this.waitForElementToBePresent(SEARCH_RESULTS, "Unable to locate search results", 15);
    }

    public List<WebElement> getSearchResultsList() {
        //get search results to list
         return this.waitForListOfElementsToBePresent(SEARCH_RESULTS_LIST_ELEMENT, "Unable to locate search results list", 10);
    }

    public int getSearchResultsCount() {
        return this.getSearchResultsCount(getSearchResultsList());
    }

    public boolean isSearchResultsListPresent() {
        return this.isElementPresent(SEARCH_RESULTS);
    }

    public boolean isEmptySearchResultDisplayed() {
        return this.isElementPresent(EMPTY_SEARCH_RESULT_ELEMENT);
    }

    /*
    Написать функцию, которая проверяет наличие текста “Search…” в строке поиска перед вводом текста
     и помечает тест упавшим, если такого текста нет.
     */
    public void validateDefaultSearchElementText() {
        //ex2
        this.assertElementAttributeValue(SEARCH_ELEMENT_DEFAULT_TEXT,"text", "Search…", " Unable to locate 'Search...' input", 5);
    }

    public void enterSearchKeyWord(String keyWord) {
        this.initSearchElement();
        this.typeSearchLine(keyWord);
        this.waitForSearchResultsToAppear();
    }

    public List<WebElement> search(String keyWord) {
        enterSearchKeyWord(keyWord);
        //get search results
        return this.waitForListOfElementsToBePresent(SEARCH_RESULTS_LIST_ELEMENT, "Unable to locate search results list", 5);
    }

    public WebElement waitForElementByTitleAndDescription(String title, String description) {
        try {
            return this.waitForElementToBePresent(
                    getResultSearchElementXpathByTitleAndDescription(title, description),
                    "Unable to locate article in search results with title " + title + " and description " + description,
                    5
            );
        } catch (Exception e) {
            e.getMessage().toString();
        }
        return null;
    }

}
