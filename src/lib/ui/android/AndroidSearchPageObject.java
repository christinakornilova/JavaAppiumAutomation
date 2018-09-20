package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.SearchPageObject;

public class AndroidSearchPageObject extends SearchPageObject {

    static {
        SEARCH_INIT_ELEMENT = "xpath://*[contains(@text; 'Search Wikipedia')]";
        SEARCH_INPUT = "id:org.wikipedia:id/search_src_text";
        SEARCH_CANCEL_BUTTON = "id:org.wikipedia:id/search_close_btn";
        SEARCH_RESULT_BY_SUBSTRING_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";
        SEARCH_RESULT_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        SEARCH_EMPTY_RESULT_LABEL = "xpath://*[@resource-id='org.wikipedia:id/search_empty_text'][@text='No results found']";
        SEARCH_RESULTS = "xpath://*[@resource-id='org.wikipedia:id/search_results_list']";
        SEARCH_RESULTS_LIST_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_title']";
        EMPTY_SEARCH_RESULT_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/search_empty_container']";
        SEARCH_ELEMENT_DEFAULT_TEXT = "id:org.wikipedia:id/search_src_text";
        SEARCH_RESULT_ELEMENT_BY_TITLE_AND_DESCRIPTION_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']/android.widget.LinearLayout[child::android.widget.TextView[@text='{ARTICLE_TITLE}'] and child::android.widget.TextView[@text='{ARTICLE_DESCRIPTION}']]";
    }


    public AndroidSearchPageObject(AppiumDriver driver) {
        super(driver);
    }
}
