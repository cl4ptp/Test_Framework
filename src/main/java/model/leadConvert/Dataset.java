package model.leadConvert;

import utilities.JsonUtils;

/**
 * Main test data object for Lead Convert-related tests (see test/java/leads/BaseLeadConvertTest).
 * <p></p>
 * Normally, test data gets loaded from JSON files via {@link JsonUtils} into the objects of this type.
 * After that, actual test interacts with this object.
 * <p></p>
 * This object only contains data sets with test data for different Lead Convert scenarios.
 * <p></p>
 * <b> Note: not to be confused with {@link model.ngbs.testdata.Dataset}! </b>
 * @see OpportunityDataSet
 */
public class Dataset {
    public String description;

    public OpportunityDataSet[] dataSets;
}
