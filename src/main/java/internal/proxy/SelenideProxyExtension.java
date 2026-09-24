package internal.proxy;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static utilities.JsonUtils.readResourceAsString;
import static com.codeborne.selenide.Selenide.open;

/**
 * JUnit Extension class that provides mock responses for external web services,
 * in case Selenide's proxy is enabled.
 * <p></p>
 * Right now all mock responses are set up directly in the main callback method.
 * If the number of mock responses starts to grow, then this needs to be refactored.
 * <p></p>
 * Should be used for the most test scenarios that don't check integration between CRM and web services.
 */
public class SelenideProxyExtension implements BeforeAllCallback {
    private boolean isRequestForUnsupportedPackage = false;

    /**
     * Open an empty web browser and set up mock responses for external web services.
     * <p></p>
     * Note: this callback is invoked once <em>before</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!Configuration.proxyEnabled) {
            return;
        }

        if (!WebDriverRunner.hasWebDriverStarted()) {
            open();
        }

        var selenideProxy = WebDriverRunner.getSelenideProxy();
        if (selenideProxy == null) {
            throw new RuntimeException("Selenide's proxy is not found! " +
                    "Make sure that the browser is open, proxy is enabled, and Selenide is configured correctly.");
        } else {
            addRequestFilterForUnsupportedPackages(selenideProxy);
            addResponseFilterForSupportedPackages(selenideProxy);
        }
    }

    /**
     * Filter requests to the Funnel Service for unsupported packages.
     * It is used to identify test cases that work with unsupported packages (e.g. "RingCentral Meetings")
     * to provide a proper mock response via {@link #addResponseFilterForSupportedPackages(SelenideProxyServer)}.
     *
     * @param proxy Selenide's proxy server instance
     */
    private void addRequestFilterForUnsupportedPackages(SelenideProxyServer proxy) {
        proxy.addRequestFilter("Funnel Service Request Filter for unsupported packages",
                (request, contents, messageInfo) -> {
                    var isPost = request.method().name().equalsIgnoreCase("POST");
                    var isGetCountries = messageInfo.getUrl().contains("/funnel-area-codes/get-countries");
                    var textBody = contents.getTextContents();
                    if (isPost && isGetCountries && !textBody.isBlank()) {
                        //  Any new unsupported packages' IDs should be incorporated into this RegEx
                        isRequestForUnsupportedPackage = textBody.matches(".*\"packageId\":\"[678]\".*");
                    }

                    return null;
                });
    }

    /**
     * Filter responses from the Funnel Service for supported packages.
     * It is used to mock responses from the service.
     *
     * @param proxy Selenide's proxy server instance
     */
    private void addResponseFilterForSupportedPackages(SelenideProxyServer proxy) {
        proxy.addResponseFilter("Funnel Service Mock Response Filter", (response, contents, messageInfo) -> {
            var isPost = messageInfo.getOriginalRequest().method().name().equalsIgnoreCase("POST");
            var isFunnelService = messageInfo.getUrl().contains("funnel-area-codes") ||
                    messageInfo.getUrl().contains("package/availability");
            if (!isPost && !isFunnelService) {
                return;
            }

            String newJsonResponse = null;
            if (messageInfo.getUrl().contains("/get-countries")) {
                newJsonResponse = isRequestForUnsupportedPackage ?
                        readResourceAsString("mock/get-countries-unsupported_response.json") :
                        readResourceAsString("mock/get-countries_response.json");
            } else if (messageInfo.getUrl().contains("/get-states")) {
                newJsonResponse = readResourceAsString("mock/get-states_response.json");
            } else if (messageInfo.getUrl().contains("/get-locations")) {
                newJsonResponse = readResourceAsString("mock/get-locations_response.json");
            } else if (messageInfo.getUrl().contains("/get-toll-free-prefixes")) {
                newJsonResponse = readResourceAsString("mock/get-toll-free-prefixes_response.json");
            } else if (messageInfo.getUrl().contains("/check-availability")) {
                newJsonResponse = readResourceAsString("mock/check-availability_response.json");
            } else if (messageInfo.getUrl().contains("/package/availability")) {
                newJsonResponse = readResourceAsString("mock/package-availability_response.json");
            }

            if (newJsonResponse != null) {
                response.headers().remove("Content-Length");
                contents.setTextContents(newJsonResponse);
            }
        });
    }
}
