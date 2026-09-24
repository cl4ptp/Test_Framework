package page.opportunity.ngbsquotingwizard.modal;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Case;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Modal window in {@link NGBSQuotingWizardPage} ("Main Quote" tab)
 * activated by clicking on "Report a Problem" button.
 * <p>
 * This dialog creates {@link Case} object for Opportunity.
 * </p>
 */
public class CreateCaseModal {
    private final SelenideElement dialogContainer = $("case-creation-modal");

    public final SelenideElement description = dialogContainer.$("[data-ui-auto='case-description']");
    public final SelenideElement submitButton = dialogContainer.$(withText("Submit"));
}
