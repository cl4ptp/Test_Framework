package page.opportunity.ngbsquotingwizard.modal;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Modal window in {@link NGBSQuotingWizardPage} that appears every time
 * when there are some unsaved changes on the active tab, and user switches to another tab.
 */
public class UnsavedChangesWarningModal {

    //  String constants used on the form
    public static final String HEADER = "Warning!";
    private static final String UNSAVED_CHANGES_ON_TAB_WARNING =
            "You have unsaved changes on %s tab. Would you like to Save them?";
    private static final String REMOVE_FREE_SERVICE_CREDIT_WARNING =
            "This account currently has %d free service credit. Moving customer off from the contract will cause free " +
                    "service credit to be removed from this account.";
    public static final String PACKAGE_CHANGE_CC_QUOTE_DELETION_WARNING =
            "You are going to change the package selection. Your Contact Center quote will be deleted";
    public static final String PRIMARY_QUOTE_CHANGE_CC_QUOTE_DELETION_WARNING =
            "You are about to change the primary quote. Your Contact Center quote will be deleted";

    //  Page elements
    private final SelenideElement dialogContainer = $("confirmation-modal");

    public final SelenideElement warningMessage = dialogContainer.$(".slds-modal__content.slds-p-around_medium");
    public final SelenideElement backdrop = dialogContainer.$(".slds-fade-in-open");

    //  Buttons
    public final SelenideElement closeButton = dialogContainer.$("[title='Close']");
    public final SelenideElement cancelButton = dialogContainer.$(byText("Cancel"));
    public final SelenideElement discardButton = dialogContainer.$(byText("Discard"));
    public final SelenideElement confirmButton = dialogContainer.$(byText("Confirm"));

    /**
     * Get warning message on the dialog window
     * depending on the active tab that's being switched.
     *
     * @param activeTabName name of the active tab with unsaved changes (e.g. "Products")
     * @return warning message about unsaved changes
     * (e.g. <i>"You have unsaved changes on Products tab. Would you like to Save them?"</i>)
     */
    public String getWarningMessage(String activeTabName) {
        return String.format(UNSAVED_CHANGES_ON_TAB_WARNING, activeTabName);
    }

    /**
     * Get warning message that Free Service Credit on Account will be removed on the dialog window
     * depending on amount of Free Service Credit.
     *
     * @param freeServiceCreditAmount amount of Free Service Credit existing on Account.
     * @return warning message that Free Service Credit on Account will be removed.
     * (e.g. <i>"This account currently has 100 free service credit. Moving customer off from the contract will cause
     * free service credit to be removed from this account."</i>)
     */
    public String getFSCWarningMessage(double freeServiceCreditAmount) {
        return String.format(REMOVE_FREE_SERVICE_CREDIT_WARNING, (int) freeServiceCreditAmount);
    }
}
