package page.salesforce.account;

import page.opportunity.modal.NewApprovalRecordTypeSelectionModal;
import page.salesforce.RecordPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Account;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * The Salesforce page that displays Account({@link Account}) record information,
 * such as Account Name, linked Opportunities, linked Contacts etc.
 */
public class AccountRecordPage extends RecordPage {

    //  Related lists' headers
    public static final String OPPORTUNITIES_RELATED_LIST = "Opportunities";
    public static final String APPROVALS_RELATED_LIST = "Approvals";

    public final SelenideElement opportunitiesTab = $x("//li[@title='Opportunities']");
    public final SelenideElement approvalTab = $x("//li[@title='Approvals']");

    //  Modal windows
    public final NewApprovalRecordTypeSelectionModal newApprovalRecordTypeSelectionModal = new NewApprovalRecordTypeSelectionModal();

    /**
     * {@inheritDoc}
     */
    public void waitUntilLoaded() {
        detailsTab.shouldBe(visible, ofSeconds(100));
        visibleLightingActionButtons.shouldHave(sizeGreaterThanOrEqual(3), ofSeconds(100));
    }

    /**
     * Open Create New Approval Modal window from 'Approval' tab.
     */
    public void openCreateNewApprovalModal() {
        approvalTab.click();
        clickHiddenListButtonOnRelatedList(APPROVALS_RELATED_LIST, NEW_BUTTON_LABEL);
    }
}
