package page.components.packageselector;

import page.lead.convert.LeadConvertPage;
import page.opportunity.ngbsquotingwizard.packagetab.PackagePage;
import page.opportunity.opportunitycreationpages.ngbs.NGBSOpportunityCreationPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.Map;

import static utilities.StringHelper.EMPTY_STRING;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

/**
 * Component that is used for Package selection.
 * Can be found on {@link LeadConvertPage}, {@link PackagePage}
 * and {@link NGBSOpportunityCreationPage}.
 * <br/>
 * User selects packages (e.g. <b>"RingCentral MVP Standard", "RingCentral Meetings Free"</b>, etc...)
 * for its active quote using this component.
 * <br/>
 * In case of Existing Business opportunity, user may also <b>'upgrade'</b> package from billing.
 */
public class PackageSelector {

    /**
     * Text for "badge" on the selected package for opportunities for Existing Business accounts.
     */
    public static final String PACKAGE_FROM_ACCOUNT_BADGE = "Package from the Account";
    public static final String TRIAL_PACKAGE = "Your current Package on Account is Trial. \n" +
            "Please Upgrade on another Package";
    /**
     * Special string value for 'Contract' picklist if no contract is selected for the package.
     */
    public static final String CONTRACT_NONE = "-- None --";
    public static final String NONE_PACKAGE_SELECTED = "--None--";
    public static final Map<String, String> SELECTED_CHARGE_TERM_CLASS_MAP = Map.of(
            "Monthly", "toggle_a-selected",
            "Annual", "toggle_b-selected");
    public static final String BUSINESS_IDENTITY_TOOLTIP_TEXT = "The Default Business Identity is ";

    public final SelenideElement self = $x("//package-select");
    public final ElementsCollection packageFolders = self.$$("package-group > div");
    public final SelenideElement selectedPackage = self.$(".selected[data-ui-auto='package-item']");
    public final SelenideElement loadingBar = self.$("span.placeholder-loading");
    public final SelenideElement loadingMessage = self.$(byText("loading..."));

    //  Package details
    public final SelenideElement selectedPackageName = self.$("package-details h3");
    public final SelenideElement selectedPackageInfo = self.$("div.package-info > span");
    public final SelenideElement selectedPackageVersion = self.$("package-details li");
    public final SelenideElement chargeTermSelector = self.$("package-charge-term > button");
    public final SelenideElement contractSelector = self.$("package-contract select");
    public final ElementsCollection contractOptions = contractSelector.$$("option");
    public final SelenideElement businessIdentitySelect = self.$("#select-business-identity");
    public final SelenideElement businessIdentityToolTip = self.$("package-business-identity icon");
    public final ElementsCollection businessIdentityOptions = businessIdentitySelect.$$("option");

    /**
     * Return the list of available package folders.
     *
     * @return list of package folders (e.g. "Office", "Fax", "Meetings", etc...)
     */
    public List<PackageFolder> getPackageFolders() {
        return packageFolders.stream()
                .map(PackageFolder::new)
                .collect(toList());
    }

    /**
     * Get a Package Folder object by its name.
     * <p>
     * Package folders need to be expanded to get access to its packages.
     * Usually package name corresponds to opportunity's <i>'Tier Name'</i>.
     *
     * @param name name of the folder (e.g. "Office", "Fax", "Meetings", etc...).
     * @return PackageFolder Object that has been found by the name
     */
    public PackageFolder getPackageFolderByName(String name) {
        var packageFolderElement = self.$(byText(name)).closest("package-group/div");
        return new PackageFolder(packageFolderElement);
    }

    /**
     * Get a currently selected package.
     *
     * @return Package Object that is selected
     */
    public Package getSelectedPackage() {
        return new Package(selectedPackage);
    }

    /**
     * Return "class" attribute value for the 'Charge Term' selector
     * corresponding to the expected charge term.
     * <br/>
     * Useful for charge term selector's assertions like:
     * <br/>
     * <code>
     * chargeTermSelector.shouldHave(cssClass(getChargeTermSelectorCssClass("Annual")))
     * </code>
     *
     * @param chargeTerm expected charge term ("Monthly" or "Annual")
     * @return "class" attribute for 'Charge Term' selector (e.g. "toggle_a-selected" for "Monthly")
     */
    public static String getChargeTermSelectorCssClass(String chargeTerm) {
        return SELECTED_CHARGE_TERM_CLASS_MAP.get(chargeTerm);
    }

    /**
     * Wait until the component loads most of its important elements (package list).
     * User may safely interact with any of the component's elements after this method is finished.
     */
    public void waitUntilLoaded() {
        loadingMessage.shouldBe(hidden, ofSeconds(20));
        loadingBar.shouldBe(hidden, ofSeconds(10));
    }

    /**
     * Select the charge term with a given name.
     *
     * @param chargeTerm expected option to be selected
     *                   (e.g. <b>"Monthly", "Annual"</b>)
     */
    public void selectChargeTerm(String chargeTerm) {
        chargeTermSelector.$(byText(chargeTerm)).click();
    }

    /**
     * Select an option from the "Contract" picklist that matches the provided name.
     *
     * @param contractName contract option to be selected (e.g. "Office Contract", "None")
     */
    public void selectContract(String contractName) {
        loadingBar.shouldBe(hidden, ofSeconds(30));
        contractSelector.selectOptionContainingText(contractName);
    }

    /**
     * Select a package from the list of available packages
     * using package field values from test data.
     * <p>
     * <b> Note: method skips selecting 'Charge Term'
     * and leaves 'Contract' picklist value as is (if picklist is available). </b>
     *
     * @param packageFolderName folder name for package (e.g. <b>"Office", "Meetings"</b>...)
     * @param testDataPackage   package test data with package's id, version, type (optional), and contract name
     */
    public void selectPackage(String packageFolderName, model.ngbs.testdata.Package testDataPackage) {
        selectPackage(EMPTY_STRING, packageFolderName, testDataPackage.id, testDataPackage.version, testDataPackage.getType(),
                EMPTY_STRING);
    }

    /**
     * Select a package from the list of available packages
     * using package field values from test data.
     * <p></p>
     * <b> Note: method populates 'Contract' picklist field as a value from test data (if picklist is available)
     * or skips it if a value from test data is blank. </b>
     *
     * @param chargeTerm        package's charge term (e.g. <b>"Monthly", "Annual"</b>)
     * @param packageFolderName folder name for package (e.g. <b>"Office", "Meetings"</b>...)
     * @param testDataPackage   package test data with package's id, version, type (optional), and contract name
     */
    public void selectPackage(String chargeTerm, String packageFolderName,
                              model.ngbs.testdata.Package testDataPackage) {
        selectPackage(chargeTerm, packageFolderName, testDataPackage.id, testDataPackage.version, testDataPackage.getType(),
                testDataPackage.contract);
    }

    /**
     * Select a package from the list of available packages
     * by locating a package using 'data-ui-auto-package-item' attribute
     * on its &lt;li&gt; element in DOM.
     *
     * @param chargeTerm        package's charge term (e.g. <b>"Monthly", "Annual"</b>)
     * @param packageFolderName folder name for package (e.g. <b>"Office", "Meetings"</b>...)
     * @param packageId         id of the package to select (e.g. <b>"18"</b>, <b>"318"</b>, <b>"100"</b>...)
     * @param packageVersion    version of the package to select (e.g. <b>"1"</b>, <b>"3"</b>...)
     * @param packageType       type of the package to select (e.g. <b>"POC"</b>, <b>"Trial"</b>...)
     * @param packageContract   contract name for the package (e.g. <b>"Office Contract", "Meetings Contract"</b>)
     */
    public void selectPackage(String chargeTerm, String packageFolderName, String packageId, String packageVersion,
                              String packageType, String packageContract) {
        var packageFolder = getPackageFolderByName(packageFolderName);
        packageFolder.getSelf().shouldBe(visible, ofSeconds(20));
        packageFolder.expandFolder();
        packageFolder.getPackageByDataAttribute(packageId, packageVersion, packageType).selectPackage();

        if (!chargeTerm.isBlank()) {
            selectChargeTerm(chargeTerm);
        }

        if (packageContract != null && !packageContract.isBlank()) {
            selectContract(packageContract);
            contractSelector.shouldHave(textCaseSensitive(packageContract), ofSeconds(10));
        }
    }

    /**
     * Select a package from the list of available packages
     * using its full name.
     *
     * @param chargeTerm        package's charge term (e.g. <b>"Monthly", "Annual"</b>)
     * @param packageFolderName folder name for package (e.g. <b>"Office", "Meetings"</b>...)
     * @param packageFullName   full name for the package
     *                          (typically includes version number, like <b>"RingCentral Meetings Free - v.1"</b>)
     * @param packageContract   contract name for the package (e.g. <b>"Office Contract", "Meetings Contract"</b>)
     */
    public void selectPackage(String chargeTerm, String packageFolderName, String packageFullName, String packageContract) {
        var packageFolder = getPackageFolderByName(packageFolderName);
        packageFolder.getSelf().shouldBe(visible, ofSeconds(20));
        packageFolder.expandFolder();
        packageFolder.getChildPackageByName(packageFullName).selectPackage();

        if (!chargeTerm.isBlank()) {
            selectChargeTerm(chargeTerm);
        }

        if (packageContract != null && !packageContract.isBlank()) {
            selectContract(packageContract);
            contractSelector.shouldHave(textCaseSensitive(packageContract), ofSeconds(10));
        }
    }
}
