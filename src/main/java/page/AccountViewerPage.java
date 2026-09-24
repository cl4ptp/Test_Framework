package page;

import page.opportunity.OpportunityRecordPage;
import page.salesforce.IframePage;
import page.salesforce.account.AccountRecordPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Account Viewer page that shows Account Relations on {@link AccountRecordPage}
 * or on {@link OpportunityRecordPage} after Sign Up attempt, in case of ELA Service Account has errors.
 */
public class AccountViewerPage extends IframePage {

    public final SelenideElement hierarchyContainer = $("#account-viewer-hierarchy-container");

    /**
     * Constructor for Account Viewer page with iframe's web element locator.
     * Defines Account Viewer location.
     */
    public AccountViewerPage() {
        super($(".account-viewer-iframe"));
    }
}
