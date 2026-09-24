package model.ngbs.testdata;

import utilities.JsonUtils;

/**
 * Main test data object for NGBS-related tests (see test/java/ngbs/BaseSalesFlowTest).
 * <p></p>
 * Normally, test data gets loaded from JSON files via {@link JsonUtils} into the objects of this type.
 * After that, actual test interacts with this object.
 * <p></p>
 * This object contains a structure for upper-level data (brand, currency, charge term;
 * IDs from NGBS for Existing business accounts; AGS scenario for dynamic accounts generation;
 * Package data for different test packages; etc...).
 * <p></p>
 * <b> Note: not to be confused with {@link model.leadConvert.Dataset}! </b>
 */
public class Dataset {
    public String description;
    public String comment;
    public String billingId;
    public String packageId;
    public String rcUserId;
    public String chargeTerm;
    public String currencyISOCode;
    public String brandName;
    public BusinessIdentity businessIdentity;
    public int engageUsers;

    public String scenario;

    public PackageFolder[] packageFolders;
    public PackageFolder[] packageFoldersUpgrade;
    public PackageFolder[] packageFoldersUpgradeChargeTerm;
}
