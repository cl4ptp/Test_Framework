package page;

import com.codeborne.selenide.SelenideElement;

import static utilities.Constants.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.time.Duration.ofSeconds;

/**
 * Login page where user logs in to Salesforce org using username and password.
 */
public class LoginPage {
    //  Elements on the login form
    public final SelenideElement logo = $("#logo");
    public final SelenideElement usernameInputBox = $("#username");
    public final SelenideElement passwordInputBox = $("#password");
    public final SelenideElement loginButton = $("#Login");

    //  Elements on the login confirmation form
    public final SelenideElement continueButton = $("input[value='Continue']");

    /**
     * Open Login page via direct link using Base URL.
     * <p> Note: contents for Base URL are usually provided via system properties. </p>
     *
     * @return opened Login Page reference
     */
    public LoginPage openPage() {
        open(BASE_URL);
        return this;
    }

    /**
     * Login to Salesforce org using default credentials
     * by submitting username and password via UI (login form).
     * <p> Note: default credentials are usually provided via system properties. </p>
     */
    public void login() {
        if (logo.isDisplayed()) {
            usernameInputBox.setValue(USER);
            passwordInputBox.setValue(PASSWORD);
            loginButton.click();
        }
    }

    /**
     * Login to Salesforce org using default credentials via direct parameterized link.
     * This method also covers confirmation form after using direct link.
     * <p> Note: default credentials are usually provided via system properties. </p>
     */
    public void loginDirect() {
        open(LOGIN_WITH_PARAMETERS_URL);
        continueButton.shouldBe(visible, ofSeconds(10)).click();
    }
}
