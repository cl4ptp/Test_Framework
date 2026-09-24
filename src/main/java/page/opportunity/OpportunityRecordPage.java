package page.opportunity;

import page.opportunity.modal.*;
import page.salesforce.RecordPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static page.opportunity.WizardBodyPage.WIZARD_IFRAME_DEFAULT;
import static page.opportunity.WizardBodyPage.WIZARD_IFRAME_PRO_SERV;
import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static org.apache.commons.lang3.StringUtils.abbreviate;


public class OpportunityRecordPage extends RecordPage {

    //  Related lists' headers
    public static final String APPROVALS_RELATED_LIST = "Approvals";

    //  Error messages
    public static final String PRIMARY_QUOTE_HAS_ERRORS_ON_SIGN_UP_ERROR = "SignUp failed\n" +
            "Primary quote has errors";
    public static final String AREA_CODES_ARE_MISSING_ERROR = "SignUp failed\n" +
            "Area Codes are missing. Please select Area Codes for all Items in the Cart of the Primary Quote that are eligible for Area Code assignment";
    public static final String ACCOUNT_IS_ALREADY_SIGNED_UP_ERROR = "Related Account is already signed up\n" +
            "Existing customers aren't allowed to sign up";
    public static final String AREA_CODES_ARE_MISSING_TO_CLOSE_OPPORTUNITY_ERROR = "You can't Close this Opportunity\n" +
            "Area Codes are missing. Please select Area Codes for all Items in the Cart of the Primary Quote that are eligible for Area Code assignment";
    public static final String ERRORS_ON_QUOTE_ERROR = "Primary quote has errors\n" +
            "Make sure that all errors on primary quote are resolved. Only after that you can close opportunity.";
    public static final String QUOTE_IS_NOT_APPROVED_ERROR = "Primary quote is not approved\n" +
            "The Opportunity cannot be Closed Won because the Primary Quote does not contain the required Approvals";
    public static final String QUOTE_IS_INVALID_ERROR = "You can't Close this Opportunity\n" +
            "Primary Quote is invalid and doesn't reflect current Account Status. You have to create new Quote to be able to close this Opportunity";
    public static final String QUOTES_DO_NOT_EXIST_ERROR = "The opportunity doesn't have any sales agreements\n" +
            "To close the opportunity, please, create at least one sales agreement";
    public static final String RENTAL_PHONES_REQUIRE_CONTRACT_ERROR = "Rental phones require a long-term contract\n" +
            "Quote with 24+ months initial term is required to sign up with rental phones";
    public static final String ACTIVE_AGREEMENT_IS_REQUIRED_ERROR = "You can't Close this Opportunity\n" +
            "You need an Active Agreement to Close this Opportunity.";
    public static final String ACTIVE_AGREEMENT_IS_REQUIRED_ON_SIGN_UP_ERROR = "Opportunity can't be Signed Up\n" +
            "You need an Active Agreement to Sign Up that Opportunity";
    public static final String RELATED_ACCOUNT_IS_ALREADY_SIGNED_UP_ERROR = "Related Account is already signed up\n" +
            "Existing customers aren't allowed to sign up";
    public static final String NEED_COMPLETED_ENVELOPE_ERROR = "SignUp failed\n" +
            "You need to have Completed Envelope to Sign Up the Account";
    public static final String EMPTY_BILLING_ADDRESS_ERROR = "Account's Billing Address is incomplete\n" +
            "Corresponding fields must be specified:\n" +
            "Billing Street\n" +
            "Billing City\n" +
            "Billing Zip/Postal Code\n" +
            "Billing Country";
    public static final String EMPTY_STATE_BILLING_ADDRESS_ERROR = "Account's Billing Address is incomplete\n" +
            "Corresponding fields must be specified:\n" +
            "Billing State/Province";
    public static final String ONLY_BILLING_COUNTRY_POPULATED_ERROR = "Account's Billing Address is incomplete\n" +
            "Corresponding fields must be specified:\n" +
            "Billing Street\n" +
            "Billing City\n" +
            "Billing State/Province\n" +
            "Billing Zip/Postal Code";
    public static final String MASTER_ACCOUNT_IS_NOT_PAID_ERROR = "You can't Close this Opportunity\n" +
            "Master account is not Paid or Contact Roles mismatch";
    public static final String LINKED_BOUND_ACCOUNT_SHOULD_BE_RINGCENTRAL_MVP_ERROR = "You can't Close this Opportunity\n" +
            "Linked Bound Account should be RingCentral MVP";
    public static final String INVOICE_IS_REQUIRED_FOR_CLOSE_ERROR = "You can't Close this Opportunity\n" +
            "Invoice payment method is required";
    public static final String INVOICE_IS_REQUIRED_FOR_ENGAGE_SIGNUP_ERROR = "Sign Up is not allowed.\n" +
            "Invoice payment method is required";
    public static final String INVOICE_IS_REQUIRED_FOR_MASTER_CLOSE_ERROR = "You can't Close this Opportunity\n" +
            "Invoice payment method is required on related Office Account for Engage Accounts.";
    public static final String AT_LEAST_ONE_QUOTE_IS_REQUIRED_ERROR = "SignUp failed\n" +
            "At least one quote on the opportunity is required to sign up a customer";
    private static final String UNABLE_TO_CLOSE_RECORD_ERROR =
            "Unable to close record\n" +
                    "Please contact %s to recall the Legal Engagement process before closing the opportunity";
    public static final String LINKED_MASTER_ACCOUNT_SHOULD_BE_SIGNED_UP_ERROR = "Linked Master Account should be signed Up first\n" +
            "Please Sign Up this Master Account before proceeding";
    public static final String REVIEW_LINKED_ELA_SERVICE_ACCOUNTS_ERROR = "Sorry, can't sign up this Opportunity at the moment.\n" +
            "Please, review linked Service ELA Accounts in Account Viewer for next action.";
    public static final String SUBMIT_INVOICE_ON_BEHALF_REQUEST_APPROVAL_ERROR = "You can't Close this Opportunity\n" +
            "Please, submit Invoice-on-behalf request approval first";
    public static final String SIGNUP_IS_NOT_AVAILABLE_FOR_VODAFONE_ERROR = "The sign-up is unavailable for the Vodafone Business with RingCentral brand.\n" +
            "Please use the Quoting Tool for Professional Services only";
    public static final String ACCOUNT_SHOULD_HAVE_PAYMENT_METHOD_ERROR = "Sign Up is not allowed.\n" +
            "Account should have predefined payment method to proceed with Sign Up.";
    public static final String INVOICE_ON_BEHALF_CREATED_MESSAGE
            = "Approval \"Invoice-on-behalf Request - %s\" was created.";
    public static final String USER_PROFILE_NOT_PERMITTED_TO_SIGN_UP_ERROR = "Your user profile is not permitted to execute Sign Up\n" +
            "Please reach out to Engage Order Desk";
    public static final String MASTER_ACCOUNT_WASNT_BOUND_ERROR = "You can't Close this Opportunity\n" +
            "Master Account wasn't bound. Master Account should be linked";
    public static final String APPROVED_KYC_APPROVAL_REQUIRED_ERROR = "You can't Close this Opportunity\n" +
            "You need an approved KYC request to Close an opportunity";
    public static final String NEED_APPROVED_KYC_REQUEST_ERROR = "SignUp failed\n" +
            "You need an approved KYC request to Sign Up an opportunity";
    public static final String ACCOUNT_SHOULD_HAVE_APPROVED_INVOICING_REQUEST_ERROR = "Sign Up is not allowed.\n" +
            "Account should have approved Invoicing Request Approval to proceed with Sign Up.";
    public static final String INVOICING_APPROVAL_SHOULD_BE_APPROVED_ERROR = "Sign Up is not allowed.\n" +
            "Invoicing approval request should be Approved";
    public static final String OBTAIN_INVOICE_PAYMENT_APPROVAL_ERROR = "Sign Up is not allowed.\n" +
            "You won’t be able to sign up this account, please, obtain Invoice payment approval first.";

    //  Tabs
    public final SelenideElement quoteTab = $x("//li[@title='Quote']");
    public final SelenideElement approvalTab = $x("//li[@title='Approval']");

    //  Alerts
    public final SelenideElement alertNotificationBlock = $("[role='alert'][class*='error']");
    public final ElementsCollection notifications = $$("[role='alert'][class*='error'] > div");
    public final SelenideElement alertCloseButton = alertNotificationBlock.$("button");

    //  Quote Wizard
    public WizardBodyPage wizardBodyPage = new WizardBodyPage();

    //  Modal windows
    public final SignUpDialogModal signUpDialog = new SignUpDialogModal();
    public final SyncWithNGBSModal syncWithNGBSModal = new SyncWithNGBSModal();
    public final RecallEngageLegalApprovalModal recallEngageLegalApprovalModal = new RecallEngageLegalApprovalModal();
    public final AccountViewerModal accountViewerModal = new AccountViewerModal();
    public final NewApprovalRecordTypeSelectionModal newApprovalRecordTypeSelectionModal = new NewApprovalRecordTypeSelectionModal();
    public final InvoiceOnBehalfApprovalCreationModal invoiceOnBehalfApprovalCreationModal =
            new InvoiceOnBehalfApprovalCreationModal();

    /**
     * Get the error message that Opportunity can't be closed because of the existing Legal Engagement process.
     *
     * @param approvalSubmitterName expected full name of Engage Legal Approval submitter.
     * @return error message that record can't be closed because of the existing Legal Engagement process.
     * (e.g. <i>"Unable to close record
     * Please contact Kristin Cooper to recall the Legal Engagement process before closing the opportunity."</i>)
     */
    public static String getUnableToCloseRecordError(String approvalSubmitterName) {
        return String.format(UNABLE_TO_CLOSE_RECORD_ERROR, approvalSubmitterName);
    }

    /**
     * Get standard success notification message on Opportunity Record Page
     * after Invoice-on-Behalf Approval is created.
     *
     * @param opportunityName name of the Opportunity for which Approval was created
     * @return notification message about Invoice-on-Behalf Approval Creation
     * (e.g. <i>"Approval "Invoice-on-behalf Request - OpportunityName" was created."</i>)
     */
    public static String getInvoiceOnBehalfApprovalCreatedMessage(String opportunityName) {
        var shortenedOpportunityName = abbreviate(opportunityName, 52);
        return String.format(INVOICE_ON_BEHALF_CREATED_MESSAGE, shortenedOpportunityName);
    }

    /**
     * {@inheritDoc}
     */
    public void waitUntilLoaded() {
        detailsTab.shouldBe(visible, ofSeconds(100));
        visibleLightingActionButtons.shouldHave(sizeGreaterThanOrEqual(3), ofSeconds(100));
    }

    /**
     * Open 'Quote' tab on the Opportunity record page and switch to NGBS Quoting Wizard iFrame.
     * Use it for any user except the user with profile = 'Professional Services Lightning'.
     *
     * @see #switchToNGBSQWAsProServ()
     */
    public void switchToNGBSQW() {
        quoteTab.shouldBe(enabled, ofSeconds(80)).click(usingJavaScript());
        switchToNGBSQWIframe(WIZARD_IFRAME_DEFAULT);
    }

    /**
     * Open 'Details' tab on the Opportunity record page and switch to NGBS Quoting Wizard iFrame.
     * Use it for a user with profile = 'Professional Services Lightning'.
     *
     * @see #switchToNGBSQW()
     */
    public void switchToNGBSQWAsProServ() {
        detailsTab.click(usingJavaScript());
        switchToNGBSQWIframe(WIZARD_IFRAME_PRO_SERV);
    }

    /**
     * Switch to the provided iFrame element that represents the Quoting Wizard.
     *
     * @param frameElement iFrame Web Element to switch
     */
    public void switchToNGBSQWIframe(SelenideElement frameElement) {
        wizardBodyPage = new WizardBodyPage(frameElement);
        wizardBodyPage.switchToIFrame();
        wizardBodyPage.mainQuoteWizardPage.waitUntilLoaded();
    }

    /**
     * Open 'Quote' tab on the Opportunity record page and switch to NGBS Quoting Wizard iFrame.
     * Use it for Opportunities that do not support Quoting.
     */
    public void switchToNGBSQWIframeWithoutQuote() {
        quoteTab.shouldBe(enabled, ofSeconds(80)).click(usingJavaScript());
        wizardBodyPage = new WizardBodyPage(WIZARD_IFRAME_DEFAULT);
        wizardBodyPage.switchToIFrame();
    }

    /**
     * Switch the current context away from the Quote Wizard
     * back to the actual record page.
     * <br/>
     * It's necessary because the current implementation places
     * the Quote Wizard in the separate iframe inside the Opportunity record page.
     * <br/>
     * Useful if the test needs to interact with record page elements
     * (e.g. lightning buttons) after performing actions in Quote Wizard.
     */
    public void switchToRecordPageFromNGBSQW() {
        wizardBodyPage.switchFromIFrame();
    }

    /**
     * Open Create New Approval Modal window from 'Approval' tab.
     */
    public void openCreateNewApprovalModal() {
        approvalTab.click(usingJavaScript());
        clickHiddenListButtonOnRelatedList(APPROVALS_RELATED_LIST, NEW_BUTTON_LABEL);
    }

    /**
     * Click on 'Sync with NGBS' button.
     * <p></p>
     * This method searches "Sync with NGBS" button among Lightning Experience actions
     * in the upper right corner of the page
     * (even if the button is hidden in the "show more actions" list).
     */
    public void clickSyncWithNGBSButton() {
        switchToRecordPageFromNGBSQW();
        clickDetailPageButton("Sync with NGBS");
    }

    /**
     * Click on 'Sign Up' button.
     * <p></p>
     * This method searches "Sign Up" button among Lightning Experience actions
     * in the upper right corner of the page
     * (even if the button is hidden in the "show more actions" list).
     */
    public void clickSignUpButton() {
        switchToRecordPageFromNGBSQW();
        clickDetailPageButton("Sign up");
    }

    /**
     * Click on 'Close' button.
     * <p></p>
     * This method searches "Close" button among Lightning Experience actions
     * in the upper right corner of the page.
     */
    public void clickCloseButton() {
        switchToRecordPageFromNGBSQW();
        clickDetailPageButton("Close");
    }

    /**
     * Close all appearing error alert notifications on {@link OpportunityRecordPage}
     * if they are not closed immediately.
     */
    public void closeErrorAlertNotifications() {
        notifications.forEach(n -> n.parent().$("button").click());
    }
}