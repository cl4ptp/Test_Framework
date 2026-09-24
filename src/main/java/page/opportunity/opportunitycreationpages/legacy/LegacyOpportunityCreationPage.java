package page.opportunity.opportunitycreationpages.legacy;

import page.components.Calendar;
import page.components.lookup.AngularLookupComponent;
import page.opportunity.opportunitycreationpages.OpportunityCreationPage;
import utilities.Constants;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Area_Codes__c;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static utilities.salesforce.sobjecthelper.AreaCodeHelper.getFullName;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class LegacyOpportunityCreationPage extends OpportunityCreationPage {

    //  Calendar
    private final Calendar calendar = new Calendar();

    public SelenideElement heading = $x("//h1[text()='New Opportunity']");

    // Account selection
    public AngularLookupComponent accountComboboxInput = new AngularLookupComponent($x("//label[contains(text(),'Account')]/parent::div[@class='slds-form-element']"));
    public SelenideElement selectedAccount = $x("//label[contains(text(),'Account')]/following-sibling::div//a");
    // Contact selection
    public SelenideElement contactComboboxInput = $x("//label[contains(text(),'Contact')]/following-sibling::div//input");
    public SelenideElement selectedContact = $x("//label[contains(text(),'Contact')]/following-sibling::div//a");
    // Opportunity info
    public SelenideElement opportunityNameTextInput = $x("//label[contains(text(),'Opportunity Name')]/following-sibling::div/input");
    public SelenideElement closeDateTextInput = $("[data-ui-auto='close-date']");

    public SelenideElement leadSourcePicklist = $x("//label[span/text()='Lead Source']/following-sibling::div/div/select");
    public SelenideElement typePicklist = $x("//label[span/text()='Type']/following-sibling::div/div/select");
    // Details
    public SelenideElement shippingAddressSection = $x("//p[text() = 'Shipping Address']/following-sibling::div");
    public SelenideElement shippingAddressApplyButton = $x("//button[text() = 'Apply']");
    public SelenideElement shippingAddressUseAccountAddressCheckbox = $x("//span[text()='Use Account Billing Address']/preceding-sibling::span[@class='slds-checkbox_faux']");
    public SelenideElement shippingAddressCountry = $x("//label[text()='Country']/following-sibling::div/input");
    public SelenideElement shippingAddressCity = $x("//label[text()='City']/following-sibling::div/input");
    public SelenideElement shippingAddressState = $x("//label[text()='State']/following-sibling::div/input");
    public SelenideElement shippingAddressAddressLine = $x("//label[text()='Address Line']/following-sibling::div/input");
    public SelenideElement shippingAddressZip = $x("//label[text()='Zip Code']/following-sibling::div/input");
    public SelenideElement shippingAddressInlineAddress = $x("//dd[@title='Address']");
    public SelenideElement shippingAddressErrorText = $x("//p[@class='slds-form-element__help' and contains(text(),'Shipping Address')]");
    public SelenideElement provisioningDetailsTextArea = $x("//label[contains(text(),'Provisioning Details')]/following-sibling::div/textarea");
    // Service Plan Selector
    public SelenideElement selectedServicePlan = $x("//p[contains(text(),'Service Plan')]/following-sibling::p[contains(@class, ('slds-text-body--regular'))]");
    public SelenideElement brandPicklist = $x("//label[span/text() = 'Brand']/following-sibling::div//select");
    public SelenideElement servicePicklist = $x("//label[span/text() = 'Service']/following-sibling::div//select");
    public SelenideElement editionPicklist = $x("//label[span/text() = 'Edition']/following-sibling::div//select");
    public SelenideElement planPicklist = $x("//label[span/text() = 'Plan']/following-sibling::div//select");
    public SelenideElement forecastedUsersTextInput = $x("//label[text() = 'Forecasted Users']/following-sibling::div/input");
    public SelenideElement existingUsersTextInput = $x("//label[contains(text(),'Existing Users')]/following-sibling::div/input");
    public SelenideElement newTotalUsersTextInput = $x("//label[contains(text(),'New Total Users')]/following-sibling::div//input");
    // Area Code selection
    public AngularLookupComponent areaCodeComboboxInput = new AngularLookupComponent($x("//label[contains(text(),'Area Code')]/parent::div[@class='slds-form-element']"));
    public SelenideElement selectedAreaCode = $x("//label[contains(text(),'Area Code')]/following-sibling::div//div[@class='slds-input slds-combobox__input']");
    // Add Products section
    private final SelenideElement addProductsSection = $x("//h3[contains(text(),'Add Products')]/ancestor::div[@class='slds-setup-assistant__step-summary']");
    public SelenideElement renderedProductsCounter = $x("//div[@class='slds-badge products-rendered']");
    public SelenideElement productNameTextInput = $x("//label[text() = 'Product Name']/following-sibling::div//input");
    public ElementsCollection productRows = $$x("//div[@class='slds-scrollable product-table']//tbody//tr");
    public ElementsCollection addProductsCategories = addProductsSection.$$x(".//div[@title='Category']");
    public ElementsCollection addProductsCheckboxes = addProductsSection.$$x(".//span[@class='slds-checkbox_faux']");
    public ElementsCollection addProductsNames = addProductsSection.$$x(".//div[@title='Product Name']");
    public ElementsCollection addProductsAreaCodeExpanders = addProductsSection.$$x(".//button[@title='Toggle details']");
    public ElementsCollection addProductsAddAreaCodeButtons = addProductsSection.$$x(".//button[contains(text(), 'Area Code')]");
    public ElementsCollection addProductsAreaCodeComboboxes = addProductsSection.$$x(".//input[@name='comboboxInput']");
    public ElementsCollection addProductsSelectedAreaCodes = addProductsSection.$$x(".//div[@class='slds-input slds-combobox__input' and string-length(text()) > 0]");
    // Buttons
    public SelenideElement discardButton = $x("//button[text()='Discard']");
    public SelenideElement continueToOppButton = $x("//button[text()='Continue to Opportunity']");
    public SelenideElement confirmAndCloseButton = $x("//button[text()='Confirm & Close']");
    // Spinners
    public SelenideElement entitlementUpdateSpinner = $x("//p[contains(text(), 'Updating Entitlements')]");
    public SelenideElement spinner = $x("//div[contains(@class,'slds-spinner')]");

    /**
     * Open Legacy "Quick Opportunity Page" by direct link.
     * This method allows to skip:
     * <p> 1. Opening corresponding account record </p>
     * <p> 2. Pressing "New" in Opportunity section </p>
     * <p> 3. Selecting "New Sales Opportunity" there </p>
     *
     * @return opened Legacy Opportunity Creation Page reference
     */
    public LegacyOpportunityCreationPage openPage() {
        open(OPPORTUNITY_CREATION_PAGE_URL);
        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(30));
        return this;
    }

    public void clearCombobox(String targetLabel) {
        $x("//label[contains(text(),'" + targetLabel + "')]/following-sibling::div//button[@title='Remove value']").click();
    }

    public LegacyOpportunityCreationPageProductRow getProductRowByProductName(String name) {
        return new LegacyOpportunityCreationPageProductRow($x("//tr[contains(@class, 'cOpportunityCreationFormProduct') and .//div[text() = '" + name + "']]"));
    }

    public LegacyOpportunityCreationPageProductRow getFirstProductRow() {
        return new LegacyOpportunityCreationPageProductRow(productRows.shouldHave(sizeGreaterThanOrEqual(1)).first());
    }

    public void checkHrefAttribute(SelenideElement elem, String expectedId) {
        elem.shouldHave(attribute("href", Constants.BASE_VF_URL + "/" + expectedId));
    }

    public void selectServicePlan(String brand, String service, String edition, String plan) {
        brandPicklist.selectOption(brand);
        servicePicklist.selectOption(service);
        editionPicklist.selectOption(edition);
        planPicklist.selectOption(plan);
    }

    public void quickCreateNewCustomerOpportunity(Account acc, Integer numberOfLines, Area_Codes__c areaCode) {
        quickCreateOpportunity(acc, acc.getName(), new Date(), numberOfLines, areaCode, false);
    }

    public void quickCreateUpsellOpportunity(Account acc, Integer numberOfLines, Area_Codes__c areaCode) {
        quickCreateOpportunity(acc, acc.getName(), new Date(), null, areaCode, true);
    }

    public void quickCreateOpportunity(Account acc, String oppName, Date closeDate,
                                       Integer numberOfLines,
                                       Area_Codes__c areaCode, Boolean isUpsell) {
        this.openPage();
        accountComboboxInput.selectItemInCombobox(acc.getName());
        opportunityNameTextInput.setValue(oppName);
        closeDateTextInput.setValue(
                new SimpleDateFormat("MMM d, yyyy", new Locale("en", "US"))
                        .format(closeDate)
        );

        if (!isUpsell) {
            forecastedUsersTextInput.setValue(numberOfLines.toString());
        }
        areaCodeComboboxInput.selectItemInCombobox(getFullName(areaCode));
        continueToOppButton.click();
    }

    public void populateCloseDate() {
        closeDateTextInput.shouldBe(visible, ofSeconds(30)).click();
        calendar.setTodayDate();
    }
}
