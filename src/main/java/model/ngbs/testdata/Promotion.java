package model.ngbs.testdata;

import model.DataModel;

/**
 * Test data object that represents a specific promotion data.
 * It is used to contain a test data for actual test actions
 * (changing the state of SUT; assertions; etc...)
 * <br/><br/>
 * Normally, this object consists of:
 * <p> - promo code (e.g. "QA-AUTO-DLUNLIMITED-CATEGORY-USD") </p>
 * <p> - promo description (e.g. "$10.00 off to Category: DL unlimited") </p>
 * <p> - charge term (e.g. "Monthly", "Annual", "One - Time") </p>
 * <p> - one or several discount templates data (with discount values, types, etc...) </p>
 */
public class Promotion extends DataModel {
    public String promoCode;
    public String description;
    public String chargeTerm;
    public PromotionDiscountTemplate[] discountTemplates;
}
