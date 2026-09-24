package page.salesforce;

import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.User;

import static utilities.Constants.BASE_URL;
import static utilities.Constants.LOGOUT_LINK;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class UserPage {

    public SelenideElement loginAsButton = $x("//input[@name='login']");

    /**
     * Open User url corresponding page
     * in the browser.
     *
     * @param user User whose page you want
     *             to open.
     * @return opened User Page reference
     */
    public UserPage openPage(User user) {
        var userPageUrl = BASE_URL + "/" + user.getId() + "?noredirect=1&isUserEntityOverride=1";
        open(userPageUrl);
        return this;
    }

    /**
     * Login as passed User.
     *
     * @param user User to log in to.
     */
    public void loginAsUser(User user) {
        openPage(user);
        loginAsButton.click();
    }

    /**
     * Logout in the current User session then
     * login under passed User.
     * Work only if you are already logged in
     * as some User(not QA Auto User).
     *
     * @param user User to log in to.
     */
    public void reLoginAsUser(User user) {
        open(LOGOUT_LINK);
        loginAsUser(user);
    }

}
