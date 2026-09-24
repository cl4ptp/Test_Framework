package page.opportunity.modal;

import page.opportunity.OpportunityRecordPage;
import page.salesforce.GenericSalesforceModal;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static java.time.Duration.ofSeconds;

/**
 * Modal window in {@link OpportunityRecordPage}
 * activated by clicking on 'Sign Up' button.
 * <p>
 * This window allows user to start Sign Up process for Opportunity.
 * </p>
 */
public class SignUpDialogModal extends GenericSalesforceModal {

    //  Buttons
    public SelenideElement cancelButton = dialogContainer.$("[title='Cancel']");
    public SelenideElement continueButton = dialogContainer.$("[title='Continue']");

    /**
     * Constructor for the modal window to locate it via its default header.
     */
    public SignUpDialogModal() {
        super("Sign up");
    }

    /**
     * Select one of the Opportunity's Quotes from the list
     * and submit by clicking 'Continue'.
     *
     * @param quoteId Salesforce ID of the Quote to be selected
     */
    public void selectQuote(String quoteId) {
        dialogContainer.$("[value='" + quoteId + "'] + label")
                .shouldBe(visible, ofSeconds(60))
                .click();
        continueButton.click();
    }
}
