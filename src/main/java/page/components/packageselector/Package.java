package page.components.packageselector;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byCssSelector;

/**
 * A single package item in the {@link PackageSelector}:
 * represents one of the packages to select.
 * <br/>
 * A Package has a name (usually, package name + package version, like "RingCentral MVP Standard - v.3")
 * and an info badge (with currency info, like "USD", or tags from NGBS like "Testing").
 */
public class Package {

    /**
     * Attribute whose value contains offerType, PackageID and Package version
     * in format: "offerType_packageId_packageVersion".
     */
    public static final String PACKAGE_INFO_ATTRIBUTE = "data-ui-auto-package-item";

    private final SelenideElement packageItem;
    private final By name = byCssSelector("[data-label='Package Name']");
    private final By badge = byCssSelector("badge > div");

    /**
     * Constructor with web element as a parameter.
     *
     * @param packageItem SelenideElement that used to locate package item element in DOM.
     */
    public Package(SelenideElement packageItem) {
        this.packageItem = packageItem;
    }

    /**
     * Return actual web element behind Package component.
     * <p></p>
     * Useful if test needs to perform actions on the web element itself
     * via Selenide framework actions (waits, assertions, etc...)
     *
     * @return web element that represents Package in the DOM.
     */
    public SelenideElement getSelf() {
        return packageItem;
    }

    /**
     * Return the name element of the current package.
     *
     * @return web element for the name of the current package.
     */
    public SelenideElement getName() {
        return packageItem.$(name);
    }

    /**
     * Return the badge element of the current package.
     * It displays as a badge on the right,
     * and can contain Currency, source of package
     * or other package info.
     *
     * @return web element for the badge of the current package.
     */
    public SelenideElement getBadge() {
        return packageItem.$(badge);
    }

    /**
     * Select the current package.
     *
     * @return selected/current package item
     */
    public Package selectPackage() {
        packageItem.click();
        return this;
    }
}
