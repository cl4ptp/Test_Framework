package base;

import internal.proxy.SelenideProxyExtension;
import internal.reporting.JUnitLoggerExtension;
import internal.reporting.SelenideListener;
import internal.util.ElementHighlighting;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.BrowserStrategyExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import static internal.util.ElementHighlighting.IS_HIGHLIGHT_ELEMENTS;

/**
 * Base test class for all the other tests in the current framework.
 * Serves as a main parent class for every test in the framework.
 * <p></p>
 * The class defines some technical features for other tests:
 * web driver capabilities, Allure's report listener, basic commandline logging settings, etc...
 */
@ExtendWith({BrowserStrategyExtension.class, JUnitLoggerExtension.class})
@ExtendWith(SelenideProxyExtension.class)
public abstract class BaseTest {
    @BeforeAll
    public void setUpBaseTestAll() {
        if (WebDriverRunner.isChrome()) {
            var options = new ChromeOptions();
            options.addArguments("--disable-features=site-per-process");

            var capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            Configuration.browserCapabilities = capabilities;
        }

        SelenideLogger.addListener("allure", new SelenideListener());
        if (IS_HIGHLIGHT_ELEMENTS) {
            WebDriverRunner.addListener(new ElementHighlighting());
        }
    }

    @AfterAll
    public void tearDownBaseTestAll() {
        SelenideLogger.removeListener("allure");
    }
}
