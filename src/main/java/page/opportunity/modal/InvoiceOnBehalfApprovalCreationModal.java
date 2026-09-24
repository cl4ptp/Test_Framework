package page.opportunity.modal;

import page.components.LightningCombobox;
import page.components.lookup.StandardLwcLookupComponent;
import page.opportunity.OpportunityRecordPage;
import page.salesforce.account.AccountRecordPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Approval__c;

import static utilities.salesforce.sobjecthelper.ApprovalHelper.INVOICE_ON_BEHALF_REQUEST_RECORD_TYPE;

/**
 * {@link Approval__c} creation modal of 'Invoice-on-Behalf Request' approval type.
 * <br/>
 * Contains some type-related fields, like 'Invoice Terms', address fields, 'Potential Users' etc.
 *
 * <b> Note: Might be opened from {@link AccountRecordPage} or {@link OpportunityRecordPage} pages. </b>
 */
public class InvoiceOnBehalfApprovalCreationModal extends ApprovalCreationModal {

    //  Error messages
    public static final String INCORRECT_ACCOUNTS_PAYABLE_CONTACT_ERROR =
            "Incorrect Contact is selected. Please select Accounts Payable Contact";
    public static final String INCORRECT_PARTNER_ACCOUNTS_PAYABLE_CONTACT_ERROR =
            "Incorrect Contact is selected. Please select Partner's Accounts Payable Contact";

    //  Field labels
    public static final String INVOICE_TERMS_LABEL = "Invoice Terms";
    public static final String PAYMENT_TERMS_LABEL = "Payment Terms";

    //  Fields
    public final LightningCombobox invoiceTermsPicklist = new LightningCombobox(INVOICE_TERMS_LABEL);
    public final LightningCombobox paymentTermsPicklist = new LightningCombobox(PAYMENT_TERMS_LABEL);
    public final SelenideElement potentialUsersInput =
            dialogContainer.$x(".//label[contains(text(),'Potential Users')]/following-sibling::div/input");
    public final SelenideElement reasonCustomerRequestInvoicingInput =
            dialogContainer.$x(".//label[contains(text(),'Reason Customer is Requesting Invoicing')]/following-sibling::div/textarea");
    public final SelenideElement whyRCShouldInvoiceInput =
            dialogContainer.$x(".//label[contains(text(),'Why RingCentral Should Invoice Customer')]/following-sibling::div/textarea");
    public final SelenideElement initialUsersInput =
            dialogContainer.$x(".//label[contains(text(),'Initial Number of Users')]/following-sibling::div/input");
    public final SelenideElement initialDevicesInput =
            dialogContainer.$x(".//label[contains(text(),'Initial Number of Devices')]/following-sibling::div/input");
    public final SelenideElement pricePerUserInput =
            dialogContainer.$x(".//label[contains(text(),'Price per User')]/following-sibling::div/input");
    public final SelenideElement companyNameInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Company Name - Head Office')]/following-sibling::div/input");
    public final SelenideElement streetInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Physical Address Street')]/following-sibling::div/input");
    public final SelenideElement zipCodeInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Physical Address Zip Code')]/following-sibling::div/input");
    public final SelenideElement cityInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Physical Address City')]/following-sibling::div/input");
    public final SelenideElement stateInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Physical Address State/Province')]/following-sibling::div/input");
    public final SelenideElement countryInput =
            dialogContainer.$x(".//label[contains(text(),'Legal Physical Address Country')]/following-sibling::div/input");

    public final StandardLwcLookupComponent opportunitySearchInput =
            new StandardLwcLookupComponent(dialogContainer.$x(".//*[./label[text()='Opportunity']]//div[./div/input]"));
    public final StandardLwcLookupComponent accountsPayableContactSearchInput =
            new StandardLwcLookupComponent(dialogContainer.$x(".//*[./label[text()='Accounts Payable Contact']]//div[./div/input]"));
    public final StandardLwcLookupComponent partnerAccountsPayableContactSearchInput =
            new StandardLwcLookupComponent(dialogContainer.$x(".//*[./label[text()=\"Partner's Accounts Payable Contact\"]]//div[./div/input]"));

    /**
     * Constructor for the modal window to locate it via its default header.
     */
    public InvoiceOnBehalfApprovalCreationModal() {
        super(INVOICE_ON_BEHALF_REQUEST_RECORD_TYPE);
    }
}
