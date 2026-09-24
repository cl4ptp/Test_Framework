package page.salesforce.contact;

import page.salesforce.GenericSalesforceModal;
import page.salesforce.RecordPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Contact;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

/**
 * The Salesforce page that displays
 * Contact({@link Contact}) record information, such as
 * Account Name, Phone, Email,
 * linked Opportunities, etc.
 */
public class ContactRecordPage extends RecordPage {

    //  Notifications
    public static final String CONTACT_CANT_BE_DELETED_ERROR =
            "Contact can't be deleted until it is set as Primary Contact Role for Engage Account";
    public static final String CONTACT_DELETED_SUCCESSFUL_MESSAGE = "Contact \"%s\" was deleted. Undo";

    //  Modal windows
    public final DeleteContactModal deleteModal = new DeleteContactModal();
    public final GenericSalesforceModal warningModal = new GenericSalesforceModal();

    //  Contact Info
    public final SelenideElement contactInfoSectionTitle = $(byText("Contact Information"));

    /**
     * Get notification message on Contact Record Page
     * depending on the Contact that was deleted.
     *
     * @param contactFullName name of the Contact, which was deleted
     * @return notification message about Contact deletion
     * (e.g. <i>"Contact FirstName Lastname was deleted. Undo"</i>)
     */
    public static String getContactDeletionSuccessMessage(String contactFullName) {
        return String.format(CONTACT_DELETED_SUCCESSFUL_MESSAGE, contactFullName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitUntilLoaded() {
        detailsTab.shouldBe(visible, ofSeconds(100));
    }

    /**
     * Press "Delete" button on the Contact Record page.
     * <br/>
     * This method searches "Delete" button among Lightning Experience actions
     * in the upper right corner of the page
     * (even if the button is hidden in the "show more actions" list).
     */
    public void clickDeleteButton() {
        clickDetailPageButton("Delete");
    }
}
