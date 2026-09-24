package page.opportunity.modal;

import page.AccountViewerPage;
import page.opportunity.OpportunityRecordPage;
import page.salesforce.GenericSalesforceModal;

import static com.codeborne.selenide.Selectors.byTitle;
import static com.codeborne.selenide.Selenide.$;

/**
 * Modal window in {@link OpportunityRecordPage}
 * shown when some of ELA Service Accounts isn't Signed Up.
 * <p>
 * This dialog manages Account Relations between Billable and Service ELA Accounts.
 * </p>
 */
public class AccountViewerModal extends GenericSalesforceModal {
    public final AccountViewerPage accountViewer = new AccountViewerPage();

    /**
     * Constructor that defines default location for the modal window
     * on the Opportunity record page.
     */
    public AccountViewerModal() {
        super($(".cOpportunity_signup .slds-modal__container"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeWindow() {
        this.dialogContainer.$(byTitle("close")).click();
    }
}
