package page.opportunity.legacyquotingwizard.phasetab;

import page.opportunity.legacyquotingwizard.LegacyQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.valueOf;

/**
 * 'Phase' page: one of the tabs on the Legacy Quote Wizard pipeline.
 * <br/><br/>
 * Can be accessed via Legacy Quote Wizard 'ProServ Quote' tab ({@link LegacyQuotingWizardPage}),
 * don't exist on 'Contact Center' tab.
 * <br/><br/>
 * Contains phases that were added and products that assigned to phases.
 */
public class PhasePage extends LegacyQuotingWizardPage {

    public final SelenideElement addPhaseButton = $(byText("Add phase"));
    public final SelenideElement estimatedCompletionDate = $(".datePicker-openIcon");
    public final SelenideElement addAllUnassignedItems = $(byText("Move all unassigned items here"));
    public final SelenideElement savePhasesButton = $x("(//button[text()='Save'])[2]");

    /**
     * Add all unassigned products to the available Phase.
     */
    public void addAllUnassignedItemsToPhase() {
        // We need to hover over any element on the 'Phase' tab to make 'Move all unassigned items here' button visible
        estimatedCompletionDate.hover();
        addAllUnassignedItems.click();
    }

    /**
     * Set tomorrow's date in 'Estimated Completion date' field.
     */
    public void selectDefaultEstimatedCompletionDate() {
        var estimateDate = LocalDate.now().plusDays(1);
        estimatedCompletionDate.click();
        legacyDatePicker.setDate(
                valueOf(estimateDate.getDayOfMonth()),
                valueOf(estimateDate.getMonth()),
                valueOf(estimateDate.getYear()));
    }

    /**
     * Press 'Save' button on the Phase Tab of Legacy Quoting Wizard.
     */
    public void savePhases() {
        savePhasesButton.click();
        waitUntilLoaded();
    }
}
