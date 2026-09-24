package page.opportunity.modal;

import page.components.lookup.StandardLwcLookupComponent;
import page.opportunity.OpportunityRecordPage;
import page.salesforce.RecordCreationModal;
import page.salesforce.account.AccountRecordPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Approval__c;

/**
 * Base class for {@link Approval__c} creation modal window.
 * <br/>
 * Used for creating different types of Approvals for Accounts.
 * Contains some common fields and buttons for every type of Approval modal window creation,
 * like 'Account' search input, 'Save' button etc.
 * <br/>
 * <b> Note: Might be opened from {@link AccountRecordPage} or {@link OpportunityRecordPage} pages. </b>
 */
public abstract class ApprovalCreationModal extends RecordCreationModal {

    //  Elements
    public final SelenideElement approvalNameInput =
            dialogContainer.$x(".//label[contains(text(),'Approval Name')]/following-sibling::div/input");
    public final StandardLwcLookupComponent accountSearchInput =
            new StandardLwcLookupComponent(dialogContainer.$x(".//*[./label[text()='Account']]//div[./div/input]"));

    /**
     * Constructor for {@link Approval__c} record creation modal window
     * located via its header's title that includes the active record type.
     *
     * @param approvalRecordType approval's record type (e.g. "KYC Approval Request")
     */
    public ApprovalCreationModal(String approvalRecordType) {
        super("New Approval: " + approvalRecordType);
    }
}
