package page.opportunity.legacyquotingwizard;

import page.components.LegacyDatePicker;
import page.opportunity.legacyquotingwizard.modal.EngageContactCenterProServModal;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

public class LegacyQuotingWizardPage {

    public static final String CONTACT_CENTER_NOT_AVAILABLE_MESSAGE =
            "CONTACT CENTER IS NOT AVAILABLE FOR THE PACKAGE SELECTED ON YOUR PRIMARY QUOTE";
    public static final String CONTACT_CENTER_QUOTE_CANNOT_BE_CREATED_MESSAGE =
            "CONTACT CENTER QUOTE CANNOT BE CREATED WHEN PRIMARY QUOTE IN AGREEMENT STAGE";

    public static final String NEW_CUSTOMER_QUOTE_TYPE = "New Customer";

    //  For 'Quotes' picklist
    public static final String NEW_QUOTE_OPTION = "--New Quote--";

    public SelenideElement quotesPicklist = $x("//div[label[text()='Quotes']]//select");

    // Top buttons
    public SelenideElement newQuoteButton = $x("//button[text()='New Quote']");
    public SelenideElement makePrimaryButton = $x("//button[text()='Primary Quote']");
    public SelenideElement engageLegalButton = $x("//button[text()='Engage Legal']");
    public SelenideElement engageProServButton = $x("//button[text()='Engage ProServ']");
    public SelenideElement engageCCProServButton = $x("//button[text()='Engage CC ProServ']");
    public SelenideElement cancelProServEngagementButton = $x("//button[text()='Cancel ProServ Engagement']");
    public SelenideElement sendWithDocuSignButton = $x("//button[text()='Send with DocuSign']");
    public SelenideElement deleteButton = $x("//button[text()='Delete']");
    public SelenideElement cancelCCProServEngagementButton = $x("//button[text()='Cancel CC ProServ Engagement']");
    public SelenideElement createPOCApprovalButton = $x("//button[text()='Create POC Approval']");

    // Quote info
    public SelenideElement quoteType = $x("//p[@title='Quote type']/following-sibling::p");
    public SelenideElement existingNumberOfDLs = $x("//p[@title='Number of Lines']/following-sibling::p");
    public SelenideElement approvalStatus = $x("//p[@title='Approval Status']/following-sibling::p");

    // Pipeline
    public SelenideElement servicePlansTabButton = $("[data-name='servicePlans']");
    public SelenideElement productsTabButton = $("[data-name='products']");
    public SelenideElement cartTabButton = $("[data-name='cart']");
    public SelenideElement phaseTabButton = $("[data-name='phases']");
    public SelenideElement quoteTabButton = $("[data-name='summary']");
    public SelenideElement agreementTabButton = $x("//li[.//span[text()='Agreement']]");

    // Bottom buttons
    public SelenideElement discardButton = $x("//button[text()='Discard']");
    public SelenideElement saveButton = $x("//button[text()='Save']");
    public SelenideElement addTaxesCartButton = $x("//button[text()='Add Taxes']");
    public SelenideElement removeTaxesCartButton = $x("//button[text()='Remove Taxes']");
    public SelenideElement generatePDFQuoteButton = $x("//button[text()='Generate PDF']");

    //  DatePicker
    public final LegacyDatePicker legacyDatePicker = new LegacyDatePicker();

    // Modal windows
    public SelenideElement discountModalWindow = $x(
            "//div[@class='slds-modal__container' " +
                    "and .//h2[text()='This change will require new Approval or will update Approvers List']]"
    );
    public SelenideElement saveAnywayButton = $x("//button[text()='Save changes anyway']");

    public final EngageContactCenterProServModal engageCCProServDialog = new EngageContactCenterProServModal();

    // Misc
    private final SelenideElement spinnerLoader = $x("//div[@id='wizard_loader']");
    public SelenideElement spinnerSave = $x("//p[@class='spinner-message slds-text-title--caps']");
    public SelenideElement quoteMessage = $("[data-name='cc'] p.slds-text-title_caps");

    public void waitUntilLoaded() {
        spinnerLoader.shouldBe(hidden, ofSeconds(30));
        spinnerSave.shouldBe(hidden, ofSeconds(30));
    }
}
