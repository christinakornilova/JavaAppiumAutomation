import lib.CoreTestCase;
import lib.ui.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

    private MainPageObject mainPageObject;

    protected void setUp() throws Exception {
        super.setUp();
        mainPageObject = new MainPageObject(driver);
    }


    @Test
    public void testSearchResultCancellation() {

        mainPageObject.waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Unable to locate 'Search Wikipedia' input",
                5
        );

        mainPageObject.waitForElementAndSendKeys(By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Unable to locate search input",
                5
        );

        driver.hideKeyboard();

        //wait for search results
        mainPageObject.waitForElementToBePresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']"),
                "Unable to locate search results",
                15
        );

        //get search results to list
        String listOfSearchResultsXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";
        List<WebElement> listOfSearchResults = mainPageObject.waitForListOfElementsToBePresent(
                By.xpath(listOfSearchResultsXpath),
                "Unable to locate search results list",
                10
        );

        //verify that search results list is not empty
        assertTrue("List is empty",
                mainPageObject.getSearchResultsCount(listOfSearchResults) > 0);

        //clear search
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Unable to locate X clear search link",
                15
        );

        driver.hideKeyboard();

        //check that Search... element contains default text now
        mainPageObject.validateSearchElementText();

        assertFalse("Search results are still present on page",
                mainPageObject.isElementPresent(By.xpath(listOfSearchResultsXpath))
        );

        //verify that empty search result is displayed on page now
        assertTrue("Unable to locate empty search result list element",
                mainPageObject.isElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/search_empty_container']"))
        );
    }

    @Test
    public void testSearchResultsValidation() {
        String word = "Java";

        //fill the list with search results;
        List<WebElement> listOfSearchResults = mainPageObject.executeSearch(word);

        //check that each search result contains given word
        for(WebElement element : listOfSearchResults) {
            assertTrue("Search line does not contain appropriate word " + word,
                    element.getText().toLowerCase().contains(word.toLowerCase())
            );
        }
    }


    @Test
    public void testSaveTwoArticles() {

        String searchLine = "Java";

        //Add first article to the list
        String articleIdentifier = "Object-oriented programming language";
        String articleTitleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleIdentifier + "']";

        driver.resetApp();

        mainPageObject.search(searchLine);

        mainPageObject.openArticle(articleTitleXpath, searchLine);

        String folderName = "Learning programming";
        mainPageObject.addArticleToReadingList(folderName);

        //Add second article
        articleIdentifier = "Java (software platform)";
        String secondArticleTitleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleIdentifier + "']";

        mainPageObject.search(searchLine);

        mainPageObject.openArticle(secondArticleTitleXpath, searchLine);

        //put 2nd article to 'Learning programming' list
        mainPageObject.addArticleToReadingList(folderName);

        //Open list with two articles
        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Unable to locate 'My lists' navigation button",
                5
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']"),
                "Unable to find created folder",
                5
        );

        //assert that list contains two articles
        assertEquals(folderName + " list contains wrong number of articles",
                2,
                mainPageObject.getAmountOfElements(By.id("org.wikipedia:id/page_list_item_container")));

        String articleToDeleteXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + articleIdentifier + "']";

        mainPageObject.swipeElementLeft(
                By.xpath(articleToDeleteXpath),
                "Unable to find saved article"
        );

        //assert that one article is still in the list
        assertEquals(folderName + " list contains wrong number of articles",
                1,
                mainPageObject.getAmountOfElements(By.id("org.wikipedia:id/page_list_item_container")));

        //assert article title
        String expectedArticleTitle = "Java (programming language)";
        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + expectedArticleTitle + "']"),
                "Unable to find first article in the list after second one was deleted",
                15
        );
        System.out.println("click on " + expectedArticleTitle);

        String actualArticleTitle = mainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Unable to find article title",
                15
        );

        assertEquals("Actual article title differs from expected one",
                expectedArticleTitle,
                actualArticleTitle
        );
    }

    @Test
    public void testrticleTitlePresence() {
        String keyWord = "Three Days Grace";
        mainPageObject.search(keyWord);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='Canadian band']"),
                "Unable to locate article in the list searching by '" + keyWord + "'",
                5
        );

        String articleTitleXpath = "//*[@resource-id='org.wikipedia:id/view_page_title_text']";
        mainPageObject.assertElementPresent(By.xpath(articleTitleXpath), ": unable to locate article title");
    }


}
