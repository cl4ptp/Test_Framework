package page.opportunity.ngbsquotingwizard.modal;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Modal window in {@link NGBSQuotingWizardPage} ("Main Quote" tab)
 * activated by clicking on "Engage ProServ" button.
 * <p>
 * After engaging "professional services", a <b>ProServ Quote</b> is created
 * and can be accessed via Quote Wizard ("ProServ Quote" tab)
 * by user with profile = <b>"Professional Services"</b>.
 * </p>
 */
public class EngageProServModal {

    //  String constants used on the form
    public static final String HEADER = "Engage Professional Services";
    public static final String LABEL = "Please provide additional details that Professional Services team should know about";

    //  Page elements
    private final SelenideElement dialogContainer = $("engage-proserv-modal");

    public final SelenideElement additionalDetailsInput = dialogContainer.$("[formcontrolname='details']");

    //  Buttons
    public final SelenideElement closeButton = dialogContainer.$("[title='Close']");
    public final SelenideElement cancelButton = dialogContainer.$(byText("Cancel"));
    public final SelenideElement submitButton = dialogContainer.$(byText("Submit"));
}
