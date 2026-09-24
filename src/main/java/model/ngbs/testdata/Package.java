package model.ngbs.testdata;

import model.DataModel;
import page.components.packageselector.PackageSelector;

/**
 * Data object that represents a specific package data.
 * It is used to contain a test data for actual test actions
 * (changing the state of SUT; assertions; etc...)
 * <p></p>
 * Normally, this object consists of:
 * <p> - name of the package (e.g. "RingCentral MVP Standard") </p>
 * <p> - package id and version (e.g. "18" and "1") </p>
 * <p> - contract name (e.g. "Office Contract", "None") </p>
 * <p> - products to add to the quote (e.g. DigitalLine, various phones...) </p>
 * <p> - products from NGBS (for Existing Business Customers) </p>
 * <p> - other products </p>
 */
public class Package extends DataModel {

    //  for 'type' variable
    public static final String REGULAR_TYPE = "Regular";

    public String name;
    public String id;
    public String version;
    public String type;
    public String contract;
    public String contractExtId;
    public ContractTerms contractTerms;

    public Product[] products;
    public Product[] productsFromBilling;
    public Product[] productsDefault;
    public Product[] productsOther;
    public Product[] taxes;
    public Promotion[] promotions;

    /**
     * Get full name for the package.
     * Normally, it includes the display name and the version.
     * <p>
     * Useful when working with {@link PackageSelector}.
     * </p>
     *
     * @return package's full name (e.g. "RingCentral MVP Standard - v.1")
     */
    public String getFullName() {
        return name + " - v." + version;
    }

    /**
     * Get package edition by extracting it from the package name
     * (normally, the last word in it).
     *
     * @return package edition (e.g. "Standard", "Essentials", "Ultimate")
     */
    public String getEdition() {
        return name.substring(name.lastIndexOf(" ") + 1);
    }

    /**
     * Get type of the package.
     * <p>
     * In most cases it is equal to 'Regular',
     * otherwise is taken from 'type' field of test data file.
     * </p>
     *
     * @return package type value (e.g. 'Regular', 'POC', etc.)
     */
    public String getType() {
        return type != null ? type : REGULAR_TYPE;
    }
}
