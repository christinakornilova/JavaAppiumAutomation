package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class AndroidMyListsPageObject extends MyListsPageObject {
    static {
        FOLDER_BY_NAME_TPL = "xpath://*[@text='{FOLDER_NAME}']";
        SWIPE_BY_ARTICLE_TPL = "xpath://*[@text='{TITLE}']";
        MY_READING_LIST_ITEMS = "id:org.wikipedia:id/page_list_item_container";
    }

    public AndroidMyListsPageObject(AppiumDriver driver) {
        super(driver);
    }
}
