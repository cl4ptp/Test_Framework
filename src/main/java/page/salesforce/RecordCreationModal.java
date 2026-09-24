package page.salesforce;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.SObject;

import static com.codeborne.selenide.Selectors.byText;

/**
 * Base class for Salesforce record creation modal window.
 * <p>
 * Used for creating different Salesforce {@link SObject} records.
 * Contains some common fields and buttons for every type of Salesforce record modal window creation,
 * like 'Save' and 'Close' button, errors list, etc.
 * </p>
 */
public abstract class RecordCreationModal extends GenericSalesforceModal {

    public final ErrorsPopUpModal errorsPopUpModal = new ErrorsPopUpModal();

    /**
     * Constructor for SObject record creation modal window with initialization
     * of its dialog container's locator using its header's title.
     *
     * @param modalWindowHeaderSubstring string that header's title of the modal window contains
     */
    public RecordCreationModal(String modalWindowHeaderSubstring) {
        super(modalWindowHeaderSubstring);
    }

    /**
     * Get 'Save' button in record creation modal window.
     *
     * @return SelenideElement that represents 'Save' button
     */
    public SelenideElement getSaveButton() {
        return dialogContainer.$(byText("Save"));
    }

    /**
     * Get collection of section headers in record creation modal window.
     *
     * @return collection of web elements that represent section headers
     */
    public ElementsCollection getSectionHeaders() {
        return dialogContainer.$$("records-record-layout-section h3");
    }
}
