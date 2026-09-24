package page.opportunity;

import page.opportunity.legacyquotingwizard.LegacyQuotingWizardPage;
import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import page.salesforce.VisualforcePage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Opportunity;

import static utilities.Constants.BASE_VF_URL;
import static com.codeborne.selenide.Selenide.*;

/**
 * Page with custom Quote Wizard functionality.
 * <br/>
 * It contains several tabs:
 * <p> - Main Quote (Quote Wizard 2.0 / NGBS) </p>
 * <p> - Contact Center (Legacy Quote Wizard) </p>
 * <p> - ProServ Quote (Legacy Quote Wizard) </p>
 * <br/>
 * Normally, Quote Wizard is located on the {@link OpportunityRecordPage}.
 * But it also can be opened via direct link (as any VF page).
 */
public class WizardBodyPage extends VisualforcePage {

    //  Iframes
    /**
     * Web element for Quote Wizard's iframe.
     * Default for most Sales Reps.
     */
    public static final SelenideElement WIZARD_IFRAME_DEFAULT = $x("//*[@id='tab-3']//iframe");
    /**
     * Web element for Quote Wizard's iframe.
     * Used for ProServ users.
     */
    public static final SelenideElement WIZARD_IFRAME_PRO_SERV =
            $x("//records-record-layout-section[.//span[contains(text(),'Quot')]]//iframe");

    //  Spinner
    public final SelenideElement spinner = $("spinner div.slds-spinner");
    public final SelenideElement spinnerContainer = $(".slds-spinner_container");

    //  Tab buttons
    public final SelenideElement mainQuoteTab = $("#main-tab");
    public final SelenideElement contactCenterTab = $("#cc-tab");
    public final SelenideElement proServTab = $("#proserv-tab");

    //  Tabs
    public final NGBSQuotingWizardPage mainQuoteWizardPage = new NGBSQuotingWizardPage();
    public final LegacyQuotingWizardPage contactCenterWizardPage = new LegacyQuotingWizardPage();
    public final LegacyQuotingWizardPage proServWizardPage = new LegacyQuotingWizardPage();

    //  Wizard Placeholder
    public final SelenideElement wizardPlaceholder = $(".placeholder-text");

    public final SelenideElement openInNewTabButton = $("#newtab-tab");

    /**
     * Default no-arg constructor.
     * Defines default Quote Wizard location on the Opportunity record page
     * (for Sales Reps, Deal Desk agents, etc...).
     */
    public WizardBodyPage() {
        super("Quote Wizard");
    }

    /**
     * Parameterized constructor.
     * Defines Quote Wizard location depending on the provided iframe.
     *
     * @param wizardIframe web element for iframe where Quote Wizard is located.
     */
    public WizardBodyPage(SelenideElement wizardIframe) {
        super(wizardIframe);
    }

    /**
     * Open Quote Wizard page via direct link.
     * QW is opened in the Visualforce container without any additional wrappers
     * (iframes, web components, etc...).
     *
     * @param opportunity Opportunity record for which the Quote Wizard is being open
     * @return reference to the opened Quote Wizard page
     */
    public WizardBodyPage openPage(Opportunity opportunity) {
        open(BASE_VF_URL + "/apex/QuoteWizard?id=" + opportunity.getId());
        return this;
    }
}
