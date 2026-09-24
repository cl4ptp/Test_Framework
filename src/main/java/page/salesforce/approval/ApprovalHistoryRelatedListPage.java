package page.salesforce.approval;

import page.salesforce.GenericSalesforceModal;
import page.salesforce.ListViewPage;
import page.salesforce.approval.modal.ApproveApprovalModal;
import com.sforce.soap.enterprise.sobject.Approval__c;

import static com.codeborne.selenide.Selenide.$;

/**
 * Separate page for "Approval History" related records of {@link Approval__c} record.
 */
public class ApprovalHistoryRelatedListPage extends ListViewPage {
    public static final String APPROVE_BUTTON_LABEL = "Approve";

    //  Modal windows
    public final ApproveApprovalModal approvalModal = new ApproveApprovalModal();
    public final GenericSalesforceModal approveNotificationModal =
            new GenericSalesforceModal($(".uiModal.open div.slds-modal__container"));
}
