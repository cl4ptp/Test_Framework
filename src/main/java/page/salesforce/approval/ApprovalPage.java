package page.salesforce.approval;

import page.salesforce.RecordPage;
import com.sforce.soap.enterprise.sobject.Approval__c;

import static com.codeborne.selenide.Condition.visible;
import static java.time.Duration.ofSeconds;

/**
 * The Standard Salesforce page that displays Approval ({@link Approval__c}) record information,
 * such as Approval details (Approval Name, Approval's Account, etc...), action buttons,
 * related records and many more.
 */
public class ApprovalPage extends RecordPage {

    //  Related lists headers
    public static final String APPROVAL_HISTORY_RELATED_LIST = "Approval History";

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitUntilLoaded() {
        entityTitle.shouldBe(visible, ofSeconds(60));
    }
}