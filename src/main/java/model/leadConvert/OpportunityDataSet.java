package model.leadConvert;

import model.ngbs.testdata.BusinessIdentity;
import model.ngbs.testdata.PackageFolder;

/**
 * Data object that represents all the test data for specific potential Opportunity
 * (after successful Lead Convert procedure).
 * <p></p>
 * This object contains a structure for upper-level data (brand, currency, charge term;
 * IDs from NGBS for Existing business accounts; AGS scenario for dynamic accounts generation;
 * Package data for different test packages; etc...).
 * <p></p>
 * <b> Note: not to be confused with {@link model.ngbs.testdata.Dataset}! </b>
 */
public class OpportunityDataSet {
    public String description;
    public String billingId;
    public String packageId;
    public String chargeTerm;
    public String currencyISOCode;
    public String brandName;
    public BusinessIdentity businessIdentity;
    public String forecastedUsers;

    public String scenario;

    public PackageFolder[] packageFolders;
    public PackageFolder[] packageFoldersUpgrade;
}
