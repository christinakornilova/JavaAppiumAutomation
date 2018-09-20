package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.SearchPageObject;

public class IOSSearchPageObject extends SearchPageObject {

    static {
        SEARCH_INIT_ELEMENT = "xpath://XCUIElementTypeSearchField[@name='Search Wikipedia']";
        SEARCH_INPUT = "xpath://XCUIElementTypeSearchField[@value='Search Wikipedia']";
        SEARCH_CANCEL_BUTTON = "id:clear mini";
        SEARCH_RESULT_BY_SUBSTRING_TPL = "xpath://XCUIElementTypeLink[contains(@name,'{SUBSTRING}')]";
        SEARCH_RESULT_ELEMENT = "xpath://XCUIElementTypeLink";
        SEARCH_EMPTY_RESULT_LABEL = "xpath://XCUIElementTypeStaticText[@name='No results found']"; //id:No results found
        SEARCH_RESULTS = "xpath://XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeCollectionView/XCUIElementTypeCell";
        SEARCH_RESULTS_LIST_ELEMENT = SEARCH_RESULTS + "/XCUIElementTypeLink";
        SEARCH_RESULT_ELEMENT_BY_TITLE_AND_DESCRIPTION_TPL = "xpath://XCUIElementTypeLink[@name='{ARTICLE_TITLE}\n{ARTICLE_DESCRIPTION}']";
    }

    public IOSSearchPageObject(AppiumDriver driver) {
        super(driver);
    }
}
