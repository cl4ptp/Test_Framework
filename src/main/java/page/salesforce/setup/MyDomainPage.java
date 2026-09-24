package page.salesforce.setup;

import page.salesforce.IframePage;
import com.codeborne.selenide.SelenideElement;

import static utilities.Constants.BASE_URL;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Page in the 'Company Settings' section of the 'Setup' that contains 'My Domain' settings.
 */
public class MyDomainPage extends IframePage {

    public final SelenideElement editPoliciesButton = $("[id$='editPoliciesButton']");
    public final SelenideElement loginPolicyEditableCheckbox = $("[id$='requireLoginCheckbox']");
    public final SelenideElement loginPolicyImageCheckbox = $(byText("Login Policy")).parent().$("img");
    public final SelenideElement savePolicyChangesButton = $("input[value='Save']");

    public MyDomainPage() {
        super("My Domain Settings ~ Salesforce");
    }

    /**
     * Open 'Setup - Company Settings - My Domain' page via direct link using Base URL.
     * <p> Note: contents for Base URL are usually provided via system properties. </p>
     *
     * @return opened My Domain Page reference
     */
    public MyDomainPage openPage() {
        open(BASE_URL + "/lightning/setup/OrgDomain/home");
        switchToIFrame();
        return this;
    }
}
