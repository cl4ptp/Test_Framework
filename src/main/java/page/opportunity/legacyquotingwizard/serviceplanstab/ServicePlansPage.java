package page.opportunity.legacyquotingwizard.serviceplanstab;

import page.opportunity.legacyquotingwizard.LegacyQuotingWizardPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * 'Service plans' page: one of the tabs on the Legacy Quote Wizard pipeline.
 * <br/><br/>
 * Can be accessed via Quote Wizard, 'Cost Center' tab
 * ({@link LegacyQuotingWizardPage}).
 * <br/><br/>
 * Contains items similar to packages in Quote Wizard 2.0.
 */
public class ServicePlansPage extends LegacyQuotingWizardPage {
    public final ElementsCollection servicePlanListEntries = $$("tr.cQuotingToolTierListEntry");

    private final SelenideElement servicePlanSection = $(".service-plan-tab");
    public final SelenideElement serviceFilter = servicePlanSection.$x(".//*[./div/label/span='Service']//select");
    public final SelenideElement editionFilter = servicePlanSection.$x(".//*[./div/label/span='Edition']//select");
    public final SelenideElement planFilter = servicePlanSection.$x(".//*[./div/label/span='Plan']//select");
    public final SelenideElement numberOfLinesFilter = servicePlanSection.$x(".//*[./div//label='Number of Lines']//input");

    public final SelenideElement saveAndNextButton = $(byText("Save & Next"));
}
