package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class IOSMyListsPageObject extends MyListsPageObject {
    static {
        SWIPE_BY_ARTICLE_TPL = "xpath://XCUIElementTypeLink[contains(@name,'{TITLE}')]";
        MY_READING_LIST_ITEMS = "id:org.wikipedia:id/page_list_item_container";
    }

    public IOSMyListsPageObject(AppiumDriver driver) {
        super(driver);
    }
}
