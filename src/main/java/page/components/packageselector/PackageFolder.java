package page.components.packageselector;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static page.components.packageselector.Package.PACKAGE_INFO_ATTRIBUTE;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactTextCaseSensitive;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

/**
 * Represents an expandable folder with a name ("Office", "Fax", "Meetings", etc...),
 * and with packages (see {@link Package}) inside.
 * Can be found in the {@link PackageSelector}.
 * <br/><br/>
 * Note: packages inside the folder don't exist and are not visible in the DOM
 * if the folder is not expanded. Make sure that the folder is expanded first
 * before calling any UI actions/assertions with package elements inside.
 */
public class PackageFolder {
    private final SelenideElement packageFolder;
    private final By expandButton = byCssSelector("c-button");
    private final By childPackages = byCssSelector("package li");

    /**
     * Constructor with a web element as a parameter.
     *
     * @param packageFolder SelenideElement that is used to locate the package folder element in the DOM
     */
    public PackageFolder(SelenideElement packageFolder) {
        this.packageFolder = packageFolder;
    }

    /**
     * Return the actual web element behind the Package component.
     * <p></p>
     * Useful if the test needs to perform actions on the web element itself
     * via Selenide framework actions (waits, assertions, etc...).
     *
     * @return SelenideElement that represents a Package in the DOM
     */
    public SelenideElement getSelf() {
        return packageFolder;
    }

    /**
     * Return the button that expands/collapses the current Package folder.
     *
     * @return expand button of the current Package folder.
     */
    public SelenideElement getExpandButton() {
        return packageFolder.$(expandButton);
    }

    /**
     * Return the packages in the current package folder
     * as a Java List collection.
     * <br/>
     * Note: make sure that the folder is expanded
     * before calling UI-actions/assertions for these packages!
     *
     * @return child packages as a list
     */
    public List<Package> getPackagesList() {
        return new ArrayList<>(getPackagesElements())
                .stream()
                .map(Package::new)
                .collect(toList());
    }

    /**
     * Return the packages in the current package folder
     * as a collection of web elements.
     * <br/>
     * Note: make sure that the folder is expanded
     * before calling UI-actions/assertions for these elements!
     *
     * @return child packages as a collection of web elements
     */
    public ElementsCollection getPackagesElements() {
        return packageFolder.$$(childPackages);
    }

    /**
     * Get a child package in the current package folder
     * using its display name.
     * <br/>
     * Note: make sure that the folder is expanded
     * before calling UI-actions/assertions for this package!
     *
     * @param packageName full name of the package to find (e.g. "RingCentral MVP Standard - v.1")
     * @return child package with a given full name
     */
    public Package getChildPackageByName(String packageName) {
        var packageElement = packageFolder.$$("[data-label='Package Name']")
                .findBy(exactTextCaseSensitive(packageName))
                .ancestor("package/li");
        return new Package(packageElement);
    }

    /**
     * Get a child package in the current package folder
     * using its 'data-ui-auto-package-item' attribute value.
     * <br/>
     * Note: make sure that the folder is expanded
     * before calling UI-actions/assertions for this package!
     *
     * @param packageId      id of the package that will be selected
     * @param packageVersion version of the package that will be selected
     * @param packageType    type of the package that will be selected
     *                       (e.g. 'Regular', 'POC', 'Trial')
     * @return package that will be selected
     */
    public Package getPackageByDataAttribute(String packageId, String packageVersion, String packageType) {
        var packageInfoAttributeValue = String.join("_", packageType, packageId, packageVersion);
        var packageElement = packageFolder.$(String.format("[%s='%s']",
                PACKAGE_INFO_ATTRIBUTE, packageInfoAttributeValue));

        return new Package(packageElement);
    }

    /**
     * Expand the current package folder.
     *
     * @return current package folder
     */
    public PackageFolder expandFolder() {
        if (!isExpanded()) {
            getExpandButton().scrollIntoView("{block: \"center\"}").click();
            getPackagesElements().shouldHave(sizeGreaterThan(0), ofSeconds(10));
        }
        return this;
    }

    /**
     * Check if the current package folder is expanded or not.
     *
     * @return true if folder is expanded, false if not.
     */
    private boolean isExpanded() {
        packageFolder.shouldHave(attribute("class"));
        return packageFolder.getAttribute("class").contains("slds-is-open");
    }
}
