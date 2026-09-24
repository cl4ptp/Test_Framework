package page.opportunity.ngbsquotingwizard.quotetab;

import model.ngbs.testdata.AreaCode;
import page.components.*;
import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

/**
 * Quote tab in {@link NGBSQuotingWizardPage}
 * that contains some useful fields and picklists,
 * {@link ShippingAddressForm}, {@link Calendar} and {@link AreaCodeSelector}
 * for Main Area Code and Fax Area Code;
 * 'Save', 'Discard' and 'Generate PDF' buttons
 */
public class QuotePage extends NGBSQuotingWizardPage {

    //  Error Message
    public static final String FIELD_IS_REQUIRED_ERROR = "This field is required";

    //  Info message
    public static final String LINK_OFFICE_ACCOUNT_TO_SET_UP_CONTRACT_TERMS_MESSAGE =
            "In order to set up contract terms please link with Office Account using \"Manage Account Bindings\"";

    // Picklists values
    public static final String CREDIT_CARD_PAYMENT_METHOD = "Credit Card";
    public static final String INVOICE_PAYMENT_METHOD = "Invoice";
    public static final String NONE_PAYMENT_METHOD = "--None--";

    public final SelenideElement loadingMessage = $("quote-details").$(byText("loading..."));

    public final SelenideElement quoteName = $("[data-ui-auto='quote-name']");
    public final SelenideElement expirationDateInput = $("[data-ui-auto='expiration-date']");
    public final SelenideElement stageInfoTooltip = $("[for='quote-type'] info");
    public final SelenideElement stagePicklist = $("[data-ui-auto='quote-type']");
    public final SelenideElement agreementStatusPicklist = $("[data-ui-auto='agreement-status']");
    public final SelenideElement contractTermsInfoPlaceholder = $(".contract-term-info");
    public final SelenideElement initialTermPicklist = $("[data-ui-auto='initial-term']");
    public final SelenideElement initialTermSection = $(withText("Initial Term")).parent();
    public final SelenideElement renewalTermPicklist = $("[data-ui-auto='renewal-term']");
    public final SelenideElement autoRenewalCheckbox = $("[data-ui-auto='auto-renewal']");
    public final SelenideElement startDateInput = $("[data-ui-auto='start-date']");
    public final SelenideElement endDateInput = $("[data-ui-auto='end-date']");
    public final SelenideElement justificationDescriptionTextArea = $("[data-ui-auto='justification']");
    public final SelenideElement specialTermsPicklist = $("[data-ui-auto='special-term']");
    public final SelenideElement freeServiceCreditAmount = $("#fscAmount");
    public final SelenideElement fullMRSToggle = $(byText("Full MRS"));
    public final SelenideElement intendedPaymentMethodPicklist = $("#payment-method");
    public final ElementsCollection intendedPaymentMethodOptions = intendedPaymentMethodPicklist.$$("option");

    //  Area Code section
    public final SelenideElement mainAreaCodeInput = $("[datauiautoinput='main-area-code']");
    public final SelenideElement mainAreaCodeError = mainAreaCodeInput.$(".slds-form-element__help");
    public final SelenideElement faxAreaCodeInput = $("[datauiautoinput='fax-area-code']");
    public final SelenideElement faxAreaCodeError = faxAreaCodeInput.$(".slds-form-element__help");
    private final By clearButton = byCssSelector("[iconname='close']");
    public final SelenideElement clearFaxAreaCodeButton = faxAreaCodeInput.$(clearButton);

    public final SelenideElement shippingAddressTextArea = $("[data-ui-auto='shipping-address']");
    public final SelenideElement selfProvisionedCheckbox = $("[data-ui-auto='self-provisioned']");
    public final SelenideElement provisioningDetailsTextArea = $("[data-ui-auto='provisioning-details']");

    //  Buttons
    public final SelenideElement discardButton = $("[data-ui-auto='quote-discard']");
    public final SelenideElement saveButton = $("[data-ui-auto='quote-save']");
    public final SelenideElement generatePDFButton = $("[data-ui-auto='quote-generate-pdf']");

    //  Shipping form
    public final ShippingAddressForm shippingAddressForm = new ShippingAddressForm();

    //  Calendar
    public final Calendar calendar = new Calendar();

    //  Area Code selector
    public final AreaCodeSelector areaCodeSelector = new AreaCodeSelector();

    /**
     * Open Quote tab by clicking on the tab's button.
     */
    public QuotePage openTab() {
        quoteTabButton.click();
        waitUntilLoaded();
        return this;
    }

    /**
     * Wait until the page is fully loaded.
     * User may safely interact with any of the page's elements after this method is finished.
     */
    public void waitUntilLoaded() {
        unsavedChangesDialog.backdrop.shouldBe(hidden);
        loadingMessage.shouldBe(hidden, ofSeconds(30));
        quoteName.shouldBe(visible, ofSeconds(30));
    }

    /**
     * Set current date value to 'Start Date' field.
     */
    public void setDefaultStartDate() {
        startDateInput.click();
        calendar.setTodayDate();
    }

    /**
     * Set value for 'Default Area Code' input field with provided {@link AreaCode} object.
     * Used in Existing Business Opportunities.
     *
     * @param areaCode {@link AreaCode} object with values for area code: country name, state name, city name
     */
    public void setDefaultAreaCode(AreaCode areaCode) {
        setMainAreaCode(areaCode);
    }

    /**
     * Set value for 'Main Area Code' input field with provided {@link AreaCode} object.
     * Used in New Business Opportunities.
     *
     * @param areaCode {@link AreaCode} object with values for area code:
     *                 country name, state name, city name
     */
    public void setMainAreaCode(AreaCode areaCode) {
        areaCodeSelector.selectCode(areaCode);
    }

    /**
     * Set value for 'Fax Area Code' input field with provided {@link AreaCode} object.
     * Used in New Business Opportunities.
     *
     * @param areaCode {@link AreaCode} object with values for area code:
     *                 country name, state name, city name
     */
    public void setFaxAreaCode(AreaCode areaCode) {
        var areaCodeSelector = new AreaCodeSelector(faxAreaCodeInput);
        areaCodeSelector.selectCode(areaCode);
    }

    /**
     * Press 'Save' button on the Quote Tab of Quoting Wizard.
     */
    public void saveQuote() {
        saveButton.scrollIntoView(true).click();
        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(60));
        waitUntilLoaded();
    }
}
