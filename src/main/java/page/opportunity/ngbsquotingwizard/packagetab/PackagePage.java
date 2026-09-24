package page.opportunity.ngbsquotingwizard.packagetab;

import page.components.packageselector.PackageSelector;
import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

/**
 * Package tab in {@link NGBSQuotingWizardPage}
 * that contains {@link PackageSelector} component.
 */
public class PackagePage extends NGBSQuotingWizardPage {

    //  Package Selector Section
    public final PackageSelector packageSelector = new PackageSelector();

    //  Package actions
    public final SelenideElement saveButton = $("[data-ui-auto='save-package']");
    public final SelenideElement discardButton = $("[data-ui-auto='discard-package']");
    public final SelenideElement upgradeButton = $("[data-ui-auto='upgrade']");
    public final SelenideElement cancelUpgradeButton = $("[data-ui-auto='cancel-upgrade']");

    /**
     * Open Package tab by clicking on the tab's button.
     */
    public PackagePage openTab() {
        packageTabButton.click();
        packageSelector.chargeTermSelector.shouldBe(visible, ofSeconds(30));
        return this;
    }

    /**
     * Press 'Save' button.
     * <br/>
     * Method also waits for the loading spinner to disappear.
     */
    public void saveChanges() {
        saveButton.click();

        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(90));
    }

    /**
     * Press 'Save' button and confirm changes in the pop-up modal window.
     * <br/>
     * Method also waits for the loading spinner to disappear.
     * <br/>
     * Note: use it when selecting a package different from already selected one.
     */
    public void saveChangesAndConfirm() {
        saveButton.click();
        unsavedChangesDialog.confirmButton.click();

        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(90));
    }
}
