package page.lead;

import page.salesforce.VisualforcePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Base class for Sales and Partner Lead Creation Page classes.
 * Contains common elements of Sales and Partner Lead Creation pages.
 */
public abstract class BaseLeadCreationPage extends VisualforcePage {

    public final SelenideElement firstName = $("[id$='firstName2']");
    public final SelenideElement lastName = $("[id$='lastName2']");
    public final SelenideElement companyName = $("[id$='companyName2']");
    public final SelenideElement title = $("[id$='Title']");
    public final SelenideElement emailAddress = $("[id$='emailAddress2']");
    public final SelenideElement phoneNumber = $("[id$='contactNumber']");
    public final SelenideElement rangeOfEmployeesPicklist = $("[id$='NumberOfEmployeesR']");
    public final SelenideElement numberOfLocationsPicklist = $("[id$='NumberOfLocation']");
    public final SelenideElement website = $("[id$='Website']");
    public final SelenideElement industryPicklist = $("[id$='industry']");
    public final SelenideElement searchSpinner = $("div.slds-spinner_container");
    public final SelenideElement createNewLeadButton = $("[value='Create New Lead']");

    /**
     * Constructor for Sales/Partner Lead Creation page with iframe's title.
     * Defines Sales/Partner Lead Creation page location.
     */
    public BaseLeadCreationPage(String iframeTitleSubstring) {
        super(iframeTitleSubstring);
    }
}
