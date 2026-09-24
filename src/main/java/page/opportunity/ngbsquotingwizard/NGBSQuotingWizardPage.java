package page.opportunity.ngbsquotingwizard;

import page.opportunity.ngbsquotingwizard.modal.*;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static page.opportunity.ngbsquotingwizard.modal.EngageLegalRequestModal.AMENDMENT_ENGAGEMENT_TYPE;
import static utilities.StringHelper.TEST_STRING;
import static utilities.salesforce.sobjecthelper.QuoteHelper.QUOTE_TYPE_NEW;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class NGBSQuotingWizardPage {

    //  Notifications text
    public static final String ASSIGN_CORRECT_NUMBER_OF_LICENSES_TO_DL =
            "%1$s doesn't have equal number of assigned child licenses. " +
                    "Please assign correct number of child licenses to %1$s";
    public static final String THANK_YOU_FOR_REPORT_CASE_CREATED_MESSAGE =
            "Thank you for the report. Case is successfully created";

    public static final String PROSERV_QUOTE_DEFAULT_DETAILS =
            "ProServ Engagement Auto Test - Additional Details";
    public static final String PROVISIONING_IS_DISABLED =
            "Provisioning is disabled, all provisioning related data will be discarded.";
    public static final String AREA_CODES_AND_DEVICES_ASSIGNED =
            "Please make sure, that Phones with Area Codes are assigned to all Digital Lines";
    public static final String QUOTING_IS_UNAVAILABLE_MESSAGE =
            "Quoting is unavailable for the currently selected brand.";
    public static final String ENGAGE_PACKAGE_REQUIRES_INVOICE_REQUEST_APPROVAL =
            "This package requires Approved Invoice Request";
    public static final String CHOOSE_EXACTLY_ONE_LICENSE_WITH_SEATS_ON_DEMAND =
            "Choose exactly 1 license from Seats on demand";
    public static final String CHOOSE_EXACTLY_ONE_LICENSE_FROM_SEATS_WITH_MINIMUM_QUANTITY =
            "Choose exactly 1 license from Seats for Engage Digital with the minimum quantity of 3";
    public static final String ENGAGE_DIGITAL_STANDALONE_CONCURRENT_SEAT_QUANTITY =
            "Engage Digital Standalone concurrent seat based (USD) can only have between 3 and 99999 Engage Digital seats in total";

    //  Tooltips text
    public static final String TRIAL_POC_QUOTE_TOOLTIP = "Upgrade Trial/POC to Paid";

    //  'New Quote' dropdown options
    public static final String QUOTE_TYPE_SALES = "Sales Quote";
    public static final String QUOTE_TYPE_POC = "POC Quote";

    //  Approval Statuses
    public static final String APPROVAL_STATUS_REQUIRED = "Required";
    public static final String APPROVAL_STATUS_PENDING_L1 = "Pending L1 Approval";
    public static final String APPROVAL_STATUS_NOT_REQUIRED = "Not Required";
    public static final String APPROVAL_STATUS_APPROVED = "Approved";

    //  LBO Statuses
    public static final String PROVISION_TYPE_STATUS_NON_LBO = "non-LBO";
    public static final String PROVISION_TYPE_STATUS_LBO = "LBO";

    //  Spinner
    public SelenideElement spinner = $("[data-ui-auto='spinner']");
    public SelenideElement spinnerContainer = $(".slds-spinner_container");

    public final SelenideElement notification = $("notifications h2");

    //  Notification bar
    public SelenideElement notificationBar = $("[data-auto-ui='notification-bar']");

    //  Error notification (Toasts)
    public ElementsCollection notifications = $$("[data-auto-ui='notification-text']");

    //  Tabs
    public SelenideElement packageTabButton = $("[data-ui-auto='package-tab']");
    public SelenideElement productsTabButton = $("[data-ui-auto='products-tab']");
    public SelenideElement cartTabButton = $("[data-ui-auto='cart-tab']");
    public SelenideElement costCentersTabButton = $x("//li[.//span[contains(text(),'Cost Centers')]]");
    public SelenideElement quoteTabButton = $("[data-ui-auto='quote-tab']");

    public SelenideElement activeTab = $(".slds-is-active .slds-path__title");

    //  Blades
    public SelenideElement packageTabBlade = $("[data-ui-auto='package-blade']");
    public SelenideElement productsTabBlade = $("[data-ui-auto='product-blade']");
    public SelenideElement cartTabBlade = $("[data-ui-auto='cart-blade']");
    public SelenideElement quoteTabBlade = $("[data-ui-auto='quote-blade']");

    //  Quotes select
    private final By quotesPicklistSelector = By.cssSelector("[data-ui-auto='quote-picklist']");
    public SelenideElement quotesPicklist = $(quotesPicklistSelector);

    //  Quote operations
    public SelenideElement newQuoteButton = $("[data-ui-auto='new-quote']");
    public SelenideElement newSalesQuoteButton = $("[data-ui-auto='new-sales-quote']");
    public SelenideElement newPOCQuoteButton = $("[data-ui-auto='new-poc-quote']");
    public SelenideElement makePrimaryButton = $("[data-ui-auto='make-primary']");
    public SelenideElement deleteQuoteButton = $("[data-ui-auto='delete-quote']");
    public SelenideElement createPOCApprovalButton = $("#create-poc-approval-data-action");
    public SelenideElement submitForApprovalButton = $x("//span[text()='Submit For Approval']");
    public SelenideElement engageLegalButton = $("[data-ui-auto='engage-legal']");
    public SelenideElement engageProServButton = $("[data-ui-auto='engage-proserv']");
    public SelenideElement engageCCProServButton = $x("//button[text()='Engage CC ProServ']");
    public SelenideElement sendWithDocuSignButton = $("[data-ui-auto='send-with-docusign']");
    public final SelenideElement manageAccountBindingsButton = $x("//span[text()='Manage Account Bindings']");
    public final SelenideElement reportProblemButton = $("[data-ui-auto='create-case']");

    //  Quote type and Approval Status
    public SelenideElement billingSystem = $("[data-ui-auto='status-billing-system']");
    public SelenideElement quoteType = $("[data-ui-auto='status-quote-type']");
    public SelenideElement quoteTypeTooltip = quoteType.sibling(0).$("span");
    public SelenideElement approvalStatus = $("[data-ui-auto='status-approval-status']");
    public SelenideElement provisionType = $("[data-ui-auto='provision-type']");

    //  Modal windows
    public final UnsavedChangesWarningModal unsavedChangesDialog = new UnsavedChangesWarningModal();
    public final CreatePOCApprovalModal pocApprovalDialog = new CreatePOCApprovalModal();
    public final EngageProServModal engageProServDialog = new EngageProServModal();
    public final PDFGenerateModal pdfGenerateModal = new PDFGenerateModal();
    public final ConfigurePlaybooksModal configurePlaybooksPage = new ConfigurePlaybooksModal();
    public final AccountManagerModal manageAccountBindings = new AccountManagerModal();
    public final EngageLegalRequestModal engageLegalRequestModal = new EngageLegalRequestModal();
    public final CreateCaseModal createCaseModal = new CreateCaseModal();

    //  Pop-up notifications
    public final SelenideElement tooltip = $(".cdk-overlay-container [role='tooltip']");

    /**
     * Get picklist web element to select quote ('Quotes' dropdown list on the form).
     * <p></p>
     * Useful in case of {@link org.openqa.selenium.StaleElementReferenceException}.
     * In other cases, refer directly to {@link NGBSQuotingWizardPage#quotesPicklist}.
     *
     * @return 'Quotes' picklist as {@link SelenideElement}
     */
    public SelenideElement getQuotesPicklist() {
        $(quotesPicklistSelector).shouldBe(visible, ofSeconds(10));
        return $(quotesPicklistSelector);
    }

    /**
     * Get all options in 'Quotes' picklist as {@link ElementsCollection}.
     *
     * @return 'Quotes' picklist elements as {@link ElementsCollection}.
     */
    public ElementsCollection getQuotesPicklistOptions() {
        getQuotesPicklist().$("option").shouldBe(visible, ofSeconds(10));
        return getQuotesPicklist().$$("option");
    }

    /**
     * Get selected option from picklist web element to select quote ('Quotes' dropdown list on the form).
     * <p></p>
     * Useful in case of {@link org.openqa.selenium.StaleElementReferenceException}.
     * In other cases, refer directly to {@link SelenideElement#getSelectedOption()}
     * on {@link NGBSQuotingWizardPage#quotesPicklist}.
     *
     * @return currently selected option in 'Quotes' picklist as {@link SelenideElement}
     */
    public SelenideElement getQuotesPicklistSelectedOption() {
        getQuotesPicklist().$("option").shouldBe(visible, ofSeconds(10));
        return getQuotesPicklist().getSelectedOption();
    }

    /**
     * <p>Extract Quote ID for selected quote from value attribute</p>
     * <p></p>
     * <p>e.g. Quote ID = <b>0Q0550000001ztpCAA</b> for selected option in DOM: </p>
     * {@code <option value="42: 0Q0550000001ztpCAA">Some Quote Name</option>}
     * <p></p>
     * Method throws {@link RuntimeException} if 'value' attribute is empty or doesn't exist.
     *
     * @return string value of Quote ID
     */
    public String getSelectedQuoteId() {
        //  Additional explicit wait to evade possible StaleElementReferenceException
        //  in test classes that use this method
        Selenide.sleep(5_000);

        String valueAttribute = getQuotesPicklistSelectedOption().getAttribute("value");

        if (valueAttribute == null || valueAttribute.isBlank()) {
            throw new RuntimeException("Attribute 'value' for selected quote's web element is null/empty!");
        } else {
            return valueAttribute.replaceAll("\\d+:\\s*(.+)", "$1");
        }
    }

    /**
     * Wait until the page loads most of its important elements.
     * User may safely interact with any of the page's elements after this method is finished.
     */
    public void waitUntilLoaded() {
        quotesPicklist.shouldBe(visible, ofSeconds(120));
        spinner.shouldBe(hidden, ofSeconds(60));
        quotesPicklist.getSelectedOptions().shouldHave(sizeGreaterThan(0), ofSeconds(60));
    }

    /**
     * Create new 'Sales Quote' from Quote Wizard.
     * This method works for both New Business and Existing Business opportunities.
     */
    public void createNewSalesQuote() {
        if (quoteType.getText().equals(QUOTE_TYPE_NEW)) {
            newQuoteButton.hover();
            newSalesQuoteButton.click();
        } else {
            newQuoteButton.click();
        }

        spinner.shouldBe(hidden, ofSeconds(30));
    }

    /**
     * Create new 'POC Quote' from Quote Wizard.
     */
    public void createNewPOCQuote() {
        newQuoteButton.hover();
        newPOCQuoteButton.click();
        spinner.shouldBe(hidden, ofSeconds(30));
    }

    /**
     * Click on 'Engage ProServ' button and submit some default data for it (as additional details).
     */
    public void engageProServ() {
        engageProServButton.click();
        engageProServDialog.additionalDetailsInput.setValue(PROSERV_QUOTE_DEFAULT_DETAILS);
        engageProServDialog.submitButton.click();
        spinner.shouldBe(hidden, ofSeconds(20));
    }

    /**
     * Click on the 'Engage Legal' button and submit some default data in an opened modal window (Engagement Type,
     * Legal Account Name and Ask by Customer).
     *
     * @param accountName the value that will be set in Legal Account Name input
     */
    public void engageLegal(String accountName) {
        engageLegalButton.click();

        engageLegalRequestModal.legalEngagementTypeSelect.selectOption(AMENDMENT_ENGAGEMENT_TYPE);
        engageLegalRequestModal.legalAccountNameInput.setValue(accountName);
        engageLegalRequestModal.askByCustomerTextarea.setValue(TEST_STRING);
        engageLegalRequestModal.submitButton.click();

        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(60));
    }

    /**
     * Click 'Submit' button in Account Bindings modal window and wait until spinner is no longer displayed.
     */
    public void submitAccountBindingChanges() {
        manageAccountBindings.submitButton.shouldBe(enabled).click();
        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(60));
    }
}
