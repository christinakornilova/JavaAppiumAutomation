package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;
import junit.framework.TestCase;
import lib.ui.WelcomePageObject;
import org.openqa.selenium.ScreenOrientation;

public class CoreTestCase extends TestCase {
    protected AppiumDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        driver = Platform.getInstance().getDriver();
        this.rotateScreenPortrait();
        this.skipWelcomePageForIOSApp();
    }

    @Override
    protected void tearDown() throws Exception {
        driver.quit();
        super.tearDown();
    }

    protected void rotateScreenPortrait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    protected void rotateScreenLandscape() {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    protected void sendAppToBackground(int seconds) {
        driver.runAppInBackground(seconds);
    }

    protected void hideKeyboard() {
        if (Platform.getInstance().isAndroid()) {
            driver.hideKeyboard();
        } else if (Platform.getInstance().isIOS()) {
            driver.findElementByAccessibilityId("Hide keyboard").click();
        }
    }

    protected void resetApp() {
        driver.resetApp();
    }

    private void skipWelcomePageForIOSApp() {
        if (Platform.getInstance().isIOS()) {
            WelcomePageObject welcomePageObject = new WelcomePageObject(driver);
            welcomePageObject.clickSkip();
        }
    }

}
