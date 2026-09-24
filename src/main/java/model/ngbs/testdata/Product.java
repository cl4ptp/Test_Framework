package model.ngbs.testdata;

import model.DataModel;
import page.opportunity.ngbsquotingwizard.producttab.ProductsPage;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object that represents a specific product data.
 * It is used to contain a test data for actual test actions
 * (changing the state of SUT; assertions; etc...)
 * <p></p>
 * Normally, this object consists of:
 * <p> - name of the product (e.g. "DigitalLine Unlimited Standard") </p>
 * <p> - data name (e.g. "LC_DL-UNL_50" for DL Unlimited) </p>
 * <p> - cbox name: subfolder in {@link ProductsPage} where similar products are grouped
 * (e.g. "Rental Phones") </p>
 * <p> - product's charge term (e.g. "Monthly", "Annual", "One - Time") </p>
 * <p> - product's quantity (e.g. 35) </p>
 * <p> - product's price (e.g. 42.99) </p>
 * <p> - discounts: type and amount (e.g. "%" and "4") </p>
 */
@JsonInclude(value = NON_NULL)
public class Product extends DataModel {
    public String name;
    public String dataName;
    public String cbox;
    public String chargeTerm;

    /**
     * 'List Price' on the Cart Item.
     */
    public String price;
    /**
     * 'Your Price' on the Cart Item (including discounts).
     */
    public String yourPrice;

    /**
     * 'Quantity' on the Cart Item (new quantity).
     */
    public Integer quantity;
    /**
     * 'Existing Quantity' on the Cart Item (for Existing Business Accounts).
     */
    public Integer existingQuantity;
    /**
     * Absolute max quantity of product to be added at once.
     */
    public Integer maxQuantity;

    public String discountType;
    /**
     * 'Discount' on the Cart Item.
     * Can be used to store an existing discount and a new discount
     * (depends on the test context).
     */
    public Integer discount;
    /**
     * New discount to be set.
     * Useful when the test needs to check the initial discount (see 'discount' variable)
     * and set a new one.
     */
    public Integer newDiscount;

    public PriceRater[] priceRater;

    public String comment;
}
