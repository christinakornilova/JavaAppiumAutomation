package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.WelcomePageObject;
import org.junit.Test;

public class GetStartedTest extends CoreTestCase {

    @Test
    public void testPassThroughWelcome() {
        if (Platform.getInstance().isAndroid()) {
            return;
        }
        WelcomePageObject welcomePage = new WelcomePageObject(driver);

        welcomePage.waitForLearnMoreLink();
        welcomePage.clickNextButton();

        welcomePage.waitForNewWaysToExploreText();
        welcomePage.clickNextButton();

        welcomePage.waitForAddOrEditPreferredLangsLink();
        welcomePage.clickNextButton();

        welcomePage.waitForLearnMoreAboutDataCollectedLink();
        welcomePage.clickGetStartedButton();
    }
}
