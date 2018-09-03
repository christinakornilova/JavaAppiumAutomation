package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SearchPageObject extends MainPageObject {

    private final static String
            SEARCH_INIT_ELEMENT_XPATH = "//*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT_ID = "org.wikipedia:id/search_src_text",
            SEARCH_CANCEL_BUTTON_ID = "org.wikipedia:id/search_close_btn",
            SEARCH_RESULT_BY_SUBSTRING_XPATH_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
            SEARCH_RESULT_ELEMENT_XPATH = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']",
            SEARCH_EMPTY_RESULT_LABEL_XPATH = "//*[@resource-id='org.wikipedia:id/search_empty_text'][@text='No results found']";

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATES METHODS */
    private static String getResultSearchElement(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_XPATH_TPL.replace("{SUBSTRING}", substring);
    }
    /* TEMPLATES METHODS */

    public void initSearchElement() {
        this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT_XPATH), "Unable to locate and click search init element", 5);
        this.waitForElementToBePresent(By.id(SEARCH_INPUT_ID), "Unable to locate search input after clicking search init element", 5);
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementToBePresent(By.id(SEARCH_CANCEL_BUTTON_ID), "Unable to locate search cancel 'X' button", 5);
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON_ID), "Search cancel 'X' button is still present", 5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(By.id(SEARCH_CANCEL_BUTTON_ID), "Unable to locate and click search cancel 'X' button", 5);
    }

    public void typeSearchLine(String searchLine) {
        this.waitForElementAndSendKeys(By.id(SEARCH_INPUT_ID), searchLine, "Unable to find and type into search input", 5);
    }

    public void waitForSearchResult(String substring) {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementToBePresent(By.xpath(searchResultXpath), "Unable to find search result with substring " + substring);
    }

    public void clickByArticleWithSubstring(String substring) {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementAndClick(By.xpath(searchResultXpath), "Unable to find and click search result with substring " + substring, 10);
    }

    public int getamountOfFoundArticles() {
        this.waitForElementToBePresent(
                By.xpath(SEARCH_RESULT_ELEMENT_XPATH),
                "Unable to find anything by request",
                15
        );

        return this.getAmountOfElements(By.xpath(SEARCH_RESULT_ELEMENT_XPATH));
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementToBePresent(By.xpath(SEARCH_EMPTY_RESULT_LABEL_XPATH), "Unable to find empty result element", 15);
    }

    public void assertNoSearchResults() {
        this.assertElementNotPresent(By.xpath(SEARCH_RESULT_ELEMENT_XPATH), "Non zero number of search results were found by request");
    }
}
